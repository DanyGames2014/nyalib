package net.danygames2014.nyalib.network;

import net.danygames2014.nyalib.NyaLib;
import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
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
    public static void addNetwork(Dimension dimension, Network network) {
        HashMap<Identifier, ArrayList<Network>> dimNetworks = NETWORKS.get(dimension);

        if (network == null) {
            return;
        }

        if (dimNetworks == null) {
            dimNetworks = new HashMap<>();
            NETWORKS.put(dimension, dimNetworks);
        }

        ArrayList<Network> typeNetworks = dimNetworks.get(network.type.getIdentifier());
        if (typeNetworks == null) {
            typeNetworks = new ArrayList<>();
            dimNetworks.put(network.type.getIdentifier(), typeNetworks);
        }

        typeNetworks.add(network);
    }

    public static Network createNetwork(Dimension dimension, NetworkType networkType) {
        Network network;

        try {
            network = networkType.getNetworkClass().getDeclaredConstructor(World.class, NetworkType.class).newInstance(dimension.world, networkType);
        } catch (Exception e) {
            NyaLib.LOGGER.error("Error when creating a network of type {}", networkType.getIdentifier(), e);
            return null;
        }

        network.id = NEXT_ID.getAndIncrement();
        addNetwork(dimension, network);
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

    /**
     * Get a network of the given type at the given coords
     *
     * @param dimension             The dimension to check in
     * @param x                     x-position to check
     * @param y                     y-position to check
     * @param z                     z-position to checj
     * @param networkTypeIdentifier The type of network to check for
     * @return A network if it exists on the coordinates. null if there is no network on the given coordinates.
     */
    public static Network getAt(Dimension dimension, int x, int y, int z, Identifier networkTypeIdentifier) {
        for (Network net : getNetworks(dimension, networkTypeIdentifier)) {
            if (net.isAt(x, y, z)) {
                return net;
            }
        }
        return null;
    }

    /**
     * Returns an ArrayList of networks of any type neighboring this block
     *
     * @param world The world to check in
     * @param x     x-position to check
     * @param y     y-position to check
     * @param z     z-position to checj
     * @return An ArrayList of networks neighboring this block
     */
    public static ArrayList<Network> getNeighbors(World world, int x, int y, int z) {
        ArrayList<Network> neighborNets = new ArrayList<>();

        for (var networksOfType : getNetworks(world.dimension).values()) {
            for (var network : networksOfType) {
                for (Direction direction : Direction.values()) {
                    if (network.isAt(x + direction.getOffsetX(), y + direction.getOffsetY(), z + direction.getOffsetZ())) {
                        neighborNets.add(network);
                    }
                }
            }
        }

        return neighborNets;
    }

    /**
     * Returns an ArrayList of networks of a given type neighboring this block
     *
     * @param world                 The world to check in
     * @param x                     x-position to check
     * @param y                     y-position to check
     * @param z                     z-position to checj
     * @param networkTypeIdentifier The type of network to check for
     * @return An ArrayList of networks neighboring this block
     */
    public static ArrayList<Network> getNeighbors(World world, int x, int y, int z, Identifier networkTypeIdentifier) {
        ArrayList<Network> neighborNets = new ArrayList<>();

        for (Network network : getNetworks(world.dimension, networkTypeIdentifier)) {
            for (Direction direction : Direction.values()) {
                if (network.isAt(x + direction.getOffsetX(), y + direction.getOffsetY(), z + direction.getOffsetZ())) {
                    neighborNets.add(network);
                }
            }
        }

        return neighborNets;
    }

    // Adding and Removing Blocks
    @SuppressWarnings("RedundantLabeledSwitchRuleCodeBlock")
    public static <T extends Block & NetworkComponent> void addBlock(World world, int x, int y, int z, T component) {
        if (component == null) {
            return;
        }

        // For each of the network types this network component can handle, discover and add to networks
        for (NetworkType networkType : component.getNetworkTypes()) {
            ArrayList<Network> neighborNets = new ArrayList<>(2);
            ArrayList<Network> potentialNets = new ArrayList<>();

            potentialNets.addAll(getNetworks(world.dimension, networkType.getIdentifier()));

            // Check all the potential networks to connect to
            for (Network potentialNet : potentialNets) {
                // Loop thru all sides
                for (Direction direction : Direction.values()) {
                    // Check if the network exists on this side
                    if (potentialNet.isAt(x + direction.getOffsetX(), y + direction.getOffsetY(), z + direction.getOffsetZ())) {
                        // If it exists, check if the found component of the potential net isnt an edge and this component isnt an edge
                        if (!((potentialNet.getAt(x + direction.getOffsetX(), y + direction.getOffsetY(), z + direction.getOffsetZ()).block() instanceof NetworkEdgeComponent) && component instanceof NetworkEdgeComponent)) {
                            // Check if this component can connect to the other one in the potential network
                            if (component.canConnectTo(world, x, y, z, potentialNet, direction)) {
                                neighborNets.add(potentialNet);
                            }
                        }
                    }
                }
            }

            Network network;

            // Check how many neighbor networks have been found
            switch (neighborNets.size()) {
                // No networks have been found -> Create a new one
                case 0 -> {
                    network = createNetwork(world.dimension, networkType);
                }

                // One networks has been found -> Add to that network
                case 1 -> {
                    network = neighborNets.get(0);
                }

                // Two or more networks have been found, merge them
                default -> {
                    network = neighborNets.get(0);
                    for (int i = 1; i < neighborNets.size(); i++) {

                        if (neighborNets.get(i).getId() == network.getId()) {
                            continue;
                        }

                        network.components.putAll(neighborNets.get(i).components);
                        neighborNets.get(i).components.clear();
                        removeNetwork(neighborNets.get(i));
                    }
                }
            }

            // Add to the network
            if (network != null) {
                network.addBlock(x, y, z, component);
                network.update();
            }
        }
    }

    public static <T extends Block & NetworkComponent> void removeBlock(World world, int x, int y, int z, T component) {
        for (NetworkType networkType : component.getNetworkTypes()) {
            Network net = getAt(world.dimension, x, y, z, networkType.getIdentifier());

            if (net == null) {
                NyaLib.LOGGER.warn("Removed a block at [x={}, y={}, z={}] which was not in any network of type {}.", x, y, z, networkType.toString());
                continue;
            }

            ArrayList<Vec3i> neighborBlocks = new ArrayList<>();
            for (Direction direction : Direction.values()) {
                var neighborPos = new Vec3i(x + direction.getOffsetX(), y + direction.getOffsetY(), z + direction.getOffsetZ());
                if (net.isAt(neighborPos.x, neighborPos.y, neighborPos.z)) {
                    neighborBlocks.add(neighborPos);
                }
            }

            NyaLib.LOGGER.debug("NET SIZE: {} | NEIGHBORS FOUND : {}", net.components.size(), neighborBlocks.size());

            switch (neighborBlocks.size()) {
                // There are no neighbors, which should mean that this is the last block in the network and the network can be removed
                case 0 -> {
                    NyaLib.LOGGER.debug("Last block in network, removing network with ID {}", net.getId());
                    net.removeBlock(x, y, z);

                    if (net.components.isEmpty()) {
                        removeNetwork(net);
                    } else {
                        NyaLib.LOGGER.warn("Removed a block from network {} with no neighbors but the network still has {} blocks. Network will not be deleted", net.getId(), net.components.size());
                    }

                }

                case 1 -> {
                    NyaLib.LOGGER.debug("Only one neighbor, its a stump and can be removed normally");
                    net.removeBlock(x, y, z);
                }

                default -> {
                    net.removeBlock(x, y, z);

                    ArrayList<HashSet<Vec3i>> potentialNetworks = new ArrayList<>(6);
                    // Walk thru all the sides
                    for (Direction dir : Direction.values()) {
                        Vec3i neighbor = new Vec3i(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ());

                        // If the network reaches this neighbor side, walk thru all the blocks
                        if (net.isAt(neighbor.x, neighbor.y, neighbor.z)) {
                            HashSet<Vec3i> discovered = net.walk(neighbor);

                            NyaLib.LOGGER.debug("Discovered a potential network of {} blocks", discovered.size());

                            boolean exists = false;

                            // Check if the first block of this potential networks exists in the other potential networks
                            // We dont have to check every block because if theyre connected
                            // somewhere they *should* have access to the same block
                            for (var potentialNet : potentialNetworks) {
                                if (potentialNet.contains(discovered.iterator().next())) {
                                    exists = true;
                                }
                            }

                            // If it doesnt exist, we can safely assume this is an independed new network
                            if (!exists) {
                                potentialNetworks.add(discovered);
                            }
                        }
                    }

                    NyaLib.LOGGER.debug("There will be {} new networks", potentialNetworks.size());

                    switch (potentialNetworks.size()) {
                        // This shouldnt happen
                        case 0 -> {
                            NyaLib.LOGGER.warn("There were 0 potential networks when splitting, this shouldn't happen");
                        }

                        case 1 -> {
                            // This is fine
                        }

                        // 2 or more networks
                        // The first potential network will be kept in the existing one while others will be transferred to a new one
                        // `net` is the first network here
                        default -> {
                            // Iterate over the new potential networks
                            for (int i = 1; i < potentialNetworks.size(); i++) {

                                Network newNetwork = createNetwork(world.dimension, networkType);

                                // Iterate over every block in this new potential network
                                for (Vec3i pos : potentialNetworks.get(i)) {
                                    net.removeBlock(pos.x, pos.y, pos.z);
                                    newNetwork.addBlock(
                                            pos.x,
                                            pos.y,
                                            pos.z,
                                            world.getBlockState(pos.x, pos.y, pos.z).getBlock()
                                    );
                                }

                                // Update the new network
                                newNetwork.update();
                            }

                            // Update the existing network
                            net.update();
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
            dimensionNbt.put(type.toString(), new NbtCompound());
            NbtCompound networksOfTypeNbt = dimensionNbt.getCompound(type.toString());

            // Write Each network
            for (Network network : networks) {
                networksOfTypeNbt.put(network.id + "", network.toNbt());
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
                                Network.fromNbt(networks, world, Identifier.of(networksOfType.getKey()))
                        );
                    }
                }
            }
        }

    }

}
