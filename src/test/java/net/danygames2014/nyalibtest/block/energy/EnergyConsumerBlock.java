package net.danygames2014.nyalibtest.block.energy;

import net.danygames2014.nyalib.energy.template.block.EnergyConsumerBlockTemplate;
import net.danygames2014.nyalib.energy.template.block.entity.EnergyConsumerBlockEntityTemplate;
import net.danygames2014.nyalibtest.block.energy.entity.EnergyConsumerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

public class EnergyConsumerBlock extends EnergyConsumerBlockTemplate {
    public EnergyConsumerBlock(Identifier identifier, Material material) {
        super(identifier, material);
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new EnergyConsumerBlockEntity();
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        if (world.getBlockEntity(x, y, z) instanceof EnergyConsumerBlockEntityTemplate consumer) {
            if (player.isSneaking()) {
                consumer.removeEnergy(10);
            }

            player.sendMessage(consumer.getEnergyStored() + "/" + consumer.getEnergyCapacity());
            return true;
        }
        return false;
    }
}
