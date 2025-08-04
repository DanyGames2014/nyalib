package net.danygames2014.nyalib.capability.item.itemhandler;

import net.danygames2014.nyalib.capability.item.ItemCapability;
import net.danygames2014.nyalib.item.ItemHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public abstract class ItemHandlerItemCapability extends ItemCapability {
    public abstract boolean canExtractItem();

    public abstract ItemStack extractItem(int slot, int amount);

    public abstract ItemStack extractItem();

    public abstract ItemStack extractItem(Item item, int amount);

    public abstract boolean canInsertItem();

    public abstract ItemStack insertItem(ItemStack stack, int slot);

    public abstract ItemStack insertItem(ItemStack stack);

    public abstract ItemStack getItemInSlot(int slot);

    public abstract int getItemSlots();

    public abstract ItemStack[] getInventory();
}
