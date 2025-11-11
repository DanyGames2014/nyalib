package net.danygames2014.nyalib.world.structure;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;

import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings({"UnusedReturnValue", "BooleanMethodIsAlwaysInverted", "unused"})
public class Structure {
    public ArrayList<StructureBlockEntry> blocks;
    public Random random;

    public Structure(World world) {
        this.blocks = new ArrayList<>();
        this.random = world.random;
    }

    /*
        Add Block Methods
    */

    public boolean addBlock(StructureBlockEntry entry) {
        return this.blocks.add(entry);
    }

    public boolean addBlock(int xOffset, int yOffset, int zOffset, BlockState state, CollisionType collisionType) {
        return this.addBlock(new StructureBlockEntry(xOffset, yOffset, zOffset, state, collisionType));
    }

    public boolean addBlock(int xOffset, int yOffset, int zOffset, BlockState state) {
        return this.addBlock(new StructureBlockEntry(xOffset, yOffset, zOffset, state));
    }

    public boolean addBlock(int xOffset, int yOffset, int zOffset, Block block, CollisionType collisionType) {
        return this.addBlock(new StructureBlockEntry(xOffset, yOffset, zOffset, block, collisionType));
    }

    public boolean addBlock(int xOffset, int yOffset, int zOffset, Block block) {
        return this.addBlock(new StructureBlockEntry(xOffset, yOffset, zOffset, block));
    }

    /**
     * Checks if the structure can be generated according to its Collision Type rules
     *
     * @param world The world the structure is in
     * @param x     The X coordinate of the structure
     * @param y     The Y coordinate of the structure
     * @param z     The Z coordinate of the structure
     * @return True if the structure can be placed
     */
    public boolean checkCollision(World world, int x, int y, int z) {
        for (StructureBlockEntry block : this.blocks) {
            if (!isReplaceable(world, x + block.xOffset, y + block.yOffset, z + block.zOffset) && (block.collisionType == CollisionType.DONT_GENERATE)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Generate the structure
     *
     * @param world The world to generate it in
     * @param x     The X coordinate of the structure
     * @param y     The Y coordinate of the structure
     * @param z     The Z coordinate of the structure
     * @return Returns true if the structure was placed succesfully
     */
    public boolean generate(World world, int x, int y, int z) {
        return this.generate(world, x, y, z, Rotation.NONE);
    }

    /**
     * Generate the structure
     *
     * @param world The world to generate it in
     * @param x     The X coordinate of the structure
     * @param y     The Y coordinate of the structure
     * @param z     The Z coordinate of the structure
     * @return Returns true if the structure was placed succesfully
     */
    public boolean generate(World world, int x, int y, int z, Rotation rotation) {
        if (!checkCollision(world, x, y, z)) {
            return false;
        }

        for (StructureBlockEntry block : this.blocks) {
            placeBlock(world, x, y, z, block, rotation);
        }

        return true;
    }


    /**
     * @param world The world the to place the block in
     * @param x     The X coordinate of the structure
     * @param y     The Y coordinate of the structure
     * @param z     The Z coordinate of the structure
     * @param block The block to place
     * @return true if the block placement was succesfull
     */
    public boolean placeBlock(World world, int x, int y, int z, StructureBlockEntry block) {
        return this.placeBlock(world, x, y, z, block, Rotation.NONE);
    }

    /**
     * @param world The world the to place the block in
     * @param x     The X coordinate of the structure
     * @param y     The Y coordinate of the structure
     * @param z     The Z coordinate of the structure
     * @param block The block to place
     * @return true if the block placement was succesfull
     */
    public boolean placeBlock(World world, int x, int y, int z, StructureBlockEntry block, Rotation rotation) {
        if (rotation.swapXZ) {
            return placeState(
                    world,
                    x + (block.zOffset * rotation.getXMultiplier()),
                    y + block.yOffset,
                    z + (block.xOffset * rotation.getZMultiplier()),
                    getState(this, world, x, y, z, block),
                    block.collisionType
            );
        } else {
            return placeState(
                    world,
                    x + (block.xOffset * rotation.getXMultiplier()),
                    y + block.yOffset,
                    z + (block.zOffset * rotation.getZMultiplier()),
                    getState(this, world, x, y, z, block),
                    block.collisionType
            );
        }
    }
    
    public BlockState getState(Structure structure, World world, int x, int y, int z, StructureBlockEntry block){
        return block.getState(structure, world, x, y, z, block);
    }

    public boolean placeState(World world, int x, int y, int z, BlockState state, CollisionType collisionType) {
        if (isReplaceable(world, x, y, z)) {
            setState(world, x, y, z, state);
            return true;
        } else {
            if (collisionType == CollisionType.REPLACE_BLOCK) {
                setState(world, x, y, z, state);
                return true;
            } else {
                return false;
            }
        }
    }
    
    public void setState(World world, int x, int y, int z, BlockState state){
        world.setBlockState(x, y, z, state);
    }

    // Helper method to see if material is either air or replaceable
    public boolean isReplaceable(World world, int x, int y, int z) {
        return world.getBlockState(x, y, z).isAir() || world.getMaterial(x, y, z).isReplaceable() || world.getBlockState(x,y,z).isOf(Block.SAPLING);
    }
}
