package net.danygames2014.nyalibtest.mixin;

import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BucketItem.class)
public class BucketItemMixin extends Item {
    public BucketItemMixin(int id) {
        super(id);
    }

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void increaseStackSize(int id, int fluidBlockId, CallbackInfo ci) {
        if (fluidBlockId == 0) {
            this.maxCount = 16;
        }
    }
}
