package net.danygames2014.nyalibtest.item;

import net.danygames2014.nyalibtest.item.entity.SideHopperBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.Properties;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;

public class SideHopperBlock extends TemplateBlockWithEntity {
    public SideHopperBlock(Identifier identifier) {
        super(identifier, Material.METAL);
        this.setHardness(0.1F);
        this.setResistance(0.1F);
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
        super.appendProperties(builder);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return getDefaultState().with(Properties.HORIZONTAL_FACING, context.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new SideHopperBlockEntity();
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        if (world.getBlockEntity(x, y, z) instanceof SideHopperBlockEntity hopper) {
            player.sendMessage("Counter : " + hopper.tickCounter + " | Buffer : " + hopper.internalBuffer);
            return true;
        }
        return false;
    }
}
