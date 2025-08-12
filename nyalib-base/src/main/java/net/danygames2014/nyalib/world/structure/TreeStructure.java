package net.danygames2014.nyalib.world.structure;

import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;

public class TreeStructure extends Structure {
    public BlockState trunkBlockState;
    public CollisionType trunkCollisionType;

    public TreeStructure(World world, BlockState trunkBlockState, CollisionType trunkCollisionType) {
        super(world);
        this.trunkBlockState = trunkBlockState;
        this.trunkCollisionType = trunkCollisionType;
    }

    public boolean checkCollision(World world, int x, int y, int z, int trunkHeight) {
        for (int i = 0; i < trunkHeight; i++) {
            if (!isReplaceable(world, x, y + i, z) && (trunkCollisionType == CollisionType.DONT_GENERATE)) {
                return false;
            }
        }

        return super.checkCollision(world, x, y + trunkHeight, z);
    }

    public boolean generate(World world, int x, int y, int z, int trunkHeight) {
        if (!checkCollision(world, x, y, z, trunkHeight)) {
            return false;
        }

        for (int i = 0; i < trunkHeight; i++) {
            this.placeState(world, x, y+i, z, trunkBlockState, trunkCollisionType);
        }

        for (StructureBlockEntry block : this.blocks) {
            placeBlock(world, x, y+trunkHeight, z, block, Rotation.NONE);
        }

        return true;
    }
}
