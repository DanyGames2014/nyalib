package net.danygames2014.nyalib.init.fluid;

import net.danygames2014.nyalib.block.JsonOverrideRegistry;
import net.danygames2014.nyalib.fluid.Fluid;
import net.danygames2014.nyalib.fluid.FluidRegistry;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.client.event.color.item.ItemColorsRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.template.item.TemplateBucketItem;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.HashMap;

public class ItemListener {
    HashMap<Item, Fluid> bucketFluids;

    @EventListener
    public void registerBuckets(ItemRegistryEvent event) {
        bucketFluids = new HashMap<>();

        for (var fluidEntry : FluidRegistry.getRegistry().entrySet()) {
            if (!fluidEntry.getValue().automaticBucketRegistrationEnabled()) {
                continue;
            }

            Identifier bucketIdentifier = fluidEntry.getKey().withSuffixedPath("_bucket");
            JsonOverrideRegistry.registerItemModelOverride(bucketIdentifier, bucketItemJson);
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

    public String bucketItemJson = ("""
            {
              "parent": "nyalib-fluid:item/fluid_bucket"
            }"""
    );
}
