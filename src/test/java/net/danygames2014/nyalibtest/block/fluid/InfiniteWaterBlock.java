package net.danygames2014.nyalibtest.block.fluid;

import net.danygames2014.nyalibtest.block.fluid.entity.InfiniteWaterBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;

public class InfiniteWaterBlock extends TemplateBlockWithEntity {
    public InfiniteWaterBlock(Identifier identifier) {
        super(identifier, Material.METAL);
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new InfiniteWaterBlockEntity();
    }
}
