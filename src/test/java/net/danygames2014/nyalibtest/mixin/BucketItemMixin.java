package net.danygames2014.nyalibtest.mixin;

import net.danygames2014.nyalib.fluid.Fluid;
import net.danygames2014.nyalib.fluid.FluidBucket;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BucketItem.class)
public class BucketItemMixin extends Item implements CustomTooltipProvider {
    public BucketItemMixin(int id) {
        super(id);
    }

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void increaseStackSize(int id, int fluidBlockId, CallbackInfo ci) {
        if (fluidBlockId == 0) {
            this.maxCount = 16;
        }
    }

    @Override
    public @NotNull String[] getTooltip(ItemStack stack, String originalTooltip) {
        String fluidName = "Empty";
        
        if (stack.getItem() instanceof FluidBucket fluidBucket) {
            Fluid fluid = fluidBucket.getFluid();
            if (fluid != null) {
                fluidName = fluid.getTranslatedName();
            }
        }
        
        return new String[] {
                originalTooltip,
                "Contents: " + fluidName
        };
    }
}
