package net.danygames2014.nyalib.network;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;

public interface NetworkComponent {
    Identifier getNetworkTypeIdentifier();

    default ArrayList<Identifier> getNetworkTypeIdentifiers() {
        ArrayList<Identifier> identifiers = new ArrayList<>();
        identifiers.add(getNetworkTypeIdentifier());
        return identifiers;
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
