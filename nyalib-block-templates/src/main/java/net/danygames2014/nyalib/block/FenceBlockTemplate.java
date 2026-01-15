package net.danygames2014.nyalib.block;

import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;

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

        float minX = 0.375F;
        float minY = 0.0F;
        float minZ = 0.375F;

        float maxX = 0.625F;
        float maxY = 1.0F;
        float maxZ = 0.625F;

        if (state.get(WEST)) {
            maxZ = 1.0F;
        }

        if (state.get(EAST)) {
            minZ = 0.0F;
        }

        if (state.get(NORTH)) {
            minX = 0.0F;
        }

        if (state.get(SOUTH)) {
            maxX = 1.0F;
        }
        
        if (collider) {
            maxY += 0.5F;
        }

        return Box.createCached(x + minX, y + minY, z + minZ, x + maxX, y + maxY, z + maxZ);
    }
    
    @Override
    public Box getBoundingBox(World world, int x, int y, int z) {
        return generateBox(world, x, y, z, false);
    }

    @Override
    public void addIntersectingBoundingBox(World world, int x, int y, int z, Box box, ArrayList boxes) {
        BlockState state = world.getBlockState(x, y, z);

        if (!(state.getBlock() instanceof FenceBlockTemplate)) {
            return;
        }

        this.setBoundingBox(0.375F, 0.0F, 0.375F, 0.625F, 1.5F, 0.625F);
        super.addIntersectingBoundingBox(world, x, y, z, box, boxes);

        if (state.get(WEST)) {
            this.setBoundingBox(0.375F, 0.0F, 0.375F, 0.625F, 1.5F, 1.0F);
            super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
        }

        if (state.get(EAST)) {
            this.setBoundingBox(0.375F, 0.0F, 0.0F, 0.625F, 1.5F, 0.625F);
            super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
        }

        if (state.get(SOUTH)) {
            this.setBoundingBox(0.375F, 0.0F, 0.375F, 1.0F, 1.5F, 0.625F);
            super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
        }

        if (state.get(NORTH)) {
            this.setBoundingBox(0.0F, 0.0F, 0.375F, 0.625F, 1.5F, 0.625F);
            super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
        }
    }
    
    @Override
    public HitResult raycast(World world, int x, int y, int z, Vec3d startPos, Vec3d endPos) {
        Box box = getBoundingBox(world, x, y, z).expand(0.05D, 0.05D, 0.05D);

        this.updateBoundingBox(world, x, y, z);

        HitResult hitResult = box.raycast(startPos, endPos);

        if (hitResult == null) {
            return null;
        }

        if (hitResult.blockX == 0 && hitResult.blockY == 0 && hitResult.blockZ == 0) {
            return new HitResult(x,y,z,hitResult.side, hitResult.pos);
        }

        return hitResult;
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
