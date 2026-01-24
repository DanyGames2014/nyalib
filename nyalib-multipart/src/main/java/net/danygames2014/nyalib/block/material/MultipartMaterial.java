package net.danygames2014.nyalib.block.material;

import net.minecraft.block.MapColor;
import net.minecraft.block.material.Material;

public class MultipartMaterial extends Material {
    public MultipartMaterial() {
        super(MapColor.GRAY);
    }

    @Override
    public boolean isReplaceable() {
        return true;
    }
}
