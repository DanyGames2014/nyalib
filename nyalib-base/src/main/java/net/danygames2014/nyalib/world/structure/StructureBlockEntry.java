package net.danygames2014.nyalib.world.structure;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;

public class StructureBlockEntry {
    public byte xOffset;
    public byte yOffset;
    public byte zOffset;
    public BlockState state;
    public CollisionType collisionType;

    // Default Constructor
    public StructureBlockEntry(int xOffset, int yOffset, int zOffset, BlockState state, CollisionType collisionType) {
        this.xOffset = (byte) xOffset;
        this.yOffset = (byte) yOffset;
        this.zOffset = (byte) zOffset;
        this.state = state;
        this.collisionType = collisionType;
    }

    // Default Constructor with Default Collision Type
    public StructureBlockEntry(int xOffset, int yOffset, int zOffset, BlockState state) {
        this(xOffset, yOffset, zOffset, state, CollisionType.DONT_PLACE);
    }

    // Block Constructor
    public StructureBlockEntry(int xOffset, int yOffset, int zOffset, Block block, CollisionType collisionType) {
        this(xOffset, yOffset, zOffset, block.getDefaultState(), collisionType);
    }

    // Block Constructor with Defautl Collision Type
    public StructureBlockEntry(int xOffset, int yOffset, int zOffset, Block block) {
        this(xOffset, yOffset, zOffset, block.getDefaultState(), CollisionType.DONT_PLACE);
    }

    public BlockState getState(Structure structure, World world, int x, int y, int z, StructureBlockEntry block){
        return state;
    }
}
