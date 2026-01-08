package net.danygames2014.nyalibtest.fluid;

import net.danygames2014.nyalibtest.NyaLibTest;
import net.danygames2014.nyalibtest.fluid.entity.ManagedFluidTankBlockEntity;
import net.danygames2014.nyalibtest.screen.handler.ManagedFluidTankScreenHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;

public class ManagedFluidTankBlock extends TemplateBlockWithEntity {
    public ManagedFluidTankBlock(Identifier identifier) {
        super(identifier, Material.METAL);
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new ManagedFluidTankBlockEntity();
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        if (world.getBlockEntity(x, y, z) instanceof ManagedFluidTankBlockEntity tank) {
            if (player.isSneaking()) {
                player.sendMessage("Fluid Tank Contents: ");
                for (int i = 0; i < tank.getFluidSlots(null); i++) {
                    player.sendMessage(i + ": " + tank.getFluid(i, null));
                }
            } else {
                GuiHelper.openGUI(player, NyaLibTest.NAMESPACE.id( "managed_fluid_tank"), tank, new ManagedFluidTankScreenHandler(player, tank));
            }
        }
        
        return false;
    }

    @Override
    public boolean isOpaque() {
        return false;
    }
}
