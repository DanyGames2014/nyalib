package net.danygames2014.nyalibtest.fluid;

import net.danygames2014.nyalibtest.NyaLibTest;
import net.danygames2014.nyalibtest.fluid.entity.FluidTankBlockEntity;
import net.danygames2014.nyalibtest.screen.handler.FluidTankScreenHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;

public class FluidTankBlock extends TemplateBlockWithEntity {
    public FluidTankBlock(Identifier identifier) {
        super(identifier, Material.METAL);
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new FluidTankBlockEntity();
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        if (world.getBlockEntity(x, y, z) instanceof FluidTankBlockEntity tank) {
            if (player.isSneaking()) {
                player.sendMessage("Fluid Tank Contents: ");
                for (int i = 0; i < tank.getFluidSlots(null); i++) {
                    player.sendMessage(i + ": " + tank.getFluid(i, null));
                }
            } else {
                GuiHelper.openGUI(player, NyaLibTest.NAMESPACE.id( "fluid_tank"), tank, new FluidTankScreenHandler(player, tank));       
            }
        }
        
        return false;
    }

    @Override
    public boolean isOpaque() {
        return false;
    }
}
