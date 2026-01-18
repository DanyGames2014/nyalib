package net.danygames2014.nyalib.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public interface SlotLockingItem {
    boolean shouldLockSlot(ItemStack stack, int slotIndex, Slot slot, PlayerEntity player);
}
