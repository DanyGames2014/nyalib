package net.danygames2014.nyalibtest.simpleenergy;

import net.danygames2014.nyalibtest.simpleenergy.entity.InfiniteSimpleEnergyBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;

public class SimpleInfiniteEnergyBlock extends TemplateBlockWithEntity {
    public SimpleInfiniteEnergyBlock(Identifier identifier) {
        super(identifier, Material.METAL);
        this.setHardness(1F);
        this.setResistance(1F);
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new InfiniteSimpleEnergyBlockEntity();
    }
}
