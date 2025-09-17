package net.danygames2014.nyalib.block;

import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class FenceBlockTemplate extends TemplateBlock {
    public static final BooleanProperty NORTH = BooleanProperty.of("north"); // X--
    public static final BooleanProperty SOUTH = BooleanProperty.of("south"); // X++
    public static final BooleanProperty EAST = BooleanProperty.of("east"); // Z--
    public static final BooleanProperty WEST = BooleanProperty.of("west"); // Z++

    public FenceBlockTemplate(Identifier identifier, Block baseBlock) {
        this(identifier, baseBlock, null);
    }
    
    public FenceBlockTemplate(Identifier identifier, Block baseBlock, Identifier texture) {
        super(identifier, baseBlock.material);
        this.setOpacity(0);
        if (texture != null) {
            TemplateBlockRegistry.registerFence(identifier, texture);
        }
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(NORTH);
        builder.add(SOUTH);
        builder.add(EAST);
        builder.add(WEST);
    }

    public Box generateBox(World world, int x, int y, int z, boolean collider) {
        BlockState state = world.getBlockState(x, y, z);

        if (!(state.getBlock() instanceof FenceBlockTemplate)) {
            return null;
        }

        Box box = Box.create(state.get(NORTH) ? 0 : 0.375F, 0F, state.get(EAST) ? 0F : 0.375F, state.get(SOUTH) ? 1F : 0.625F, 1F, state.get(WEST) ? 1.F : 0.625F);

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

    @Override
    public void neighborUpdate(World world, int x, int y, int z, int id) {
        this.updateConnections(world, x, y, z);
        super.neighborUpdate(world, x, y, z, id);
    }

    @Override
    public void onPlaced(World world, int x, int y, int z) {
        this.updateConnections(world, x, y, z);
        super.onPlaced(world, x, y, z);
    }

    public void updateConnections(World world, int x, int y, int z) {
        BlockState state = world.getBlockState(x, y, z);

        if (!state.isOf(this)) {
            return;
        }
        
        state = state.with(NORTH, canConnectTo(world.getBlockState(x - 1, y, z)));
        state = state.with(SOUTH, canConnectTo(world.getBlockState(x + 1, y, z)));
        state = state.with(EAST, canConnectTo(world.getBlockState(x, y, z - 1)));
        state = state.with(WEST, canConnectTo(world.getBlockState(x, y, z + 1)));

        world.setBlockStateWithNotify(x, y, z, state);
    }

    public boolean canConnectTo(BlockState state) {
        if (state.isAir()) {
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

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isOpaque() {
        return false;
    }
}
