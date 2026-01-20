package net.danygames2014.nyalib.mixin.fluid;

import com.llamalad7.mixinextras.sugar.Local;
import net.danygames2014.nyalib.fluid.item.ManagedFluidHandlerItem;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerInventory.class, priority = 1100)
public class PlayerInventoryMixin {
    @Shadow
    public ItemStack[] main;

    @Inject(
            method = "combineStacks",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;<init>(III)V",
                    shift = At.Shift.BY,
                    by = 2
            )
    )
    private void nyalib_transferManagedTankData(ItemStack stack, CallbackInfoReturnable<Integer> cir, @Local(ordinal = 2) int var4) {
        ItemStack newStack = this.main[var4];
        
        if (newStack != null && newStack.getItem() instanceof ManagedFluidHandlerItem managedFluidHandlerItem && newStack.getStationNbt() != null && newStack.getStationNbt().contains("ManagedTankData")) {
            managedFluidHandlerItem.getTankManager(newStack).readNbt(stack.getStationNbt().getCompound("ManagedTankData"));
        }
    }
}
