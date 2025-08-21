package net.danygames2014.nyalib.capability.entity.itemhandler;

import net.danygames2014.nyalib.capability.entity.EntityCapability;
import net.danygames2014.nyalib.item.ItemHandlerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class ItemHandlerEntityCapability extends EntityCapability implements ItemHandlerEntity {
    public abstract boolean canExtractItem();

    public abstract ItemStack extractItem(int slot, int amount);

    public abstract ItemStack extractItem();

    public abstract ItemStack extractItem(int amount);

    public abstract ItemStack extractItem(Item item, int amount);

    public abstract boolean canInsertItem();

    public abstract ItemStack insertItem(ItemStack stack, int slot);

    public abstract ItemStack insertItem(ItemStack stack);

    public abstract ItemStack getItem(int slot);
    
    public abstract boolean setItem(ItemStack stack, int slot);

    public abstract int getItemSlots();

    public abstract ItemStack[] getInventory();
}
