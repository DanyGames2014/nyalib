package net.danygames2014.nyalibtest.redstone;

import net.danygames2014.nyalib.block.RedstoneLevelProvider;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.world.BlockStateView;

public class CustomRedstoneLevelBlock extends TemplateBlock implements RedstoneLevelProvider {
    public static final IntProperty REDSTONE_LEVEL = IntProperty.of("redstone_level", 0, 15);
    public static final BooleanProperty STRONG = BooleanProperty.of("strong");
    
    public CustomRedstoneLevelBlock(Identifier identifier, Material material) {
        super(identifier, material);
        this.setDefaultState(this.getDefaultState().with(REDSTONE_LEVEL, 0).with(STRONG, false));
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(REDSTONE_LEVEL);
        builder.add(STRONG);
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        BlockState state = world.getBlockState(x, y, z);
        
        if (player.isSneaking()) {
            state = state.cycle(STRONG);
        } else {
            state = state.cycle(REDSTONE_LEVEL);
        }
        
        world.setBlockStateWithNotify(x, y, z, state);

        world.notifyNeighbors(x + 1, y, z, this.id);
        world.notifyNeighbors(x - 1, y, z, this.id);
        world.notifyNeighbors(x, y, z + 1, this.id);
        world.notifyNeighbors(x, y, z - 1, this.id);
        world.notifyNeighbors(x, y - 1, z, this.id);
        world.notifyNeighbors(x, y + 1, z, this.id);
        
        return true;
    }

    @Override
    public boolean canEmitRedstonePower() {
        return true;
    }

    @Override
    public int getSidePowerLevel(BlockView blockView, int x, int y, int z, int side) {
        if (blockView instanceof BlockStateView stateView) {
            if (!stateView.getBlockState(x, y, z).get(STRONG)) {
                return stateView.getBlockState(x, y, z).get(REDSTONE_LEVEL);
            }
        }
        
        return 0;
    }

    @Override
    public int getSideStrongPowerLevel(World world, int x, int y, int z, int side) {
        if (world.getBlockState(x,y,z).get(STRONG)) {
            return world.getBlockState(x,y,z).get(REDSTONE_LEVEL);
        }
        
        return 0;
    }
}
