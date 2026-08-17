package net.danygames2014.nyalib.impl;

import net.danygames2014.nyalib.block.RedstoneLevelProvider;
import net.danygames2014.nyalib.mixininterface.RedstoneLevelWorld;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class RedstoneLevelWorldImpl implements RedstoneLevelWorld{
    private final World world;

    public RedstoneLevelWorldImpl(World world) {
        this.world = world;
    }

    @Override
    public int getStrongPowerLevelOnSide(int x, int y, int z, int side) {
        int blockId = world.getBlockId(x, y, z);

        if (blockId == 0) {
            return 0;
        }

        Block block = Block.BLOCKS[blockId];

        if (block instanceof RedstoneLevelProvider redstoneLevelProvider) {
            return redstoneLevelProvider.getSideStrongPowerLevel(world, x, y, z, side);
        } else {
            return block.isStrongPoweringSide(world, x, y, z, side) ? 15 : 0;
        }
    }

    @Override
    public int getStrongPowerLevel(int x, int y, int z) {
        int highestLevel = 0;
        int level;

        level = this.getStrongPowerLevelOnSide(x, y - 1, z, 0);
        if (level > highestLevel) {
            highestLevel = level;
        }

        level = this.getStrongPowerLevelOnSide(x, y + 1, z, 1);
        if (level > highestLevel) {
            highestLevel = level;
        }

        level = this.getStrongPowerLevelOnSide(x, y, z - 1, 2);
        if (level > highestLevel) {
            highestLevel = level;
        }

        level = this.getStrongPowerLevelOnSide(x, y, z + 1, 3);
        if (level > highestLevel) {
            highestLevel = level;
        }

        level = this.getStrongPowerLevelOnSide(x - 1, y, z, 4);
        if (level > highestLevel) {
            highestLevel = level;
        }

        level = this.getStrongPowerLevelOnSide(x + 1, y, z, 5);
        if (level > highestLevel) {
            highestLevel = level;
        }

        return highestLevel;
    }

    @Override
    public int getPowerLevelOnSide(int x, int y, int z, int side) {
        int powerLevel = 0;

        if (world.shouldSuffocate(x, y, z)) {
            powerLevel = this.getStrongPowerLevel(x, y, z);
        }

        int blockId = world.getBlockId(x, y, z);

        if (blockId == 0) {
            return 0;
        }

        Block block = Block.BLOCKS[blockId];

        if (block instanceof RedstoneLevelProvider redstoneLevelProvider) {
            int level = redstoneLevelProvider.getSidePowerLevel(world, x, y, z, side);
            if (level > powerLevel) {
                powerLevel = level;
            }
        } else {
            int level = block.isPoweringSide(world, x, y, z, side) ? 15 : 0;
            if (level > powerLevel) {
                powerLevel = level;
            }
        }

        return powerLevel;
    }

    @Override
    public int getPowerLevel(int x, int y, int z) {
        int highestLevel = 0;
        int level;

        level = this.getPowerLevelOnSide(x, y - 1, z, 0);
        if (level > highestLevel) {
            highestLevel = level;
        }

        level = this.getPowerLevelOnSide(x, y + 1, z, 1);
        if (level > highestLevel) {
            highestLevel = level;
        }

        level = this.getPowerLevelOnSide(x, y, z - 1, 2);
        if (level > highestLevel) {
            highestLevel = level;
        }

        level = this.getPowerLevelOnSide(x, y, z + 1, 3);
        if (level > highestLevel) {
            highestLevel = level;
        }

        level = this.getPowerLevelOnSide(x - 1, y, z, 4);
        if (level > highestLevel) {
            highestLevel = level;
        }

        level = this.getPowerLevelOnSide(x + 1, y, z, 5);
        if (level > highestLevel) {
            highestLevel = level;
        }

        return highestLevel;
    }
}
