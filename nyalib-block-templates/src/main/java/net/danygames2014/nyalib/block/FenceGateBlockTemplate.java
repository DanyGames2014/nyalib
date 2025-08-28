package net.danygames2014.nyalib.block;

import net.danygames2014.nyalib.sound.SoundHelper;
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
    public static final BooleanProperty IN_WALL = BooleanProperty.of("in_wall");

    public FenceGateBlockTemplate(Identifier identifier, Block baseBlock) {
        this(identifier, baseBlock, null);
    }

    public FenceGateBlockTemplate(Identifier identifier, Block baseBlock, Identifier texture) {
        super(identifier, baseBlock.material);
        this.setOpacity(0);
        if (texture != null) {
            TemplateBlockRegistry.registerFenceGate(identifier, texture);
        }
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(OPEN);
        builder.add(FACING);
        builder.add(IN_WALL);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return this.getDefaultState().with(FACING, context.getHorizontalPlayerFacing()).with(OPEN, false).with(IN_WALL, false);
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        BlockState state = world.getBlockState(x, y, z);
        
        if (state.get(OPEN)) {
            world.setBlockStateWithNotify(x, y, z, state.with(OPEN, false));
            SoundHelper.playSound(world, x, y, z, getCloseSound(world, x, y, z), 1.0F, (world.random.nextFloat() * 0.1F) + 0.9F);
        } else {
            world.setBlockStateWithNotify(x, y, z, state.with(OPEN, true));
            SoundHelper.playSound(world, x, y, z, getOpenSound(world, x, y, z), 1.0F, (world.random.nextFloat() * 0.1F) + 0.9F);
        }
        
        world.setBlockDirty(x, y, z);
        return true;
    }

    public String getOpenSound(World world, int x, int y, int z) {
        return "random.door_open";
    }

    public String getCloseSound(World world, int x, int y, int z) {
        return "random.door_close";
    }

    @Override
    public void neighborUpdate(World world, int x, int y, int z, int id) {
        super.neighborUpdate(world, x, y, z, id);
        updateInWall(world, x, y, z);
    }

    @Override
    public void onPlaced(World world, int x, int y, int z) {
        super.onPlaced(world, x, y, z);
        updateInWall(world, x, y, z);
    }

    public void updateInWall(World world, int x, int y, int z) {
        BlockState state = world.getBlockState(x, y, z);
        state = state.with(IN_WALL, false);

        for (Direction side : Direction.values()) {
            if (side.getHorizontal() != -1) {
                if (world.getBlockState(x + side.getOffsetX(), y, z + side.getOffsetZ()).getBlock() instanceof WallBlockTemplate) {
                    state = state.with(IN_WALL, true);
                    world.setBlockStateWithNotify(x, y, z, state);
                    return;
                }
            }
        }

        world.setBlockStateWithNotify(x, y, z, state);
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
