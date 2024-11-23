package net.danygames2014.nyalibtest.block.energy;

import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class WireBlock extends TemplateBlock {
    public WireBlock(Identifier identifier) {
        super(identifier, Material.METAL);
    }
}
