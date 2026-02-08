package net.danygames2014.nyalib.mixin.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.danygames2014.nyalib.item.HasCraftingReturnStack;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CraftingResultSlot.class)
public class CraftingResultSlotMixin {
    @WrapOperation(method = "onTakeItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;hasCraftingReturnItem()Z"))
    public boolean foolPreviousCheck(Item item, Operation<Boolean> original) {
        if (item instanceof HasCraftingReturnStack) {
            return true;
        }
        
        return original.call(item);
    }
    
    @WrapOperation(method = "onTakeItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;setStack(ILnet/minecraft/item/ItemStack;)V"))
    public void injectResultItem(Inventory inventory, int slot, ItemStack stack, Operation<Void> original, @Local(ordinal = 1) ItemStack resultStack) {
        if (resultStack.getItem() instanceof HasCraftingReturnStack hasReturnStack) {
            inventory.setStack(slot, hasReturnStack.getCraftingReturnStack(resultStack));
            return;
        }
        
        original.call(inventory, slot, stack);
    }
}
