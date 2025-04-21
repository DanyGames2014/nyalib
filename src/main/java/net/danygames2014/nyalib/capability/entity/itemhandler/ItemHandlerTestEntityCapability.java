package net.danygames2014.nyalib.capability.entity.itemhandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.Arrays;

public class ItemHandlerTestEntityCapability extends ItemHandlerEntityCapability{
    PlayerEntity player;

    public ItemHandlerTestEntityCapability(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public boolean canExtractItem() {
        return true;
    }

    @Override
    public ItemStack extractItem(int slot, int amount) {
        return player.inventory.removeStack(slot, amount);
    }

    @Override
    public boolean canInsertItem() {
        return false;
    }

    @Override
    public ItemStack insertItem(ItemStack stack, int slot) {
        return stack;
    }

    @Override
    public ItemStack insertItem(ItemStack stack) {
        return stack;
    }

    @Override
    public ItemStack getItemInSlot(int slot) {
        return player.inventory.getStack(slot);
    }

    @Override
    public int getItemSlots() {
        return player.inventory.size();
    }

    @Override
    public ItemStack[] getInventory() {
        return player.inventory.main;
    }
}
