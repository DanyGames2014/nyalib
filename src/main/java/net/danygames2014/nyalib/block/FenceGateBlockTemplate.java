package net.danygames2014.nyalib.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.DirectionProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;

public class FenceGateBlockTemplate extends TemplateBlock {
    public static final BooleanProperty OPEN = BooleanProperty.of("open");
    public static final DirectionProperty FACING = DirectionProperty.of("facing", Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);

    public FenceGateBlockTemplate(Identifier identifier, Block baseBlock) {
        super(identifier, baseBlock.material);
        this.setOpacity(0);
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(OPEN);
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return this.getDefaultState().with(FACING, context.getHorizontalPlayerFacing()).with(OPEN, false);
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        BlockState state = world.getBlockState(x, y, z);
        world.setBlockState(x, y, z, state.with(OPEN, !state.get(OPEN)));
        world.setBlockDirty(x, y, z);
        return true;
    }

    public Box generateBox(World world, int x, int y, int z, boolean collider) {
        BlockState state = world.getBlockState(x, y, z);

        if (!(state.getBlock() instanceof FenceGateBlockTemplate)) {
            return null;
        }

        Direction facing = state.get(FACING);
        boolean open = state.get(OPEN);

        if (collider && open) {
            return null;
        }

        Box box;
        if (facing.getId() == 4 || facing.getId() == 5) {
            box = Box.create(0.375f, 0.0f, 0.0f, 0.625f, 1.0f, 1.0f);
        } else {
            box = Box.create(0.0f, 0.0f, 0.375f, 1.0f, 1.0f, 0.625f);
        }

        box.minX += x;
        box.minY += y;
        box.minZ += z;
        box.maxX += x;
        box.maxY += y;
        box.maxZ += z;

        if (collider) {
            box.maxY += 0.5F;
        }

        return box;
    }

    @Override
    public Box getBoundingBox(World world, int x, int y, int z) {
        return generateBox(world, x, y, z, false);
    }

    @Override
    public Box getCollisionShape(World world, int x, int y, int z) {
        return generateBox(world, x, y, z, true);
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
