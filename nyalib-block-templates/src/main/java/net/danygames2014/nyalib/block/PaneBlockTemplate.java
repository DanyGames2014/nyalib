package net.danygames2014.nyalib.block;

import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.ArrayList;

import static net.modificationstation.stationapi.api.state.property.Properties.*;

public class PaneBlockTemplate extends TemplateBlock {
    public PaneBlockTemplate(Identifier identifier, Block baseBlock, Material material, Identifier texture, Identifier edgeTexture) {
        super(identifier, material);
        if(texture != null) {
            TemplateBlockRegistry.registerPane(identifier, texture, edgeTexture);
        }
    }
    
    public PaneBlockTemplate(Identifier identifier, Block baseBlock, Identifier texture, Identifier edgeTexture) {
        this(identifier, baseBlock, baseBlock.material, texture, edgeTexture);
    }
    
    public PaneBlockTemplate(Identifier identifier, Block baseBlock, Identifier texture) {
        this(identifier, baseBlock, texture, texture);
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(NORTH, SOUTH, EAST, WEST);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return super.getPlacementState(context)
                .with(EAST, false)
                .with(WEST, false)
                .with(NORTH, false)
                .with(SOUTH, false);
    }

    @Override
    public Box getCollisionShape(World world, int x, int y, int z) {
        return super.getCollisionShape(world, x, y, z);
    }

    @Override
    public Box getBoundingBox(World world, int x, int y, int z) {
        BlockState state = world.getBlockState(x, y, z);

        if (!(state.getBlock() instanceof PaneBlockTemplate)) {
            return null;
        }

        float minX = 0.4375F;
        float minY = 0.0F;
        float minZ = 0.4375F;

        float maxX = 0.5625F;
        float maxY = 1.0F;
        float maxZ = 0.5625F;

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

        return Box.createCached(x + minX, y + minY, z + minZ, x + maxX, y + maxY, z + maxZ);
    }

    @Override
    public void addIntersectingBoundingBox(World world, int x, int y, int z, Box box, ArrayList boxes) {
        BlockState state = world.getBlockState(x, y, z);

        if (!(state.getBlock() instanceof PaneBlockTemplate)) {
            return;
        }

        if (state.get(WEST)) {
            this.setBoundingBox(0.4375F, 0.0F, 0.4375F, 0.5625F, 1.0F, 1.0F);
            super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
        }

        if (state.get(EAST)) {
            this.setBoundingBox(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 0.5625F);
            super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
        }

        if (state.get(SOUTH)) {
            this.setBoundingBox(0.4375F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
            super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
        }

        if (state.get(NORTH)) {
            this.setBoundingBox(0.0F, 0.0F, 0.4375F, 0.5625F, 1.0F, 0.5625F);
            super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
        }

        this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

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
        
        if (!state.isOf(this)) {
            return;
        }

        state = getPaneState(world, x, y, z, state, Direction.EAST, EAST);
        state = getPaneState(world, x, y, z, state, Direction.WEST, WEST);
        state = getPaneState(world, x, y, z, state, Direction.NORTH, NORTH);
        state = getPaneState(world, x, y, z, state, Direction.SOUTH, SOUTH);

        world.setBlockStateWithNotify(x, y, z, state);
    }

    public boolean canConnectTo(BlockState state) {
        if (state.isAir()) {
            return false;
        }
        
        Block block = state.getBlock();
        if (block instanceof WallBlockTemplate || block instanceof FenceBlockTemplate || block instanceof FenceGateBlockTemplate || block instanceof FenceBlock || block instanceof PaneBlockTemplate) {
            return true;
        }

        if (state.getMaterial().suffocates() && state.getBlock().isFullCube()) {
            return true;
        }

        return false;
    }

    private BlockState getPaneState(World world, int x, int y, int z, BlockState state, Direction side, BooleanProperty property) {
        return state.with(property, canConnectTo(world.getBlockState(x + side.getOffsetX(), y, z + side.getOffsetZ())));
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
