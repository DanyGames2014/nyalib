package net.danygames2014.nyalib.block;

import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.Properties;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;

public class PaneBlockTemplate extends TemplateBlock {
    // TODO: Proper bounding & collision Box
    
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
        builder.add(Properties.NORTH, Properties.SOUTH, Properties.EAST, Properties.WEST);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return super.getPlacementState(context)
                .with(Properties.EAST, false)
                .with(Properties.WEST, false)
                .with(Properties.NORTH, false)
                .with(Properties.SOUTH, false);
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

        state = getPaneState(world, x, y, z, state, Direction.EAST, Properties.EAST);
        state = getPaneState(world, x, y, z, state, Direction.WEST, Properties.WEST);
        state = getPaneState(world, x, y, z, state, Direction.NORTH,Properties.NORTH);
        state = getPaneState(world, x, y, z, state, Direction.SOUTH,Properties.SOUTH);

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
