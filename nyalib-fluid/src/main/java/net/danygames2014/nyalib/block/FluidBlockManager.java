package net.danygames2014.nyalib.block;

import net.danygames2014.nyalib.fluid.Fluid;
import net.minecraft.block.material.Material;
import net.minecraft.client.resource.language.TranslationStorage;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
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

            // Create the Still Block and register its item model
            Identifier stillBlockIdentifier = fluid.getIdentifier().withSuffixedPath("_still");
            StillFluidBlock stillBlock = new StillFluidBlock(stillBlockIdentifier, Material.WATER, fluid);
            fluid.setStillBlock(stillBlock);
            JsonOverrideRegistry.registerItemModelOverride(stillBlockIdentifier, fluidInventoryJson);
            JsonOverrideRegistry.registerItemModelTextureOverride(stillBlockIdentifier, "layer0", entry.getValue().stillTexture);

            // Create the Flowing Block and register its item model
            Identifier flowingBlockIdentifier = fluid.getIdentifier().withSuffixedPath("_flowing");
            FlowingFluidBlock flowingFluidBlock = new FlowingFluidBlock(flowingBlockIdentifier, Material.WATER, fluid);
            fluid.setFlowing(flowingFluidBlock);
            JsonOverrideRegistry.registerItemModelOverride(flowingBlockIdentifier, fluidInventoryJson);
            JsonOverrideRegistry.registerItemModelTextureOverride(flowingBlockIdentifier, "layer0", entry.getValue().flowingTexture);
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

    public static void registerTranslations() {
        TranslationStorage translationStorage = TranslationStorage.getInstance();
        String stillFluid = translationStorage.get("tile.fluid.nyalib.still");
        String flowingFluid = translationStorage.get("tile.fluid.nyalib.Flowing");

        for (var entry : fluidBlocks.entrySet()) {
            Fluid fluid = entry.getKey();

            translationStorage.translations.put(fluid.getStillBlock().getTranslationKey() + ".name", translationStorage.get(stillFluid, fluid.getTranslatedName()));
            translationStorage.translations.put(fluid.getFlowingBlock().getTranslationKey() + ".name", translationStorage.get(flowingFluid, fluid.getTranslatedName()));
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
                stillFluidBlock.textureId = textureHolder.getStillTextureId();
            }

            if (fluid.getFlowingBlock() instanceof FlowingFluidBlock flowingFluidBlock) {
                flowingFluidBlock.textureHolder = textureHolder;
                flowingFluidBlock.textureId = textureHolder.getFlowingTextureId();
            }
        }
    }

    public static final String fluidInventoryJson = ("""
            {
              "parent": "minecraft:item/generated",
              "textures": {
              }
            }"""
    );
}
