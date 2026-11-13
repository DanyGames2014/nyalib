package net.danygames2014.nyalibtest.energy.material;

import net.minecraft.block.MapColor;
import net.minecraft.block.material.Material;

public class CableMaterial extends Material {
    public CableMaterial(MapColor mapColor) {
        super(mapColor);
    }

    @Override
    public boolean blocksVision() {
        return false;
    }

    public boolean suffocates() {
        return false;
    }
}
