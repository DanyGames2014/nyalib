package net.danygames2014.nyalib.network.energy;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.energy.EnergyConductor;
import net.danygames2014.nyalib.energy.EnergyConsumer;
import net.danygames2014.nyalib.energy.EnergySource;
import net.danygames2014.nyalib.network.*;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EnergyNetwork extends Network {
    // Energy consumers on the network
    private final HashMap<Vec3i, ConsumerEntry> consumers;

    // Cache of paths to energy consumers
    private final HashMap<Vec3i, ArrayList<ConsumerPath>> consumerCache;

    // The energy flow values in the last tick
    private final Object2ObjectOpenHashMap<Vec3i, EnergyFlowEntry> energyFlow;

    public EnergyNetwork(World world, NetworkType type) {
        super(world, type);
        consumers = new HashMap<>();
        consumerCache = new HashMap<>();
        energyFlow = new Object2ObjectOpenHashMap<>();
    }

    @Override
    public void update() {
        // Clear Caches
        consumers.clear();
        consumerCache.clear();
        energyFlow.clear();

        // Build Caches
        for (NetworkComponentEntry componentEntry : components.values()) {
            // For network edges, check if theyre valid consumers
            if (componentEntry.component() instanceof NetworkEdgeComponent) {
                if (world.getBlockEntity(componentEntry.pos().x, componentEntry.pos().y, componentEntry.pos().z) instanceof EnergyConsumer consumer) {
                    consumers.put(componentEntry.pos(), new ConsumerEntry(componentEntry, consumer));
                }

            // For nodes, check if theyre valid conductors
            } else if (componentEntry.component() instanceof NetworkNodeComponent) {
                if (componentEntry.block() instanceof EnergyConductor conductor) {
                    energyFlow.put(componentEntry.pos(), new EnergyFlowEntry(componentEntry, conductor, 0));
                }
            }
        }

        super.update();
    }

    @Override
    public void tick() {
        super.tick();

        for (EnergyFlowEntry entry : energyFlow.values()) {
            entry.energyFlow = 0;
        }
    }

    //long time = System.nanoTime();
    //System.out.println(((System.nanoTime() - time) / 1000) + "uS");

    /**
     * Provide energy to the energy net
     *
     * @param source    The source of energy
     * @param sourcePos The position of the source
     * @param voltage   The voltage provided
     * @param energy     The amperage provided
     * @return The energy used
     */
    public int provideEnergy(EnergySource source, Vec3i sourcePos, int voltage, int energy) {
        int remainingEnergy = energy;

        for (ConsumerPath consumerPath : getValidConsumers(sourcePos)) {
            EnergyConsumer consumer = consumerPath.consumer;
            NetworkPath path = consumerPath.path;

            // Check if the consumer can even accept more energy
            if (consumer.getRemainingCapacity() > 0) {
                // Insert Energy into the consumers
                int usedEnergy = traverseEnergy(consumer, path.endFace, path, voltage, remainingEnergy);

                // Reduce the remaining amount
                remainingEnergy -= usedEnergy;

                // If there is now energy remaining, end it
                if (remainingEnergy <= 0) {
                    return energy;
                }
            }

        }

        return energy - remainingEnergy;
    }

    private int traverseEnergy(EnergyConsumer consumer, Direction consumerFace, NetworkPath path, int voltage, int energy) {
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

    /**
     * Retrieves all the valid reachable consumers from the source position
     *
     * @param source The position of the source
     * @return An ArrayList of paths to valid consumers
     */
    public ArrayList<ConsumerPath> getValidConsumers(Vec3i source) {
        if (consumerCache.containsKey(source)) {
            return consumerCache.get(source);
        } else {
            ArrayList<ConsumerPath> consumers = new ArrayList<>();

            for (Map.Entry<Vec3i, ConsumerEntry> consumer : this.consumers.entrySet()) {
                if (consumer.getKey().equals(source)) {
                    continue;
                }

                NetworkPath path = this.getPath(source, consumer.getKey());

                if (path == null) {
                    NyaLib.LOGGER.debug("Path was null when getting valid consumers");
                    continue;
                }

                if (consumer.getValue().consumer.canReceiveEnergy(path.endFace)) {
                    consumers.add(new ConsumerPath(consumer.getValue().consumer, path));
                }
            }

            consumerCache.put(source, consumers);
            return consumers;
        }
    }
    
    public EnergyFlowEntry getFlowEntry(int x, int y, int z) {
        return getFlowEntry(new Vec3i(x, y, z));
    }
    
    public EnergyFlowEntry getFlowEntry(Vec3i pos) {
        return energyFlow.get(pos);
    }

    public record ConsumerPath(EnergyConsumer consumer, NetworkPath path) {
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
