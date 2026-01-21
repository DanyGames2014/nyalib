package net.danygames2014.nyalib.network;

import net.minecraft.world.World;

public interface NetworkFactory {
    Network create(World world, NetworkType type);
}
