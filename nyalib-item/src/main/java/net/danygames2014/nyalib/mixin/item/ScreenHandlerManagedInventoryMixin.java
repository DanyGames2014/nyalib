package net.danygames2014.nyalib.mixin.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.danygames2014.nyalib.item.InventoryManager;
import net.danygames2014.nyalib.item.block.ManagedItemHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ScreenHandler.class)
public class ScreenHandlerManagedInventoryMixin {
    @WrapOperation(method = "onSlotClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;canInsert(Lnet/minecraft/item/ItemStack;)Z"))
    public boolean checkSlotLockedOnInsert(Slot slot, ItemStack cursorStack, Operation<Boolean> original, @Local(ordinal = 0, argsOnly = true) PlayerEntity player, @Local(ordinal = 0, argsOnly = true) int index) {
        if (slot.inventory instanceof ManagedItemHandler managedItemHandler) {
            InventoryManager manager = managedItemHandler.getInventoryManager();
            if (!manager.getSlot(slot.index).isItemAllowed(cursorStack.getItem())) {
                return false;
            }
        }

        return original.call(slot, cursorStack);
    }
}
