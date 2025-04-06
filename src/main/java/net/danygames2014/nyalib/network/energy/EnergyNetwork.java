package net.danygames2014.nyalib.network.energy;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.energy.EnergyConsumer;
import net.danygames2014.nyalib.energy.EnergySource;
import net.danygames2014.nyalib.network.*;
import net.danygames2014.nyalib.particle.ParticleHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EnergyNetwork extends Network {
    public HashMap<Vec3i, ConsumerEntry> consumers;

    public HashMap<Vec3i, ArrayList<ConsumerPath>> consumerCache;

    public EnergyNetwork(World world, NetworkType type) {
        super(world, type);
        consumers = new HashMap<>();
        consumerCache = new HashMap<>();
    }

    @Override
    public void update() {
        consumers.clear();
        consumerCache.clear();

        for (NetworkComponentEntry componentEntry : components.values()) {
            if (componentEntry.component() instanceof NetworkEdgeComponent) {
                if (world.getBlockEntity(componentEntry.pos().x, componentEntry.pos().y, componentEntry.pos().z) instanceof EnergyConsumer consumer) {
                    consumers.put(componentEntry.pos(), new ConsumerEntry(componentEntry, consumer));
                }
            }
        }

        super.update();
    }

    //long time = System.nanoTime();
    //System.out.println(((System.nanoTime() - time) / 1000) + "uS");

    /**
     * Provide energy to the energy net
     *
     * @param source    The source of energy
     * @param sourcePos The position of the source
     * @param voltage   The voltage provided
     * @param power     The amperage provided
     * @return The power used
     */
    public int provideEnergy(EnergySource source, Vec3i sourcePos, int voltage, int power) {
        int remainingPower = power;

        for (ConsumerPath consumerPath : getValidConsumers(sourcePos)) {
            EnergyConsumer consumer = consumerPath.consumer;
            NetworkPath path = consumerPath.path;

            // Check if the consumer can even accept more energy
            if (consumer.getRemainingCapacity() > 0) {
                // Insert Energy into the consumers
                int usedPower = traverseEnergy(consumer, path.endFace, path, voltage, remainingPower);

                // Reduce the remaining amount
                remainingPower -= usedPower;

                // If there are 0 amps remaining, end it
                if (remainingPower <= 0) {
                    return power;
                }
            }

        }

        return power - remainingPower;
    }

    private int traverseEnergy(EnergyConsumer consumer, Direction consumerFace, NetworkPath path, int voltage, int remainingPower) {
        for (Vec3i node : path.path) {
            ParticleHelper.addParticle(world, "flame", node.x + 0.5D, node.y + 1, node.z + 0.5D, 0, 0.1D, 0);
        }

        return consumer.receiveEnergy(consumerFace, voltage, remainingPower);
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

    public record ConsumerPath(EnergyConsumer consumer, NetworkPath path) {
    }

    public record ConsumerEntry(NetworkComponentEntry componentEntry, EnergyConsumer consumer) {
    }
}
