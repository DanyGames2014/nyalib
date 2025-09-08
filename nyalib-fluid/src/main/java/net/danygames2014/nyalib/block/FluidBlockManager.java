package net.danygames2014.nyalib.block;

import net.danygames2014.nyalib.fluid.Fluid;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.HashMap;

public class FluidBlockManager {
    private static final HashMap<Fluid, FluidBlockEntry> fluidBlocks = new HashMap<>();

    public static void requestBlock(Fluid fluid, Identifier stillTexture, Identifier flowingTexture) {
        fluidBlocks.put(fluid, new FluidBlockEntry(fluid, stillTexture, flowingTexture));
    }

    public static void registerBlocks() {
        for (var entry : fluidBlocks.entrySet()) {
              Fluid fluid = entry.getKey();
              
              // Create the blocks
              StillFluidBlock stillBlock = new StillFluidBlock(fluid.getIdentifier().withSuffixedPath("_still"), Material.WATER, fluid);
              fluid.setStillBlock(stillBlock);
              FlowingFluidBlock flowingFluidBlock = new FlowingFluidBlock(fluid.getIdentifier().withSuffixedPath("_flowing"), Material.WATER, fluid);
              fluid.setFlowing(flowingFluidBlock);
        }
    }

    public static void registerTextures(TextureRegisterEvent event) {
        for (var entry : fluidBlocks.entrySet()) {
            FluidBlockEntry fluidEntry = entry.getValue();
            
            FluidBlockTextureHolder textureHolder = new FluidBlockTextureHolder();
            textureHolder.addStillTexture(fluidEntry.stillTexture);
            textureHolder.addFlowingTexture(fluidEntry.flowingTexture);
            
            fluidEntry.setTextureHolder(textureHolder);
        }
    }

    public static class FluidBlockEntry {
        public Fluid fluid;
        public FluidBlockTextureHolder textureHolder;
        public Identifier stillTexture;
        public Identifier flowingTexture;

        public FluidBlockEntry(Fluid fluid, Identifier stillTexture, Identifier flowingTexture) {
            this.fluid = fluid;
            this.stillTexture = stillTexture;
            this.flowingTexture = flowingTexture;
        }
        
        public void setTextureHolder(FluidBlockTextureHolder textureHolder) {
            this.textureHolder = textureHolder;
            
            if (fluid.getStillBlock() instanceof StillFluidBlock stillFluidBlock) {
                stillFluidBlock.textureHolder = textureHolder;
            }
            
            if (fluid.getFlowingBlock() instanceof FlowingFluidBlock flowingFluidBlock) {
                flowingFluidBlock.textureHolder = textureHolder;
            }
        }
    }
}
