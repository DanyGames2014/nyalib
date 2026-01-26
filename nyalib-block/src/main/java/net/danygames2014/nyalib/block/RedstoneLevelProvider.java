package net.danygames2014.nyalib.block;

import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public interface RedstoneLevelProvider {
    int getSidePowerLevel(BlockView blockView, int x, int y, int z, int side);

    int getSideStrongPowerLevel(World world, int x, int y, int z, int side);
}
