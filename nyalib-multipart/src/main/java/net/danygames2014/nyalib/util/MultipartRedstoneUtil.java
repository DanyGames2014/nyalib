package net.danygames2014.nyalib.util;

import net.danygames2014.nyalib.multipart.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.math.Direction;

public class MultipartRedstoneUtil {

    public static int getPowerTo(MultipartComponent component, Direction side) {
        int redstoneMask = component.state.getRedstoneMask(side);
        return getPowerTo(component.world, component.x, component.y, component.z, side, redstoneMask & getMultipartComponentConnectionMask(component, side));
    }

    public static int getPowerTo(World world, int x, int y, int z, Direction side, int mask) {
        BlockPos pos = new BlockPos(x, y, z).add(side.getVector());
        return getPower(world, pos.getX(), pos.getY(), pos.getZ(), Direction.byId(side.getId() ^ 1), mask);
    }

    public static int getPower(World world, int x, int y, int z, Direction side, int mask) {
        if(world == null) return 0;

        MultipartState state = world.getMultipartState(x, y, z);

        if(state != null) {
            return state.powerLevel(side, mask);
        }

        int vanillaMask = vanillaConnectionMask(world, x, y, z, side, true);
        if ((vanillaMask & mask) > 0) {
            return world.getStrongPowerLevelOnSide(x, y, z, side.getId());
        }

        return 0;
    }

    public static int getMultipartComponentConnectionMask(MultipartComponent component, Direction side) {
        if(component instanceof MultipartRedstone redstone && redstone.canConnectRedstone(side)) {
            if(component instanceof FaceMultipartRedstone face) {
                if ((side.getId() & 6) == (face.getFace().getId() & 6)) {
                    return 0x10;
                }

                Direction sideDir = Direction.byId(side.getId() & 6);
                return 1 << MultipartDirectionUtil.rotateTo(sideDir, face.getFace());
            }
            if(component instanceof MaskedMultipartRedstone maskedRedstone) {
                return maskedRedstone.getConnectionMask(side);
            }
            return 0x1F;
        }
        return 0;
    }

    public static int getOtherBlockConnectionMask(World world, int x, int y, int z, Direction side, boolean power) {
        BlockPos pos = new BlockPos(x, y, z).add(side.getVector());
        return getBlockConnectionMask(world, pos.getX(), pos.getY(), pos.getZ(), Direction.byId(side.getId() ^ 1), power);
    }

    public static int getBlockConnectionMask(World world, int x, int y, int z, Direction side, boolean power) {
        MultipartState state = world.getMultipartState(x, y, z);
        if(state != null) {
            return state.getConnectionMask(side);
        }
        return vanillaConnectionMask(world, x, y, z, side, power);
    }

    // TODO: confirm this works;
    public static int vanillaConnectionMask(World world, int x, int y, int z, Direction side, boolean power) {
        int powerLevel;
        if (power) {
            powerLevel = world.getStrongPowerLevelOnSide(x, y, z, side.getId());
        } else {
            powerLevel = world.getPowerLevelOnSide(x, y, z, side.getId());
        }

        return powerLevel > 0 ? 0x1F : 0;
    }
}
