package net.danygames2014.nyalib.block;

import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import net.danygames2014.nyalib.fluid.Fluid;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.MapColor;
import net.minecraft.block.material.FluidMaterial;
import net.minecraft.block.material.Material;
import net.minecraft.client.resource.language.TranslationStorage;
import net.modificationstation.stationapi.api.client.event.color.item.ItemColorsRegisterEvent;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.resource.ReloadableAssetsManager;
import net.modificationstation.stationapi.api.resource.Resource;
import net.modificationstation.stationapi.api.util.Identifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Optional;

public class FluidBlockManager {
    private static final HashMap<Fluid, FluidBlockEntry> fluidBlocks = new HashMap<>();

    public static void requestBlock(Fluid fluid, Identifier stillTexture, Identifier flowingTexture, Identifier overlayTexture, MapColor mapColor, Integer blockItemColorMultiplier) {
        fluidBlocks.put(fluid, new FluidBlockEntry(fluid, stillTexture, flowingTexture, overlayTexture, mapColor, blockItemColorMultiplier));
    }

    public static void registerBlocks() {
        for (var entry : fluidBlocks.entrySet()) {
            Fluid fluid = entry.getKey();

            Material fluidMaterial = new FluidMaterial(entry.getValue().mapColor);

            // Create the Still Block and register its item model
            Identifier stillBlockIdentifier = fluid.getIdentifier().withSuffixedPath("_still");
            StillFluidBlock stillBlock = new StillFluidBlock(stillBlockIdentifier, fluidMaterial, fluid);
            fluid.setStillBlock(stillBlock);
            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
                JsonOverrideRegistry.registerItemModelOverride(stillBlockIdentifier, fluidInventoryJson);
                JsonOverrideRegistry.registerItemModelTextureOverride(stillBlockIdentifier, "layer0", entry.getValue().stillTexture);
            }

            // Create the Flowing Block and register its item model
            Identifier flowingBlockIdentifier = fluid.getIdentifier().withSuffixedPath("_flowing");
            FlowingFluidBlock flowingFluidBlock = new FlowingFluidBlock(flowingBlockIdentifier, fluidMaterial, fluid);
            fluid.setFlowingBlock(flowingFluidBlock);
            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
                JsonOverrideRegistry.registerItemModelOverride(flowingBlockIdentifier, fluidInventoryJson);
                JsonOverrideRegistry.registerItemModelTextureOverride(flowingBlockIdentifier, "layer0", entry.getValue().flowingTexture);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public static void registerTextures(TextureRegisterEvent event) {
        for (var entry : fluidBlocks.entrySet()) {
            FluidBlockEntry fluidEntry = entry.getValue();

            FluidBlockTextureHolder textureHolder = new FluidBlockTextureHolder();
            textureHolder.addStillTexture(fluidEntry.stillTexture);
            textureHolder.addFlowingTexture(fluidEntry.flowingTexture);
            fluidEntry.setTextureHolder(textureHolder);

            if (fluidEntry.overlayTexture != null) {
                IntIntImmutablePair textureSize = getTextureSize(fluidEntry.overlayTexture);
                textureHolder.addOverlayTexture(fluidEntry.overlayTexture, textureSize.leftInt(), textureSize.rightInt());
            } else {
                textureHolder.overlaySpriteId = -1;
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public static void registerColorMultipliers(ItemColorsRegisterEvent event) {
        for (var fluidEntry : fluidBlocks.entrySet()) {
            if (fluidEntry.getValue().blockItemColorMultiplier != null) {
                event.itemColors.register(
                        (stack, layer) -> fluidEntry.getValue().blockItemColorMultiplier,
                        fluidEntry.getKey().getStillBlock().asItem(), fluidEntry.getKey().getFlowingBlock().asItem()
                );
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public static IntIntImmutablePair getTextureSize(Identifier identifier) {
        Optional<Resource> resource = ReloadableAssetsManager.INSTANCE.getResource(Identifier.of("/assets/" + identifier.namespace + "/stationapi/textures/" + identifier.path + ".png"));

        if (resource.isPresent()) {
            try {
                BufferedImage image = ImageIO.read(resource.get().getInputStream());
                return new IntIntImmutablePair(image.getWidth(), image.getHeight());
            } catch (Exception ignored) {

            }
        }

        return new IntIntImmutablePair(16, 16);
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
        public Identifier overlayTexture;
        public MapColor mapColor;
        public Integer blockItemColorMultiplier;

        public FluidBlockEntry(Fluid fluid, Identifier stillTexture, Identifier flowingTexture, Identifier overlayTexture, MapColor mapColor, Integer blockItemColorMultiplier) {
            this.fluid = fluid;
            this.stillTexture = stillTexture;
            this.flowingTexture = flowingTexture;
            this.overlayTexture = overlayTexture;
            this.mapColor = mapColor;
            this.blockItemColorMultiplier = blockItemColorMultiplier;
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
