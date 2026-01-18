package net.danygames2014.nyalibtest.screen.slot;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class LockingSlot extends Slot {
    public LockingSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean isLocked(int index, ItemStack stackInSlot, PlayerEntity player) {
        return stackInSlot != null && stackInSlot.getItem() == Block.DIRT.asItem();
    }
}
