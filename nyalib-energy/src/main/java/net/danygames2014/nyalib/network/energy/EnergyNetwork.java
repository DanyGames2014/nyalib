package net.danygames2014.nyalib.network.energy;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.energy.EnergyConductor;
import net.danygames2014.nyalib.energy.EnergyConsumer;
import net.danygames2014.nyalib.energy.EnergySource;
import net.danygames2014.nyalib.network.*;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.Comparator;
import java.util.Map;

public class EnergyNetwork extends Network {
    // Energy consumers on the network
    private final Object2ObjectOpenHashMap<Vec3i, ConsumerEntry> consumerCache;

    // Cache of paths to energy consumers
    private final Object2ObjectOpenHashMap<Vec3i, ObjectArrayList<ConsumerPath>> consumerPathCache;

    // The energy flow values in the last tick
    private final Object2ObjectOpenHashMap<Vec3i, EnergyFlowEntry> energyFlow;

    private static int energyNetworkCount = 0;
    private final int updateCheckOffset;
    
    public EnergyNetwork(World world, NetworkType type) {
        super(world, type);
        
        // Offseting the updates to distribute them evenly among ticks
        energyNetworkCount++;
        this.updateCheckOffset = energyNetworkCount & 0b00011111;
        
        consumerCache = new Object2ObjectOpenHashMap<>();
        consumerPathCache = new Object2ObjectOpenHashMap<>();
        energyFlow = new Object2ObjectOpenHashMap<>();
    }

    @Override
    public void update() {
        buildCaches();

        super.update();
    }

    @Override
    public void tick() {
        super.tick();

        if ((world.getTime() & 0b00011111) == this.updateCheckOffset) {
            if (checkLoadedChanged()) {
                update();
            }
        }
        
        
        for (EnergyFlowEntry entry : energyFlow.values()) {
            entry.energyFlow = 0;
        }
    }
    
    public void buildCaches() {
        // Clear Caches
        consumerCache.clear();
        consumerPathCache.clear();
        energyFlow.clear();

        // Build Caches
        for (NetworkComponentEntry componentEntry : components.values()) {
            // For network edges, check if theyre valid consumers
            if (componentEntry.component instanceof NetworkEdgeComponent) {
                if (world.getBlockEntity(componentEntry.pos.x, componentEntry.pos.y, componentEntry.pos.z) instanceof EnergyConsumer consumer) {
                    consumerCache.put(componentEntry.pos, new ConsumerEntry(componentEntry, consumer));
                }

                // For nodes, check if theyre valid conductors
            } else if (componentEntry.component instanceof NetworkNodeComponent) {
                if (componentEntry.block instanceof EnergyConductor conductor) {
                    energyFlow.put(componentEntry.pos, new EnergyFlowEntry(componentEntry, conductor, 0));
                }
            }
        }
    }

    // Checking if the loaded state of part of the network has changed
    private final ObjectOpenHashSet<Vec3i> loadedPositions = new ObjectOpenHashSet<>(32, 0.5f);
    private final ObjectOpenHashSet<Vec3i> loadedPositionsRemoveQueue = new ObjectOpenHashSet<>(16, 0.75f);

    /**
     * Checks if the loaded state of atleast one component of the network has changed
     * @return true if the loaded state of atleast one component of the network has changed, false otherwise
     */
    public boolean checkLoadedChanged() {
        boolean changed = false;
        
        // Loop through all the components
        for (NetworkComponentEntry componentEntry : components.values()) {
            var pos = componentEntry.pos;
            
            // Check if the chunk is loaded
            if (world.chunkSource.isChunkLoaded(pos.x >> 4, pos.z >> 4)) {
                // If the chunk is loaded, check if the component was not loaded during the last check
                if (!loadedPositions.contains(pos)) {
                    loadedPositions.add(pos);
                    changed = true;
                }
            } else {
                // If the chunk is not loaded, check if the component was loaded during the last check
                if (loadedPositions.contains(pos)) {
                    loadedPositionsRemoveQueue.add(pos);
                    changed = true;
                }
            }
        }
        
        // Remove the unloaded positions from the loaded positions set
        if (!loadedPositionsRemoveQueue.isEmpty()) {
            for (Vec3i pos : loadedPositionsRemoveQueue) {
                loadedPositions.remove(pos);
            }
            loadedPositionsRemoveQueue.clear();
        }
        
        return changed;
    }

    //long time = System.nanoTime();
    //System.out.println(((System.nanoTime() - time) / 1000) + "uS");

    // Reuseable objects in provideEnergy
    private final ObjectArrayList<ConsumerPath> validConsumers = new ObjectArrayList<>(10);
    private final ObjectArrayList<ConsumerPath> validConsumersRemoveQueue = new ObjectArrayList<>(10);

    /**
     * Provide energy to the energy net
     *
     * @param source    The source of energy
     * @param sourcePos The position of the source
     * @param voltage   The voltage provided
     * @param energy    The energy provided
     * @return The energy used
     */
    @SuppressWarnings("unused")
    public int provideEnergy(EnergySource source, Vec3i sourcePos, int voltage, int energy) {
        // If there is no energy, immediately return
        if (energy <= 0) {
            return 0;
        }

        // Tracking how much energy is left to distribute
        int remainingEnergy = energy;
        
        // Fetch valid consumers
        validConsumers.clear();
        validConsumersRemoveQueue.clear();
        validConsumers.addAll(getValidConsumers(sourcePos));

        /*
            Pass 1: Distribute the energy evenly according to the pathing cost
        */
        double totalConductance = 0.0;
        for (ConsumerPath path : validConsumers) {
            if (path.consumer.getRemainingCapacity() > 0) {
                totalConductance += path.conductance;
            }
        }
        
        // If all consumers are full, return to avoid division by zero
        if (totalConductance <= 0.0) {
            return 0;
        }

        for (ConsumerPath path : validConsumers) {
            EnergyConsumer consumer = path.consumer;
            int consumerCapacity = consumer.getRemainingCapacity();
            if (consumerCapacity <= 0) {
                validConsumersRemoveQueue.add(path);
                continue;
            }
            
            // Calculates the share of energy this consumer will ideally get
            int idealShare = (int) Math.round(energy * (path.conductance / totalConductance));
            
            // Calculates the share of energy this consumer will actually get
            int realShare = Math.min(Math.min(idealShare, remainingEnergy), consumerCapacity);

            if (realShare > 0) {
                remainingEnergy -= traverseEnergy(consumer, path.path.endFace, path.path, voltage, realShare);

                // If the consumer is now full, remove it from the list of valid consumers for the potential 2nd pass
                if (consumer.getRemainingCapacity() <= 0) {
                    validConsumersRemoveQueue.add(path);
                }
                
                // If there is no more energy left, stop distributing
                if (remainingEnergy <= 0) {
                    break;
                }
            }
        }

        // Early return if no energy is remaining
        if (remainingEnergy <= 0) {
            // We don't need a minus remaining energy here
            return energy;
        }
        
        // Remove the satisfied consumers
        validConsumers.removeAll(validConsumersRemoveQueue);
        
        /*
            Pass 2: Distribute remaining energy
        */
        for (ConsumerPath path : validConsumers) {
            EnergyConsumer consumer = path.consumer;
            int realShare = Math.min(consumer.getRemainingCapacity(), remainingEnergy);

            remainingEnergy -= traverseEnergy(consumer, path.path.endFace, path.path, voltage, realShare);

            // If there is no more energy left, stop distributing
            if (remainingEnergy <= 0) {
                break;
            }
        }

        return energy - remainingEnergy;
    }

    private int traverseEnergy(EnergyConsumer consumer, Direction consumerFace, NetworkPath path, int voltage, int energy) {
        double lostEnergy = 0;

        // Traverse all the nodes the energy will go thru
        for (Vec3i node : path.path) {
            // Get the flow entry for the given node
            EnergyFlowEntry flowEntry = energyFlow.get(node);

            // If ithe entry is null, the block doesnt have a EnergyConductor implemented on it
            if (flowEntry != null) {
                // Add the energy loss
                lostEnergy += flowEntry.conductor.getEnergyLossPerBlock();
            }
        }

        energy -= (int) Math.floor(lostEnergy);

        // If all the energy would have been wasted, the transfer will not occur
        if (energy <= 0) {
            return 0;
        }
        
        int providedEnergy = consumer.receiveEnergy(consumerFace, voltage, energy);

        // Traverse all the nodes the energy will go thru
        for (Vec3i node : path.path) {
            // Get the flow entry for the given node
            EnergyFlowEntry flowEntry = energyFlow.get(node);

            // If ithe entry is null, the block doesnt have a EnergyConductor implemented on it
            if (flowEntry != null) {
                // Add the energyFlow
                flowEntry.energyFlow += providedEnergy;

                // Check for breakdown voltage
                if (voltage > flowEntry.conductor.getBreakdownVoltage(world, flowEntry.componentEntry)) {
                    flowEntry.conductor.onBreakdownVoltage(world, flowEntry.componentEntry, voltage);
                }

                // Check for breakdown power
                if (flowEntry.energyFlow > flowEntry.conductor.getBreakdownPower(world, flowEntry.componentEntry)) {
                    flowEntry.conductor.onBreakdownPower(world, flowEntry.componentEntry, voltage, flowEntry.energyFlow);
                }
            }
        }

        return providedEnergy;
    }

    // Empty list to return if the position is not loaded
    private final ObjectArrayList<ConsumerPath> empty = new ObjectArrayList<>();
    
    /**
     * Retrieves all the valid reachable consumers from the source position
     *
     * @param source The position of the source
     * @return An ObjectArrayList of paths to valid consumers
     */
    public ObjectArrayList<ConsumerPath> getValidConsumers(Vec3i source) {
        // If the world position is not loaded, return empty paths
        if (!world.isPosLoaded(source.x, source.y, source.z)) {
            consumerPathCache.remove(source);
            return empty;
        }
        
        // If the paths are cached, return them
        if (consumerPathCache.containsKey(source)) {
            return consumerPathCache.get(source);
        }
        
        // If it is not cached, compute the paths
        ObjectArrayList<ConsumerPath> consumerPaths = new ObjectArrayList<>();

        for (Map.Entry<Vec3i, ConsumerEntry> consumer : this.consumerCache.entrySet()) {
            if (consumer.getKey().equals(source)) {
                continue;
            }

            NetworkPath path = this.getPath(source, consumer.getKey());

            if (path == null) {
                NyaLib.LOGGER.debug("Path was null when getting valid consumerPaths");
                continue;
            }

            if (consumer.getValue().consumer.canReceiveEnergy(path.endFace)) {
                consumerPaths.add(new ConsumerPath(consumer.getValue().consumer, path));
            }
        }

        consumerPathCache.put(source, consumerPaths);
        consumerPaths.sort(Comparator.comparingDouble(path -> path.path.cost));
        return consumerPaths;
    }

    public EnergyFlowEntry getFlowEntry(int x, int y, int z) {
        return getFlowEntry(new Vec3i(x, y, z));
    }

    public EnergyFlowEntry getFlowEntry(Vec3i pos) {
        return energyFlow.get(pos);
    }

    public static final class ConsumerPath {
        public final EnergyConsumer consumer;
        public final NetworkPath path;
        public final double conductance;

        public ConsumerPath(EnergyConsumer consumer, NetworkPath path) {
            this.consumer = consumer;
            this.path = path;
            this.conductance = 1.0D / path.cost;
        }
    }

    public record ConsumerEntry(NetworkComponentEntry componentEntry, EnergyConsumer consumer) {
    }

    public static final class EnergyFlowEntry {
        public final NetworkComponentEntry componentEntry;
        public final EnergyConductor conductor;
        public int energyFlow;

        public EnergyFlowEntry(NetworkComponentEntry componentEntry, EnergyConductor conductor, int energyFlow) {
            this.componentEntry = componentEntry;
            this.conductor = conductor;
            this.energyFlow = energyFlow;
        }
    }
}
