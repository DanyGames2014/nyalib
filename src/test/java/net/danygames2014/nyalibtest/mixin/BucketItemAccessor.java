package net.danygames2014.nyalibtest.mixin;

import net.minecraft.item.BucketItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BucketItem.class)
public interface BucketItemAccessor {
    @Accessor(value = "fluidBlockId")
    int getFluidBlockId();
}
