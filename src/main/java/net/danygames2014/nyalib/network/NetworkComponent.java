package net.danygames2014.nyalib.network;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import java.util.ArrayList;

@SuppressWarnings("unused")
public interface NetworkComponent {
    /**
     * This is a helper method if you only need one network.
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
     * Called when the physical topology of the network updates
     * @param x The x-position of this component
     * @param y The y-position of this component
     * @param z The z-position of this component
     * @param network The network this component is in
     * @param world The world this network is in
     */
    default void update(int x, int y, int z, Network network, World world) {

    }

    /**
     * Called when this component is added to the network
     * @param x The x-position of this component
     * @param y The y-position of this component
     * @param z The z-position of this component
     * @param network The network this component is in
     * @param world The world this network is in
     */
    default void onAddedToNet(int x, int y, int z, Network network, World world) {

    }

    /**
     * Called when this component is removed from the network
     * (it's called right before it is removed from the network)
     * @param x The x-position of this component
     * @param y The y-position of this component
     * @param z The z-position of this component
     * @param network The network this component is in
     * @param world The world this network is in
     */
    default void onRemovedFromNet(int x, int y, int z, Network network, World world) {

    }

    /**
     * Called when this network is being saved
     * @param x The x-position of this component
     * @param y The y-position of this component
     * @param z The z-position of this component
     * @param network The network this component is in
     * @param world The world this network is in
     * @param nbt The NBT Compound being saved
     */
    default void writeNbt(int x, int y, int z, Network network, World world, NbtCompound nbt) {

    }

    /**
     * Called when this network is being loaded
     * @param x The x-position of this component
     * @param y The y-position of this component
     * @param z The z-position of this component
     * @param network The network this component is in
     * @param world The world this network is in
     * @param nbt The NBT Compound being read
     */
    default void readNbt(int x, int y, int z, Network network, World world, NbtCompound nbt) {

    }
}
