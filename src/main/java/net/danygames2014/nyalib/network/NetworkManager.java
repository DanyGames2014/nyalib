package net.danygames2014.nyalib.network;

import net.danygames2014.nyalib.NyaLib;
import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings({"UnusedReturnValue", "DuplicatedCode", "LoggingSimilarMessage", "CollectionAddAllCanBeReplacedWithConstructor"})
public class NetworkManager {
    /**
     * For each Dimension there is a hashmap which takes network type Identifier as a key
     */
    public static HashMap<Dimension, HashMap<Identifier, ArrayList<Network>>> NETWORKS = new HashMap<>();

    public static AtomicInteger NEXT_ID = new AtomicInteger(0);

    public static ArrayList<Network> removeQueue = new ArrayList<>();

    // Getting Networks
    public static ArrayList<Network> getNetworks(Dimension dimension, Identifier networkTypeIdentifier) {
        HashMap<Identifier, ArrayList<Network>> netDims = NETWORKS.get(dimension);

        if (netDims == null) {
            return new ArrayList<>();
        }

        ArrayList<Network> nets = netDims.get(networkTypeIdentifier);
        return nets != null ? nets : new ArrayList<>();
    }

    public static HashMap<Identifier, ArrayList<Network>> getNetworks(Dimension dimension) {
        return NETWORKS.get(dimension);
    }

    // Adding a network
    @SuppressWarnings("Java8MapApi")
    public static void addNetwork(Dimension dimension, Identifier networkTypeIdentifier, Network network) {
        HashMap<Identifier, ArrayList<Network>> dimNetworks = NETWORKS.get(dimension);

        if (dimNetworks == null) {
            dimNetworks = new HashMap<>();
            NETWORKS.put(dimension, dimNetworks);
        }

        ArrayList<Network> typeNetworks = dimNetworks.get(networkTypeIdentifier);
        if (typeNetworks == null) {
            typeNetworks = new ArrayList<>();
            dimNetworks.put(networkTypeIdentifier, typeNetworks);
        }

        typeNetworks.add(network);
    }

    public static Network createNetwork(Dimension dimension, Identifier networkTypeIdentifier) {
        Network network = new Network(dimension.world);
        network.id = NEXT_ID.getAndIncrement();
        addNetwork(dimension, networkTypeIdentifier, network);
        return network;
    }

    public static void removeNetwork(Network network) {
        if (!removeQueue.contains(network)) {
            removeQueue.add(network);
        }
    }

    public static void removeQueuedNetworks() {
        for (Network toremove : removeQueue) {
            removeNetworkInternal(toremove);
        }
    }

    private static boolean removeNetworkInternal(Network toRemove) {
        for (var dimensions : NETWORKS.entrySet()) {
            for (var networks : dimensions.getValue().entrySet()) {
                for (var network : networks.getValue()) {
                    if (network == toRemove) {
                        networks.getValue().remove(network);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // For Colorizing Temp
    public static Network getAt(int x, int y, int z, Dimension dimension, Identifier networkTypeIdentifier) {
        for (Network net : getNetworks(dimension, networkTypeIdentifier)) {
            if (net.isAt(x, y, z)) {
                return net;
            }
        }
        return null;
    }

    // Adding and Removing Blocks
    public static void addBlock(int x, int y, int z, World world, Block block, NetworkComponent component) {
        // For each of the network types this network component can handle, discover and add to networks
        for (Identifier typeIdentifier : component.getNetworkTypeIdentifiers()) {
            ArrayList<Network> neighborNets = new ArrayList<>(2);
            ArrayList<Network> potentialNets = new ArrayList<>();

            potentialNets.addAll(getNetworks(world.dimension, typeIdentifier));

//            System.out.println("FOUND " + potentialNets.size() + " POTENTIAL NETWORKS FOR " + typeIdentifier);

            for (Network potentialNet : potentialNets) {
                for (Direction direction : Direction.values()) {
                    if (potentialNet.isAt(x + direction.getOffsetX(), y + direction.getOffsetY(), z + direction.getOffsetZ())) {
                        neighborNets.add(potentialNet);
                    }
                }
            }

//            System.out.println("FOUND " + neighborNets.size() + " NEIGHBOR NETWORKS FOR " + typeIdentifier);

            Network network;

            switch (neighborNets.size()) {
                case 0 -> {
                    network = createNetwork(world.dimension, typeIdentifier);
                }

                case 1 -> {
                    network = neighborNets.get(0);
                }

                default -> {
                    network = neighborNets.get(0);
                    for (int i = 1; i < neighborNets.size(); i++) {

                        if (neighborNets.get(i).getId() == network.getId()) {
                            continue;
                        }

                        network.blocks.putAll(neighborNets.get(i).blocks);
                        neighborNets.get(i).blocks.clear();
                        removeNetwork(neighborNets.get(i));
                    }
                }
            }

            network.addBlock(x, y, z, BlockRegistry.INSTANCE.getId(block));
        }
    }

    public static void removeBlock(int x, int y, int z, World world, Block block, NetworkComponent component) {
        for (Identifier typeIdentifier : component.getNetworkTypeIdentifiers()) {
            Network net = getAt(x, y, z, world.dimension, typeIdentifier);

            if (net == null) {
                NyaLib.LOGGER.warn("Removed a block at [x={}, y={}, z={}] which was not in any network of type {}.", x, y, z, typeIdentifier.toString());
                continue;
            }

            ArrayList<Vec3i> neighborBlocks = new ArrayList<>();
            for (Direction direction : Direction.values()) {
                var neighborPos = new Vec3i(x + direction.getOffsetX(), y + direction.getOffsetY(), z + direction.getOffsetZ());
                if (net.isAt(neighborPos.x, neighborPos.y, neighborPos.z)) {
                    neighborBlocks.add(neighborPos);
                }
            }

            System.out.println("NET SIZE:" + net.blocks.size() + " | NEIGHBORS FOUND :" + neighborBlocks.size());
            switch (neighborBlocks.size()) {
                // There are no neighbors, which should mean that this is the last block in the network and the network can be removed
                case 0 -> {
                    NyaLib.LOGGER.debug("Last block in network, removing network with ID {}", net.getId());
                    net.removeBlock(x, y, z);

                    if (net.blocks.isEmpty()) {
                        removeNetwork(net);
                    } else {
                        NyaLib.LOGGER.warn("Removed a block from network {} with no neighbors but the network still has {} blocks. Network will not be deleted", net.getId(), net.blocks.size());
                    }

                }

                case 1 -> {
                    NyaLib.LOGGER.debug("Only one neighbor, its a stump and can be removed normally");
                    net.removeBlock(x, y, z);
                }

                default -> {
                    net.removeBlock(x, y, z);

                    // Walk thru all the sides
                    for (Direction dir : Direction.values()) {
                        Vec3i neighbor = new Vec3i(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ());
                        if (net.isAt(neighbor.x, neighbor.y, neighbor.z)) {
                            Set<Vec3i> discovered = net.walk(neighbor);
                            System.out.println("DISCOVERED " + discovered.size());
                        }
                    }
                }
            }

        }
    }

    // Load & Save
    public static void writeNbt(World world, NbtCompound nbt) {
        Dimension dim = world.dimension;
        Optional<Identifier> dimIdentifierO = DimensionRegistry.INSTANCE.getIdByLegacyId(dim.id);
        Identifier dimIdentifier;

        // Check if a dimension with this ID exists
        if (dimIdentifierO.isEmpty()) {
            NyaLib.LOGGER.error("Dimension {} not found", dim.id);
            return;
        }
        dimIdentifier = dimIdentifierO.get();

        // Get the compound containing all the compounds for each dimension
        if (!nbt.contains("dimensions")) {
            nbt.put("dimensions", new NbtCompound());
        }
        NbtCompound dimensionsNbt = nbt.getCompound("dimensions");

        // Get the compound of the network types in the specified dimension
        if (!dimensionsNbt.contains(dimIdentifier.toString())) {
            dimensionsNbt.put(dimIdentifier.toString(), new NbtCompound());
        }
        NbtCompound dimensionNbt = dimensionsNbt.getCompound(dimIdentifier.toString());

        // Get all the networks in this dimension and iterate over them
        NETWORKS.computeIfAbsent(dim, k -> new HashMap<>());
        for (Map.Entry<Identifier, ArrayList<Network>> networksOfType : getNetworks(dim).entrySet()) {
            Identifier type = networksOfType.getKey();
            var networks = networksOfType.getValue();

            // Get all the networks of the given type
            if (!dimensionNbt.contains(type.toString())) {
                dimensionNbt.put(type.toString(), new NbtCompound());
            }
            NbtCompound networksOfTypeNbt = dimensionNbt.getCompound(type.toString());

            // Write Each network
            for (Network network : networks) {
                networksOfTypeNbt.put(network.id + "", network.writeNbt());
            }

            // Empty Network Cleanup
            for (Object networkO : networksOfTypeNbt.values()) {
                if (networkO instanceof NbtCompound network) {
                    if (network.getList("blocks").size() == 0) {
                        networksOfTypeNbt.values().remove(networkO);
                    }
                }
            }
        }

        // Remove queued up networks on save
        removeQueuedNetworks();
    }

    public static void readNbt(World world, NbtCompound nbt) {
        Dimension dim = world.dimension;
        Optional<Identifier> dimIdentifierO = DimensionRegistry.INSTANCE.getIdByLegacyId(dim.id);
        Identifier dimIdentifier;

        // Check if a dimension with this ID exists
        if (dimIdentifierO.isEmpty()) {
            NyaLib.LOGGER.error("Dimension {} not found", dim.id);
            return;
        }
        dimIdentifier = dimIdentifierO.get();

        // Get the compound containing all the compounds for each dimension
        NbtCompound dimensionsNbt = nbt.getCompound("dimensions");

        // Get the compound if the dimension
        NbtCompound dimensionNbt = dimensionsNbt.getCompound(dimIdentifier.toString());

        // Load the Networks
        for (Object networksOfTypeO : dimensionNbt.values()) {
            if (networksOfTypeO instanceof NbtCompound networksOfType) {

                // Loading a network for a give type
                for (Object networksO : networksOfType.values()) {
                    if (networksO instanceof NbtCompound networks) {
                        addNetwork(
                                dim,
                                Identifier.of(networksOfType.getKey()),
                                Network.readNbt(networks, world)
                        );
                    }
                }
            }
        }

    }

}
