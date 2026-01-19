package net.danygames2014.nyalibtest.item;

import net.danygames2014.nyalib.item.block.ManagedItemHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class ManagedInventoryBlockEntity extends BlockEntity implements ManagedItemHandler, Inventory {
    public ManagedInventoryBlockEntity() {
        for (int i = 0; i < 27; i++) {
            this.addItemSlot();
        }
    }

    @Override
    public int size() {
        return this.getItemSlots(null);
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.getItem(slot, null);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return this.extractItem(slot, amount, null);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.setItem(stack, slot, null);
    }

    @Override
    public String getName() {
        return "Managed Inventory";
    }

    @Override
    public int getMaxCountPerStack() {
        return 64;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }
}
