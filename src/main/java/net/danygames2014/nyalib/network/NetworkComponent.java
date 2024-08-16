package net.danygames2014.nyalib.network;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;

public interface NetworkComponent {
    NetworkType getNetworkType();

    default ArrayList<NetworkType> getNetworkTypes() {
        ArrayList<NetworkType> types = new ArrayList<>();
        types.add(getNetworkType());
        return types;
    }

    default void update(int x, int y, int z, Network network, World world) {

    }

    default void onAddedToNet(int x, int y, int z, Network network, World world) {

    }

    default void onRemovedFromNet(int x, int y, int z, Network network, World world) {

    }

    default void writeNbt(int x, int y, int z, Network network, World world, NbtCompound nbt) {

    }

    default void readNbt(int x, int y, int z, Network network, World world, NbtCompound nbt) {

    }
}
