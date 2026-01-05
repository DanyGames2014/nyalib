package net.danygames2014.nyalib.fluid;

import net.minecraft.block.MapColor;
import net.minecraft.block.material.FluidMaterial;

public class Fluids {
    public static Fluid WATER;
    public static Fluid LAVA;

    /**
     * A default fluid material used when a fluid doesn't have a material
     */
    public static FluidMaterial DEFAULT_FLUID_MATERIAL = new FluidMaterial(MapColor.GRAY);
}
