package net.danygames2014.nyalib.capability.block.itemhandler;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("DuplicatedCode")
class ItemHandlerInventoryBlockCapability extends ItemHandlerBlockCapability {
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
        if (slot >= getItemSlots(direction) || slot < 0) {
            return null;
        }

        return inventory.removeStack(slot, amount);
    }

    @Override
    public ItemStack extractItem(@Nullable Direction direction) {
        return extractItem(Integer.MAX_VALUE, direction);
    }

    @Override
    public ItemStack extractItem(Item item, int amount, @Nullable Direction direction) {
        return extractItem(item, -1, amount, direction);
    }

    @Override
    public ItemStack extractItem(Item item, int meta, int amount, @Nullable Direction direction) {
        ItemStack currentStack = null;
        int remaining = amount;

        for (int i = 0; i < getItemSlots(direction); i++) {
            if (remaining <= 0) {
                break;
            }

            if (currentStack != null) {
                if (this.getItem(i, direction).isItemEqual(currentStack)) {
                    ItemStack extractedStack = extractItem(i, remaining, direction);
                    remaining -= extractedStack.count;
                    currentStack.count += extractedStack.count;
                }
            } else {
                if (getItem(i, direction).isOf(item) && (meta == -1 || getItem(i, direction).getDamage() == meta)) {
                    ItemStack extractedStack = extractItem(i, remaining, direction);
                    remaining -= extractedStack.count;
                    currentStack = extractedStack;
                }
            }
        }

        return currentStack;
    }

    @Override
    public ItemStack extractItem(int amount, @Nullable Direction direction) {
        for (int i = 0; i < getItemSlots(direction); i++) {
            if (getItem(i, direction) != null) {
                return extractItem(i, amount, direction);
            }
        }
        return null;
    }

    @Override
    public boolean canInsertItem(@Nullable Direction direction) {
        return true;
    }

    @Override
    public ItemStack insertItem(ItemStack stack, int slot, @Nullable Direction direction) {
        if (slot >= getItemSlots(direction) || slot < 0) {
            return stack;
        }

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
    public ItemStack getItem(int slot, @Nullable Direction direction) {
        if (slot >= getItemSlots(direction) || slot < 0) {
            return null;
        }
        
        return inventory.getStack(slot);
    }

    @Override
    public boolean setItem(ItemStack stack, int slot, @Nullable Direction direction) {
        if (slot >= getItemSlots(direction) || slot < 0) {
            return false;
        }

        inventory.setStack(slot, stack);
        return true;
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
