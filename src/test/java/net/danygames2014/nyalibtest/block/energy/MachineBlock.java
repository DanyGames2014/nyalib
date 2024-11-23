package net.danygames2014.nyalibtest.block.energy;

import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class MachineBlock extends TemplateBlock {
    public MachineBlock(Identifier identifier) {
        super(identifier, Material.METAL);
    }
}
