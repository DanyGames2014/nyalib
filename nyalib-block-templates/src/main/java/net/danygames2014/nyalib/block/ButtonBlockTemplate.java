package net.danygames2014.nyalib.block;

import net.danygames2014.nyalib.sound.SoundHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.EnumProperty;
import net.modificationstation.stationapi.api.state.property.Properties;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.StringIdentifiable;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.world.BlockStateView;

import java.util.Random;

@SuppressWarnings("DuplicatedCode")
public class ButtonBlockTemplate extends TemplateBlock {
    public static final EnumProperty<ButtonType> BUTTON_TYPE = EnumProperty.of("type", ButtonType.class);

    public ButtonBlockTemplate(Identifier identifier, Block baseBlock, Material material, Identifier texture) {
        super(identifier, material);
        this.setTickRandomly(true);
        if (texture != null) {
            TemplateBlockRegistry.registerButton(identifier, texture);
        }
    }

    public ButtonBlockTemplate(Identifier identifier, Block baseBlock, Identifier texture) {
        this(identifier, baseBlock, baseBlock.material, texture);
    }

    public ButtonBlockTemplate(Identifier identifier, Block baseBlock) {
        this(identifier, baseBlock, null);
    }

    // Blockstate Properties
    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(BUTTON_TYPE);
        builder.add(Properties.HORIZONTAL_FACING);
        builder.add(Properties.POWERED);
    }

    // Placement
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockState state = getDefaultState();

        switch (context.getSide()) {
            case UP -> {
                state = state.with(BUTTON_TYPE, ButtonType.FLOOR);
                state = state.with(Properties.HORIZONTAL_FACING, context.getHorizontalPlayerFacing().getOpposite());
            }

            case DOWN -> {
                state = state.with(BUTTON_TYPE, ButtonType.CEILING);
                state = state.with(Properties.HORIZONTAL_FACING, context.getHorizontalPlayerFacing().getOpposite());
            }

            case NORTH, SOUTH, EAST, WEST -> {
                state = state.with(BUTTON_TYPE, ButtonType.WALL);
                state = state.with(Properties.HORIZONTAL_FACING, context.getSide());
            }
        }

        state = state.with(Properties.POWERED, false);

        return state;
    }

    public boolean checkPlacementValidity(World world, int x, int y, int z) {
        BlockState state = world.getBlockState(new BlockPos(x, y, z));

        switch (state.get(BUTTON_TYPE)) {
            case CEILING -> {
                return canPlaceOn(world, x, y + 1, z);
            }
            case FLOOR -> {
                return canPlaceOn(world, x, y - 1, z);
            }
            case WALL -> {
                Direction side = state.get(Properties.HORIZONTAL_FACING).getOpposite();
                return canPlaceOn(world, x + side.getOffsetX(), y + side.getOffsetY(), z + side.getOffsetZ());
            }
        }

        return false;
    }

    public boolean canPlaceOn(World world, int x, int y, int z) {
        return world.shouldSuffocate(x, y, z);
    }

    // Ticking & Updates
    @Override
    public int getTickRate() {
        return 20;
    }

    @Override
    public void onTick(World world, int x, int y, int z, Random random) {
        if (world.isRemote) {
            return;
        }

        BlockState state = world.getBlockState(x, y, z);
        if (state.get(Properties.POWERED)) {
            world.setBlockStateWithNotify(x, y, z, state.with(Properties.POWERED, false));

            switch (state.get(BUTTON_TYPE)) {
                case CEILING -> {
                    world.notifyNeighbors(x, y + 1, z, this.id);
                }
                case FLOOR -> {
                    world.notifyNeighbors(x, y - 1, z, this.id);
                }
                case WALL -> {
                    Direction dir = state.get(Properties.HORIZONTAL_FACING).getOpposite();
                    world.notifyNeighbors(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), this.id);
                }
            }

            SoundHelper.playSound(world, x + 0.5D, y + 0.5D, z + 0.5D, "random.click", 0.3F, 0.5F);
        }
    }

    @Override
    public void neighborUpdate(World world, int x, int y, int z, int id) {
        if (!world.isRemote) {
            if (!checkPlacementValidity(world, x, y, z)) {
                this.dropStacks(world, x, y, z, 0);
                world.setBlockState(x, y, z, States.AIR.get());
            }
        }
    }

    // Button Pressing
    @Override
    public void onBlockBreakStart(World world, int x, int y, int z, PlayerEntity player) {
        this.onUse(world, x, y, z, player);
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        if (world.isRemote) {
            return true;
        }

        BlockState state = world.getBlockState(x, y, z);

        // This can happen if the block is broken but the breaking action still triggers the use action
        if (!state.isOf(this)) {
            return false;
        }

        // If the button isn't already pressed, press it
        if (!state.get(Properties.POWERED)) {
            SoundHelper.playSound(world, x + 0.5D, y + 0.5D, z + 0.5D, "random.click", 0.3F, 0.6F);

            world.setBlockStateWithNotify(x, y, z, state.cycle(Properties.POWERED));

            switch (state.get(BUTTON_TYPE)) {
                case CEILING -> {
                    world.notifyNeighbors(x, y + 1, z, this.id);
                }
                case FLOOR -> {
                    world.notifyNeighbors(x, y - 1, z, this.id);
                }
                case WALL -> {
                    Direction dir = state.get(Properties.HORIZONTAL_FACING).getOpposite();
                    world.notifyNeighbors(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), this.id);
                }
            }

            world.scheduleBlockUpdate(x, y, z, this.id, this.getTickRate());
        }

        return true;
    }

    // Redstone Behavior
    @Override
    public boolean canEmitRedstonePower() {
        return true;
    }

    @Override
    public boolean isEmittingRedstonePowerInDirection(BlockView blockView, int x, int y, int z, int direction) {
        if (blockView instanceof BlockStateView stateView) {
            return stateView.getBlockState(x, y, z).get(Properties.POWERED);
        }
        return false;
    }

    @Override
    public boolean canTransferPowerInDirection(World world, int x, int y, int z, int direction) {
        BlockState state = world.getBlockState(x, y, z);

        if (state.get(Properties.POWERED)) {
            switch (state.get(BUTTON_TYPE)) {
                case CEILING -> {
                    return Direction.byId(direction) == Direction.DOWN;
                }
                case FLOOR -> {
                    return Direction.byId(direction) == Direction.UP;
                }
                case WALL -> {
                    return state.get(Properties.HORIZONTAL_FACING) == Direction.byId(direction);
                }
            }
        }

        return false;
    }

    // Update neighbors on break
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState) {
        // If the button has been broken, send a block update to neighbors
        if (state.isOf(this) && newState.isAir()) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            switch (state.get(BUTTON_TYPE)) {
                case CEILING -> {
                    world.notifyNeighbors(x, y + 1, z, this.id);
                }
                case FLOOR -> {
                    world.notifyNeighbors(x, y - 1, z, this.id);
                }
                case WALL -> {
                    Direction dir = state.get(Properties.HORIZONTAL_FACING).getOpposite();
                    world.notifyNeighbors(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), this.id);
                }
            }
            world.scheduleBlockUpdate(x, y, z, this.id, this.getTickRate());
        }
    }

    // Collision Shape
    @Override
    public Box getCollisionShape(World world, int x, int y, int z) {
        return null;
    }

    // Bounding Box
    @Override
    public void updateBoundingBox(BlockView blockView, int x, int y, int z) {
        if (!(blockView instanceof BlockStateView view)) {
            return;
        }

        BlockState state = view.getBlockState(x, y, z);

        switch (state.get(BUTTON_TYPE)) {
            case CEILING -> {
                switch (state.get(Properties.HORIZONTAL_FACING)) {
                    case EAST, WEST -> {
                        if (state.get(Properties.POWERED)) {
                            setBoundingBox(0.3125F, 0.9375F, 0.375F, 0.6875F, 1.0F, 0.625F);
                        } else {
                            setBoundingBox(0.3125F, 0.875F, 0.375F, 0.6875F, 1.0F, 0.625F);
                        }
                    }
                    case NORTH, SOUTH -> {
                        if (state.get(Properties.POWERED)) {
                            setBoundingBox(0.375F, 0.9375F, 0.3125F, 0.625F, 1.0F, 0.6875F);
                        } else {
                            setBoundingBox(0.375F, 0.875F, 0.3125F, 0.625F, 1.0F, 0.6875F);
                        }
                    }
                }
            }

            case FLOOR -> {
                switch (state.get(Properties.HORIZONTAL_FACING)) {
                    case EAST, WEST -> {
                        if (state.get(Properties.POWERED)) {
                            setBoundingBox(0.3125F, 0.0F, 0.375F, 0.6875F, 0.0625F, 0.625F);
                        } else {
                            setBoundingBox(0.3125F, 0.0F, 0.375F, 0.6875F, 0.125F, 0.625F);
                        }
                    }
                    case NORTH, SOUTH -> {
                        if (state.get(Properties.POWERED)) {
                            setBoundingBox(0.375F, 0.0F, 0.3125F, 0.625F, 0.0625F, 0.6875F);
                        } else {
                            setBoundingBox(0.375F, 0.0F, 0.3125F, 0.625F, 0.125F, 0.6875F);
                        }
                    }
                }
            }

            case WALL -> {
                switch (state.get(Properties.HORIZONTAL_FACING)) {
                    case EAST -> {
                        if (state.get(Properties.POWERED)) {
                            setBoundingBox(0.3125F, 0.375F, 1.0F, 0.6875F, 0.625F, 0.9375F);
                        } else {
                            setBoundingBox(0.3125F, 0.375F, 1.0F, 0.6875F, 0.625F, 0.875F);
                        }
                    }
                    case WEST -> {
                        if (state.get(Properties.POWERED)) {
                            setBoundingBox(0.3125F, 0.375F, 0.0F, 0.6875F, 0.625F, 0.0625F);
                        } else {
                            setBoundingBox(0.3125F, 0.375F, 0.0F, 0.6875F, 0.625F, 0.125F);
                        }
                    }
                    case NORTH -> {
                        if (state.get(Properties.POWERED)) {
                            setBoundingBox(0.9375F, 0.375F, 0.3125F, 1.0F, 0.625F, 0.6875F);
                        } else {
                            setBoundingBox(0.875F, 0.375F, 0.3125F, 1.0F, 0.625F, 0.6875F);
                        }
                    }
                    case SOUTH -> {
                        if (state.get(Properties.POWERED)) {
                            setBoundingBox(0.0F, 0.375F, 0.3125F, 0.0625F, 0.625F, 0.6875F);
                        } else {
                            setBoundingBox(0.0F, 0.375F, 0.3125F, 0.125F, 0.625F, 0.6875F);
                        }
                    }
                }
            }
        }
    }

    // Rendering
    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    // Property Enum
    public enum ButtonType implements StringIdentifiable {
        CEILING("ceiling"),
        FLOOR("floor"),
        WALL("wall");

        public final String type;

        ButtonType(String type) {
            this.type = type;
        }

        @Override
        public String asString() {
            return type;
        }
    }
}
