package net.danygames2014.nyalib.network;

import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A base interface representing every NetworkComponent
 */
@SuppressWarnings("unused")
public interface NetworkComponent {
    /**
     * This is a helper method if you only need one network.
     *
     * @return A network type this component can participate in
     */
    NetworkType getNetworkType();

    /**
     * @return A list of network types this component can participate in
     */
    default ArrayList<NetworkType> getNetworkTypes() {
        ArrayList<NetworkType> types = new ArrayList<>();
        types.add(getNetworkType());
        return types;
    }

    /**
     * Retrieves the {@link NetworkComponentEntry} of a network component
     *
     * @param world       The world this component is in
     * @param x           The x-position of this component
     * @param y           The y-position of this component
     * @param z           The z-position of this component
     * @param networkType The type of network you want to retrieve the entry from
     * @return The retrieved {@link NetworkComponentEntry}
     */
    default NetworkComponentEntry getEntry(World world, int x, int y, int z, NetworkType networkType) {
        Network net = NetworkManager.getAt(world.dimension, x, y, z, networkType.identifier);

        if (net != null) {
            return net.getEntry(x, y, z);
        }

        return null;
    }

    /**
     * Retrieves all of the {@link NetworkComponentEntry} of a component from all of its networks
     *
     * @param world The world this component is in
     * @param x     The x-position of this component
     * @param y     The y-position of this component
     * @param z     The z-position of this component
     * @return The retrieved {@link NetworkComponentEntry}
     */
    default HashMap<Network, NetworkComponentEntry> getEntries(World world, int x, int y, int z) {
        var validNetworkTypes = getNetworkTypes();
        HashMap<Network, NetworkComponentEntry> entries = new HashMap<>();

        // Loop thru all of the networks of all types
        for (var networkTypes : NetworkManager.getNetworks(world.dimension).entrySet()) {
            // Check if the network has the type that this component can participate in
            if (validNetworkTypes.contains(NetworkTypeRegistry.get(networkTypes.getKey()))) {
                // Loop thru all the networks of this types
                for (var network : networkTypes.getValue()) {
                    // Check if the network is at the position
                    if (network.isAt(x, y, z)) {
                        entries.put(network, network.getEntry(x, y, z));
                    }
                }
            }
        }

        return entries;
    }

    /**
     * Allows the component to conditionally not connect to other components
     *
     * @param world     The world this component is in
     * @param x         The x-position of this component
     * @param y         The y-position of this component
     * @param z         The z-position of this component
     * @param network   The network the potential neigbor is in. Can be null if this is called from a network component that is being placed
     * @param direction The direction in which the potential neighbor is
     * @return Whether this component should connect to the potential neighbor
     */
    default boolean canConnectTo(World world, int x, int y, int z, @Nullable Network network, Direction direction) {
        return true;
    }

    /**
     * Gets the path finding cost of this component
     *
     * @param world   The world this component is in
     * @param x       The x-position of this component
     * @param y       The y-position of this component
     * @param z       The z-position of this component
     * @param network The network the component is in
     * @return The pathing cost of this component
     */
    default int getPathingCost(World world, int x, int y, int z, @Nullable Network network) {
        return 1;
    }

    /**
     * Called when the physical topology of the network updates
     *
     * @param world   The world this network is in
     * @param x       The x-position of this component
     * @param y       The y-position of this component
     * @param z       The z-position of this component
     * @param network The network this component is in
     */
    default void update(World world, int x, int y, int z, Network network) {

    }

    /**
     * Called when this component is added to the network or the network is loaded
     *
     * @param world   The world this network is in
     * @param x       The x-position of this component
     * @param y       The y-position of this component
     * @param z       The z-position of this component
     * @param network The network this component is in
     */
    default void onAddedToNet(World world, int x, int y, int z, Network network) {

    }

    /**
     * Called when this component is removed from the network
     * (it's called right before it is removed from the network)
     *
     * @param world   The world this network is in
     * @param x       The x-position of this component
     * @param y       The y-position of this component
     * @param z       The z-position of this component
     * @param network The network this component is in
     */
    default void onRemovedFromNet(World world, int x, int y, int z, Network network) {

    }

    /**
     * Called when this component is automatically added to network
     *
     * @param world     The world this component is in
     * @param x         The x-position of this component
     * @param y         The y-position of this component
     * @param z         The z-position of this component
     * @param component Reference to this component
     * @param <T>       {@link Block} implementing a {@link NetworkComponent}
     */
    default <T extends Block & NetworkComponent> void addToNet(World world, int x, int y, int z, T component) {
        if (!world.isRemote) {
            NetworkManager.addBlock(world, x, y, z, component);
        }
    }

    /**
     * Called when this component is automatically removed from network
     *
     * @param world     The world this component is in
     * @param x         The x-position of this component
     * @param y         The y-position of this component
     * @param z         The z-position of this component
     * @param component Reference to this component
     * @param <T>       {@link Block} implementing a {@link NetworkComponent}
     */
    default <T extends Block & NetworkComponent> void removeFromNet(World world, int x, int y, int z, T component) {
        if (!world.isRemote) {
            NetworkManager.removeBlock(world, x, y, z, component);
        }
    }

    /**
     * Called when this network is being saved
     *
     * @param world   The world this network is in
     * @param x       The x-position of this component
     * @param y       The y-position of this component
     * @param z       The z-position of this component
     * @param network The network this component is in
     * @param nbt     The NBT Compound being saved
     */
    default void writeNbt(World world, int x, int y, int z, Network network, NbtCompound nbt) {

    }

    /**
     * Called when this network is being loaded
     *
     * @param world   The world this network is in
     * @param x       The x-position of this component
     * @param y       The y-position of this component
     * @param z       The z-position of this component
     * @param network The network this component is in
     * @param nbt     The NBT Compound being read
     */
    default void readNbt(World world, int x, int y, int z, Network network, NbtCompound nbt) {

    }
}
