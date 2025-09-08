package net.danygames2014.nyalib.block;

import net.danygames2014.nyalib.fluid.Fluid;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.template.block.TemplateStillLiquidBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class StillFluidBlock extends TemplateStillLiquidBlock {
    public final Fluid fluid;
    public FluidBlockTextureHolder textureHolder;
    
    public StillFluidBlock(Identifier identifier, Material material, Fluid fluid) {
        super(identifier, material);
        this.fluid = fluid;
    }

    public int getTexture(int side) {
        if (textureHolder == null) {
            return 0;
        }

        return textureHolder.getFlowingTextureId();
    }
}
