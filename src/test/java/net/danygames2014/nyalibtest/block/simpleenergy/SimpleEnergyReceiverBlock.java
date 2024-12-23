package net.danygames2014.nyalibtest.block.simpleenergy;

import net.danygames2014.nyalibtest.block.simpleenergy.entity.SimpleEnergyReceiverBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;

public class SimpleEnergyReceiverBlock extends TemplateBlockWithEntity {
    public SimpleEnergyReceiverBlock(Identifier identifier) {
        super(identifier, Material.METAL);
        this.setHardness(1F);
        this.setResistance(1F);
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(x, y, z);
        if (blockEntity instanceof SimpleEnergyReceiverBlockEntity receiverBlockEntity) {
            player.sendMessage(receiverBlockEntity.getEnergyStored() + "/" + receiverBlockEntity.getEnergyCapacity());
            return true;
        }
        return false;
    }

    @Override
    public void onBlockBreakStart(World world, int x, int y, int z, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(x, y, z);
        if (blockEntity instanceof SimpleEnergyReceiverBlockEntity receiverBlockEntity) {
            receiverBlockEntity.storedEnergy = 0;
            player.sendMessage(receiverBlockEntity.getEnergyStored() + "/" + receiverBlockEntity.getEnergyCapacity());
        }
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new SimpleEnergyReceiverBlockEntity();
    }
}
