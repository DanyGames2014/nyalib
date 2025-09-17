package net.danygames2014.nyalib.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.EnumProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.world.BlockStateView;

import java.util.ArrayList;
import java.util.List;

public class SlabBlockTemplate extends TemplateBlock {

    public static final EnumProperty<SlabType> SLAB_TYPE = EnumProperty.of("slab_type", SlabType.class);

    public SlabBlockTemplate(Identifier identifier, Block baseBlock) {
        this(identifier, baseBlock, null);
    }

    public SlabBlockTemplate(Identifier identifier, Block baseBlock, Identifier texture) {
        super(identifier, baseBlock.material);
        this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        this.setOpacity(0);
        if (texture != null) {
            TemplateBlockRegistry.registerSlab(identifier, texture);
        }
    }

    @Override
    public void updateBoundingBox(BlockView blockView, int x, int y, int z) {
        if (!(blockView instanceof BlockStateView view)) {
            return;
        }

        switch (view.getBlockState(x, y, z).get(SLAB_TYPE)) {
            case BOTTOM -> this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
            case TOP -> this.setBoundingBox(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
            case DOUBLE -> this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            default ->
                    throw new IllegalStateException("Unexpected value: " + ((World) blockView).getBlockState(x, y, z).get(SLAB_TYPE));
        }
    }

    @Override
    public Box getCollisionShape(World world, int x, int y, int z) {
        BlockState state = world.getBlockState(x, y, z);

        if (!state.isOf(this)) {
            return Box.create(x + 0.0F, y + 0.0F, z + 0.0F, x + 1.0F, y + 0.5F, z + 1.0F);
        }

        return switch (world.getBlockState(x, y, z).get(SLAB_TYPE)) {
            case BOTTOM -> Box.create(x + 0.0F, y + 0.0F, z + 0.0F, x + 1.0F, y + 0.5F, z + 1.0F);
            case TOP -> Box.create(x + 0.0F, y + 0.5F, z + 0.0F, x + 1.0F, y + 1.0F, z + 1.0F);
            case DOUBLE -> Box.create(x + 0.0F, y + 0.0F, z + 0.0F, x + 1.0F, y + 1.0F, z + 1.0F);
        };
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(SLAB_TYPE);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockState state = getDefaultState();
        Direction side = context.getSide();

        if (side == Direction.DOWN) {
            return state.with(SLAB_TYPE, SlabType.TOP);
        } else {
            return state.with(SLAB_TYPE, SlabType.BOTTOM);
        }
    }

    @Override
    public void onPlaced(World world, int x, int y, int z, int direction) {
        // 1 = Placed on bottom face
        // 0 = Placed on top face

        if (!(world.getBlockState(x, y, z).get(SLAB_TYPE) == SlabType.DOUBLE)) {
            if (direction == 1 || direction == 0) {
                int offset = direction == 0 ? 1 : -1;

                if (world.getBlockId(x, y + offset, z) == this.id) {
                    if (direction == 0 && world.getBlockState(x, y + offset, z).get(SLAB_TYPE) == SlabType.TOP) {
                        world.setBlock(x, y, z, 0);
                        world.setBlockState(x, y + offset, z, this.getDefaultState().with(SLAB_TYPE, SlabType.DOUBLE));
                    } else if (direction == 1 && world.getBlockState(x, y + offset, z).get(SLAB_TYPE) == SlabType.BOTTOM) {
                        world.setBlock(x, y, z, 0);
                        world.setBlockState(x, y + offset, z, this.getDefaultState().with(SLAB_TYPE, SlabType.DOUBLE));
                    }
                }
            }
        }
    }

    @Override
    public List<ItemStack> getDropList(World world, int x, int y, int z, BlockState state, int meta) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        if (state.get(SLAB_TYPE) == SlabType.DOUBLE) {
            drops.add(new ItemStack(this, 2));
            return drops;
        } else {
            drops.add(new ItemStack(this, 1));
            return drops;
        }
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isOpaque() {
        return false;
    }
}
