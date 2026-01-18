package net.danygames2014.nyalib.mixin.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.danygames2014.nyalib.item.SlotLockingItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {
    @SuppressWarnings("rawtypes")
    @Shadow
    public List slots;

    @Inject(method = "onSlotClick", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"), cancellable = true)
    public void checkSlotLockedOnTake(int index, int button, boolean shift, PlayerEntity player, CallbackInfoReturnable<ItemStack> cir, @Local(ordinal = 0) ItemStack returnStack) {
        Slot slot = (Slot) this.slots.get(index);
        ItemStack slotStack = slot.getStack();
        
        if (slot.isLocked(index, slotStack, player) || (slotStack != null && slotStack.getItem() instanceof SlotLockingItem slotLockingItem && slotLockingItem.shouldLockSlot(slotStack, index, slot, player))) {
            cir.setReturnValue(returnStack);
            cir.cancel();
        }
    }
    
    @WrapOperation(method = "onSlotClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;canInsert(Lnet/minecraft/item/ItemStack;)Z"))
    public boolean checkSlotLockedOnInsert(Slot slot, ItemStack stack, Operation<Boolean> original, @Local(ordinal = 0, argsOnly = true) PlayerEntity player, @Local(ordinal = 0, argsOnly = true) int index) {
        ItemStack slotStack = slot.getStack();
        
        if (slot.isLocked(index, slotStack, player) || (slotStack != null && slotStack.getItem() instanceof SlotLockingItem slotLockingItem && slotLockingItem.shouldLockSlot(slotStack, index, slot, player))) {
            return false;
        }
        
        return original.call(slot, stack);
    }
}
