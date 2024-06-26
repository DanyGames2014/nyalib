package net.danygames2014.nyalibtest.block;

import net.danygames2014.nyalibtest.blockentity.InfiniteEnergyBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;

public class InfiniteEnergyBlock extends TemplateBlockWithEntity {
    public InfiniteEnergyBlock(Identifier identifier) {
        super(identifier, Material.METAL);
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new InfiniteEnergyBlockEntity();
    }
}
