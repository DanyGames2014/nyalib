package net.danygames2014.nyalib.block;

import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.EnumProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.StringIdentifiable;
import net.modificationstation.stationapi.api.util.math.Direction;

public class WallBlockTemplate extends TemplateBlock {
    public static final EnumProperty<WallType> EAST = EnumProperty.of("east", WallType.class);
    public static final EnumProperty<WallType> WEST = EnumProperty.of("west", WallType.class);
    public static final EnumProperty<WallType> NORTH = EnumProperty.of("north", WallType.class);
    public static final EnumProperty<WallType> SOUTH = EnumProperty.of("south", WallType.class);
    public static final BooleanProperty UP = BooleanProperty.of("up");

    public WallBlockTemplate(Identifier identifier, Block baseBlock, Material material, Identifier texture) {
        super(identifier, material);
        if (texture != null) {
            TemplateBlockRegistry.registerWall(identifier, texture);
        }
    }

    public WallBlockTemplate(Identifier identifier, Block baseBlock, Identifier texture) {
        this(identifier, baseBlock, baseBlock.material, texture);
    }

    public WallBlockTemplate(Identifier identifier, Block baseBlock) {
        this(identifier, baseBlock, null);
    }

    // Block Properties
    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(EAST, WEST, NORTH, SOUTH, UP);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return super.getPlacementState(context)
                .with(EAST, WallType.NONE)
                .with(WEST, WallType.NONE)
                .with(NORTH, WallType.NONE)
                .with(SOUTH, WallType.NONE)
                .with(UP, true);
    }

    @Override
    public void onPlaced(World world, int x, int y, int z) {
        updateConnections(world, x, y, z);
        super.onPlaced(world, x, y, z);
    }

    @Override
    public void neighborUpdate(World world, int x, int y, int z, int id) {
        updateConnections(world, x, y, z);
        super.neighborUpdate(world, x, y, z, id);
    }

    public void updateConnections(World world, int x, int y, int z) {
        BlockState state = world.getBlockState(x, y, z);

        Direction side;
        // East
        state = getWallState(world, x, y, z, state, Direction.EAST, EAST);
        state = getWallState(world, x, y, z, state, Direction.WEST, WEST);
        state = getWallState(world, x, y, z, state, Direction.NORTH, NORTH);
        state = getWallState(world, x, y, z, state, Direction.SOUTH, SOUTH);

        state = state.with(UP, true);

        if (state.get(EAST) == WallType.NONE && state.get(WEST) == WallType.NONE && state.get(NORTH) != WallType.NONE && state.get(SOUTH) != WallType.NONE) {
            state = state.with(UP, false);
        } else if (state.get(NORTH) == WallType.NONE && state.get(SOUTH) == WallType.NONE && state.get(EAST) != WallType.NONE && state.get(WEST) != WallType.NONE) {
            state = state.with(UP, false);
        }

        // Check if we create post anyway
        if (canConnectPost(world.getBlockState(x, y + 1, z))) {
            state = state.with(UP, true);
        }

        world.setBlockStateWithNotify(x, y, z, state);
    }

    public boolean canConnectPost(BlockState state) {
        if (state.isOf(this)) {
            return state.get(UP);
        }

        Block block = state.getBlock();
        if (block instanceof TorchBlock || block instanceof RedstoneTorchBlock || block instanceof FenceBlock || block instanceof FenceBlockTemplate) {
            return true;
        }

        return false;
    }

    public boolean canConnectTo(BlockState state) {
        if (state.isAir()) {
            return false;
        }

        if (state.isOf(Block.GLOWSTONE)) {
            return false;
        }

        Block block = state.getBlock();
        if (block instanceof WallBlockTemplate || block instanceof FenceBlockTemplate || block instanceof FenceGateBlockTemplate || block instanceof FenceBlock) {
            return true;
        }

        if (state.getMaterial().suffocates() && state.getBlock().isFullCube()) {
            return true;
        }

        return false;
    }

    private BlockState getWallState(World world, int x, int y, int z, BlockState state, Direction side, EnumProperty<WallType> property) {
        if (canConnectTo(world.getBlockState(x + side.getOffsetX(), y, z + side.getOffsetZ()))) {
            state = state.with(property, WallType.LOW);

            if (world.getBlockState(x, y + 1, z).isOf(this) && world.getBlockState(x + side.getOffsetX(), y + 1, z + side.getOffsetZ()).isOf(this)) {
                state = state.with(property, WallType.TALL);
            }
        } else {
            state = state.with(property, WallType.NONE);
        }

        return state;
    }

    // Collision and Bounding Box
    public Box generateBox(World world, int x, int y, int z, boolean collider) {
        BlockState state = world.getBlockState(x, y, z);

        if (!(state.getBlock() instanceof WallBlockTemplate)) {
            return null;
        }

        Box box = Box.create(state.get(NORTH) != WallType.NONE ? 0 : 0.25F, 0F, state.get(EAST) != WallType.NONE ? 0F : 0.25F, state.get(SOUTH) != WallType.NONE ? 1F : 0.75F, 1F, state.get(WEST) != WallType.NONE ? 1F : 0.75F);

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
    public Box getCollisionShape(World world, int x, int y, int z) {
        return generateBox(world, x, y, z, true);
    }

    @Override
    public Box getBoundingBox(World world, int x, int y, int z) {
        return generateBox(world, x, y, z, false);
    }

    // Rendering
    @Override
    public boolean isOpaque() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    public enum WallType implements StringIdentifiable {
        NONE("none"),
        LOW("low"),
        TALL("tall");

        public final String id;

        WallType(String id) {
            this.id = id;
        }

        @Override
        public String asString() {
            return id;
        }
    }
}
