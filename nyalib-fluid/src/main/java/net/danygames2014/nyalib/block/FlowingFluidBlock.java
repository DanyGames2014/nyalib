package net.danygames2014.nyalib.block;

import net.danygames2014.nyalib.fluid.Fluid;
import net.minecraft.block.material.Material;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.template.block.TemplateFlowingLiquidBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class FlowingFluidBlock extends TemplateFlowingLiquidBlock {
    public final Fluid fluid;
    public FluidBlockTextureHolder textureHolder;
    
    public FlowingFluidBlock(Identifier identifier, Material material, Fluid fluid) {
        super(identifier, material);
        this.fluid = fluid;
        this.setTranslationKey(identifier);
        this.setLuminance(this::getLightLevel);
    }

    // Tick Rate
    @Override
    public int getTickRate() {
        return fluid.getTickRate();
    }

    // Light Level
    public int getLightLevel(BlockState state) {
        return fluid.getLightLevel(state);
    }

    // Rendering
    @Override
    public int getTexture(int side, int meta) {
        if (textureHolder == null) {
            return 0;
        }
        
        if ((meta != 0 || side != 0) && side != 1) {
            return textureHolder.getFlowingTextureId();
        }

        return textureHolder.getStillTextureId();
    }

    public int getTexture(int side) {
        if (textureHolder == null) {
            return 0;
        }

        return textureHolder.getStillTextureId();
    }

    @Override
    public int getColorMultiplier(BlockView blockView, int x, int y, int z) {
        return fluid.getColorMultiplier(blockView, x, y, z);
    }
}
