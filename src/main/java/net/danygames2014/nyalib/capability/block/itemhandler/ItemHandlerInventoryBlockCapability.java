package net.danygames2014.nyalib.capability.block.itemhandler;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("DuplicatedCode")
public class ItemHandlerInventoryBlockCapability extends ItemHandlerBlockCapability {
    private final Inventory inventory;
    
    public ItemHandlerInventoryBlockCapability(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public boolean canConnectItem(Direction direction) {
        return true;
    }

    @Override
    public boolean canExtractItem(@Nullable Direction direction) {
        return true;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, @Nullable Direction direction) {
        return inventory.removeStack(slot, amount);
    }

    @Override
    public boolean canInsertItem(@Nullable Direction direction) {
        return true;
    }

    @Override
    public ItemStack insertItem(ItemStack stack, int slot, @Nullable Direction direction) {
        ItemStack slotStack;

        slotStack = inventory.getStack(slot);

        if (slotStack == null) {
            inventory.setStack(slot, stack);
            return null;
        }

        if (slotStack.isItemEqual(stack)) {
            int addedCount = Math.min(slotStack.getItem().getMaxCount() - slotStack.count, stack.count);

            slotStack.count += addedCount;

            if (addedCount >= stack.count) {
                return null;
            } else {
                return new ItemStack(stack.getItem(), stack.count - addedCount, stack.getDamage());
            }
        }

        return stack;
    }

    @Override
    public ItemStack insertItem(ItemStack stack, @Nullable Direction direction) {
        ItemStack insertedStack = stack.copy();

        for (int i = 0; i < this.getItemSlots(direction); ++i) {
            insertedStack = insertItem(insertedStack, i, direction);
            if (insertedStack == null) {
                return insertedStack;
            }
        }

        return insertedStack;
    }

    @Override
    public ItemStack getItemInSlot(int slot, @Nullable Direction direction) {
        return inventory.getStack(slot);
    }

    @Override
    public int getItemSlots(@Nullable Direction direction) {
        return inventory.size();
    }

    @Override
    public ItemStack[] getInventory(@Nullable Direction direction) {
        ItemStack[] stacks = new ItemStack[inventory.size()];
        
        for (int i = 0; i < inventory.size(); i++) {
            stacks[i] = inventory.getStack(i);
        }
        
        return stacks;
    }
}
