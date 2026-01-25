package net.danygames2014.nyalib.block.voxelshape;

import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public interface HasCollisionVoxelShape {
    @Nullable
    VoxelShape getCollisionVoxelShape(World world, int x, int y, int z);
}
