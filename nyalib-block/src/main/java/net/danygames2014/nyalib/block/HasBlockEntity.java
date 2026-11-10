package net.danygames2014.nyalib.block;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;

public interface HasBlockEntity {
    BlockEntity createBlockEntity(World world, int x, int y, int z);
}
