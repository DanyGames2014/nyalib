package net.danygames2014.nyalib.util;

import net.danygames2014.nyalib.multipart.*;
import net.minecraft.block.Block;
import net.minecraft.block.ButtonBlock;
import net.minecraft.block.LeverBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Facings;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.Direction;

public class MultipartRedstoneUtil {

    public static int getPowerTo(MultipartComponent component, Direction side) {
        int redstoneMask = component.state.getRedstoneMask(side);
        return getPowerTo(component.world, component.x, component.y, component.z, side, redstoneMask & getMultipartComponentConnectionMask(component, side));
    }

    public static int getPowerTo(World world, int x, int y, int z, Direction side, int mask) {
        BlockPos pos = new BlockPos(x, y, z).offset(side);
        return getPower(world, pos.getX(), pos.getY(), pos.getZ(), side.getOpposite(), mask);
    }

    public static int getPower(World world, int x, int y, int z, Direction side, int mask) {
        if(world == null) return 0;

        MultipartState state = world.getMultipartState(x, y, z);

        if(state != null) {
            return state.powerLevel(side, mask);
        }

        int vanillaMask = vanillaConnectionMask(world, x, y, z, side, true);
        if ((vanillaMask & mask) > 0) {
            return world.getPowerLevelOnSide(x, y, z, side.getId());
        }

        return 0;
    }

    public static int getMultipartComponentConnectionMask(MultipartComponent component, Direction side) {
        if(component instanceof MultipartRedstone redstone && redstone.canConnectRedstone(side)) {
            if(component instanceof FaceMultipartRedstone face) {
                if (side.getAxis() == face.getFace().getAxis()) {
                    return 0x10;
                }

                return 1 << MultipartDirectionUtil.rotateTo(side.getAxis(), face.getFace());
            }
            if(component instanceof MaskedMultipartRedstone maskedRedstone) {
                return maskedRedstone.getConnectionMask(side);
            }
            return 0x1F;
        }
        return 0;
    }

    public static int getOtherBlockConnectionMask(World world, int x, int y, int z, Direction side, boolean power) {
        BlockPos pos = new BlockPos(x, y, z).offset(side);
        return getBlockConnectionMask(world, pos.getX(), pos.getY(), pos.getZ(), Direction.byId(side.getId() ^ 1), power);
    }

    public static int getBlockConnectionMask(World world, int x, int y, int z, Direction side, boolean power) {
        MultipartState state = world.getMultipartState(x, y, z);
        if(state != null) {
            return state.getConnectionMask(side);
        }
        return vanillaConnectionMask(world, x, y, z, side, power);
    }

    public static int vanillaConnectionMask(World world, int x, int y, int z, Direction side, boolean power) {

        BlockState state = world.getBlockState(x, y, z);
        Block block = state.getBlock();
        int meta = world.getBlockMeta(x, y, z);


        if(block == Block.REDSTONE_WIRE) {
            if(side == Direction.UP) return 0;
            return power ? 0x1F : 4;
        }

        if(block == Block.REPEATER) {
            Direction facing = Direction.byId(Facings.TO_DIR[meta & 3]);

            if(facing.getAxis() == side.getAxis()) {
                return power ? 0x1F : 4;
            }

            return 0;
        }

        boolean isTorch = block == Block.REDSTONE_TORCH || block == Block.LIT_REDSTONE_TORCH;
        if (isTorch) {
            if (power) return 0x1F;

            Direction attachment = MultipartDirectionUtil.getAttachmentDirectionFromWallMountedBlockMeta(meta);

            if(attachment == Direction.DOWN) {
                if (side == Direction.DOWN || side == Direction.UP) {
                    return 0x10;
                }

                return 4;
            }

            Direction facing = attachment.getOpposite();

            if (side.getAxis() == facing.getAxis()) {
                return 0x10;
            }

            return 1 << MultipartDirectionUtil.rotateTo(side.getAxis(), facing);
        }

        if (block instanceof ButtonBlock || block instanceof LeverBlock) {
            if (power) return 0x1F;

            Direction facing = MultipartDirectionUtil.getAttachmentDirectionFromWallMountedBlockMeta(meta & 7).getOpposite();
            if(facing.getAxis() == side.getAxis()) {
                return 0x10;
            }

            return  1 << MultipartDirectionUtil.rotateTo(side.getAxis(), facing);

        }

        if(power || block.canEmitRedstonePower()) {
            return 0x1F;
        }

        return 0;
    }
}
