package net.danygames2014.nyalibtest.block.fluid;

import net.danygames2014.nyalibtest.blockentity.fluid.FluidTankBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
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
            player.method_490("Fluid Tank Contents: ");
            for (int i = 0; i < tank.getFluidSlots(null); i++) {
                player.method_490(i + ": " + tank.getFluidInSlot(i, null));
            }
        }
        return false;
    }
}
