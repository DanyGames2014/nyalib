package net.danygames2014.nyalibtest.energy;

import net.danygames2014.nyalib.energy.template.block.EnergySourceBlockTemplate;
import net.danygames2014.nyalib.energy.template.block.entity.EnergySourceBlockEntityTemplate;
import net.danygames2014.nyalibtest.energy.entity.EnergySourceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

public class EnergySourceBlock extends EnergySourceBlockTemplate {
    public EnergySourceBlock(Identifier identifier, Material material) {
        super(identifier, material);
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new EnergySourceBlockEntity();
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        if (world.getBlockEntity(x, y, z) instanceof EnergySourceBlockEntityTemplate source) {
            if (player.isSneaking()) {
                source.addEnergy(50);
            }

            player.sendMessage(source.getEnergyStored() + "/" + source.getEnergyCapacity());
            return true;
        }
        return false;
    }
}
