package net.danygames2014.nyalib.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.EnumProperty;
import net.modificationstation.stationapi.api.state.property.Properties;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.StringIdentifiable;
import net.modificationstation.stationapi.api.world.BlockStateView;

public class ButtonBlockTemplate extends TemplateBlock {
    public static final EnumProperty<ButtonType> BUTTON_TYPE = EnumProperty.of("type", ButtonType.class);

    public ButtonBlockTemplate(Identifier identifier, Block baseBlock, Identifier texture) {
        super(identifier, baseBlock.material);
        if (texture != null) {
            TemplateBlockRegistry.registerButton(identifier, texture);
        }
    }

    public ButtonBlockTemplate(Identifier identifier, Block baseBlock) {
        this(identifier, baseBlock, null);
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(BUTTON_TYPE);
        builder.add(Properties.HORIZONTAL_FACING);
        builder.add(Properties.POWERED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockState state = getDefaultState();

        state = switch (context.getSide()) {
            case UP -> state.with(BUTTON_TYPE, ButtonType.FLOOR);
            case DOWN -> state.with(BUTTON_TYPE, ButtonType.CEILING);
            default -> state.with(BUTTON_TYPE, ButtonType.WALL);
        };

        state = state.with(Properties.HORIZONTAL_FACING, context.getHorizontalPlayerFacing().getOpposite());

        state = state.with(Properties.POWERED, false);

        return state;
    }

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
                            setBoundingBox(0.3125F,0.9375F,0.375F,0.6875F,1.0F,0.625F);
                        } else {
                            setBoundingBox(0.3125F,0.875F,0.375F,0.6875F,1.0F,0.625F);
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
                            setBoundingBox(0.3125F,0.0F,0.375F,0.6875F,0.0625F,0.625F);
                        } else {
                            setBoundingBox(0.3125F,0.0F,0.375F,0.6875F,0.125F,0.625F);
                        }
                    }
                    case NORTH, SOUTH -> {
                        if (state.get(Properties.POWERED)) {
                            setBoundingBox(0.375F,0.0F,0.3125F,0.625F,0.0625F,0.6875F);
                        } else {
                            setBoundingBox(0.375F,0.0F,0.3125F,0.625F,0.125F,0.6875F);
                        }
                    }
                }
            }
            
            case WALL -> {
                switch (state.get(Properties.HORIZONTAL_FACING)) {
                    
                    case EAST -> {
                        if (state.get(Properties.POWERED)) {
                            setBoundingBox(0.3125F,0.375F,1.0F,0.6875F,0.625F,0.9375F);
                        } else {
                            setBoundingBox(0.3125F,0.375F,1.0F,0.6875F,0.625F,0.875F);
                        }
                    }
                    case WEST -> {
                        if (state.get(Properties.POWERED)) {
                            setBoundingBox(0.3125F,0.375F,0.0F,0.6875F,0.625F,0.0625F);
                        } else {
                            setBoundingBox(0.3125F,0.375F,0.0F,0.6875F,0.625F,0.125F);
                        }
                    }
                    case NORTH -> {
                        if (state.get(Properties.POWERED)) {
                            setBoundingBox(0.9375F,0.375F,0.3125F,1.0F,0.625F,0.6875F);
                        } else {
                            setBoundingBox(0.875F,0.375F,0.3125F,1.0F,0.625F,0.6875F);
                        }
                    }
                    case SOUTH -> {
                        if (state.get(Properties.POWERED)) {
                            setBoundingBox(0.0F,0.375F,0.3125F,0.0625F,0.625F,0.6875F);
                        } else {
                            setBoundingBox(0.0F,0.375F,0.3125F,0.125F,0.625F,0.6875F);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        if (world.isRemote) {
            return super.onUse(world, x, y, z, player);
        }

        BlockState state = world.getBlockState(x, y, z);
        world.playSound((double) x + (double) 0.5F, (double) y + (double) 0.5F, (double) z + (double) 0.5F, "random.click", 0.3F, 0.6F);
        world.setBlockStateWithNotify(x, y, z, state.cycle(Properties.POWERED));
        return true;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

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
