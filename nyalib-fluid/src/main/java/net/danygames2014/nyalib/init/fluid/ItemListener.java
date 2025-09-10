package net.danygames2014.nyalib.init.fluid;

import net.danygames2014.nyalib.block.JsonOverrideRegistry;
import net.danygames2014.nyalib.fluid.Fluid;
import net.danygames2014.nyalib.fluid.FluidRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.client.event.color.item.ItemColorsRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.template.item.TemplateBucketItem;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.HashMap;

public class ItemListener {
    public static HashMap<Item, Fluid> bucketFluids;

    @EventListener
    public void registerBuckets(ItemRegistryEvent event) {
        bucketFluids = new HashMap<>();

        for (var fluidEntry : FluidRegistry.getRegistry().entrySet()) {
            if (!fluidEntry.getValue().automaticBucketRegistrationEnabled()) {
                continue;
            }

            Identifier bucketIdentifier = fluidEntry.getKey().withSuffixedPath("_bucket");
            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
                JsonOverrideRegistry.registerItemModelOverride(bucketIdentifier, bucketItemJson);
            }
            Item bucket = new TemplateBucketItem(bucketIdentifier, fluidEntry.getValue().getBucketFluid().id).setTranslationKey(bucketIdentifier);
            fluidEntry.getValue().setBucketItem(bucket);
            bucketFluids.put(bucket, fluidEntry.getValue());
        }
    }

    @EventListener
    public void registerColorProvider(ItemColorsRegisterEvent event) {
        for (var bucketEntry : bucketFluids.entrySet()) {
            event.itemColors.register(
                    (stack, layer) -> layer == 1 ? bucketEntry.getValue().getColor() : 0xFFFFFF,
                    bucketEntry.getKey()
            );
        }

    }

    public static void registerTranslations() {
        TranslationStorage translationStorage = TranslationStorage.getInstance();
        String bucketKey = translationStorage.get("item.nyalib.fluid_bucket");

        for (var entry : bucketFluids.entrySet()) {
            Item bucket = entry.getKey();
            Fluid fluid = entry.getValue();

            translationStorage.translations.put(bucket.getTranslationKey() + ".name", translationStorage.get(bucketKey, fluid.getTranslatedName()));
        }
    }

    public String bucketItemJson = ("""
            {
              "parent": "nyalib-fluid:item/fluid_bucket"
            }"""
    );
}
