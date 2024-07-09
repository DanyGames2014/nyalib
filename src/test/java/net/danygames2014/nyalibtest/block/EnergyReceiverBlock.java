package net.danygames2014.nyalibtest.block;

import net.danygames2014.nyalibtest.blockentity.EnergyReceiverBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;

public class EnergyReceiverBlock extends TemplateBlockWithEntity {
    public EnergyReceiverBlock(Identifier identifier) {
        super(identifier, Material.METAL);
        this.setHardness(1F);
        this.setResistance(1F);
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(x, y, z);
        if (blockEntity instanceof EnergyReceiverBlockEntity receiverBlockEntity) {
            player.method_490(receiverBlockEntity.getEnergyStored() + "/" + receiverBlockEntity.getMaxEnergyStored());
            return true;
        }
        return false;
    }

    @Override
    public void onBlockBreakStart(World world, int x, int y, int z, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(x, y, z);
        if (blockEntity instanceof EnergyReceiverBlockEntity receiverBlockEntity) {
            receiverBlockEntity.storedEnergy = 0;
            player.method_490(receiverBlockEntity.getEnergyStored() + "/" + receiverBlockEntity.getMaxEnergyStored());
        }
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new EnergyReceiverBlockEntity();
    }
}
