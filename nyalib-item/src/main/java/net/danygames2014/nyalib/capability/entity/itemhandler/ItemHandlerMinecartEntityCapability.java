package net.danygames2014.nyalib.capability.entity.itemhandler;

import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

class ItemHandlerMinecartEntityCapability extends ItemHandlerEntityCapability {
    private final MinecartEntity minecart;

    public ItemHandlerMinecartEntityCapability(MinecartEntity minecart) {
        this.minecart = minecart;
    }

    @Override
    public boolean canExtractItem() {
        return true;
    }

    @Override
    public ItemStack extractItem() {
        return extractItem(Integer.MAX_VALUE);
    }

    @Override
    public ItemStack extractItem(int amount) {
        for (int i = 0; i < getItemSlots(); i++) {
            if (getItem(i) != null) {
                return extractItem(i, amount);
            }
        }
        return null;
    }

    @Override
    public ItemStack extractItem(int slot, int amount) {
        if (slot >= getItemSlots() || slot < 0) {
            return null;
        }

        return minecart.removeStack(slot, amount);
    }

    @Override
    public ItemStack extractItem(Item item, int amount) {
        return extractItem(item, -1, amount);
    }

    @Override
    public ItemStack extractItem(Item item, int meta, int amount) {
        ItemStack currentStack = null;
        int remaining = amount;

        for (int i = 0; i < getItemSlots(); i++) {
            if (remaining <= 0) {
                break;
            }

            if (currentStack != null) {
                if (this.getItem(i).isItemEqual(currentStack)) {
                    ItemStack extractedStack = extractItem(i, remaining);
                    remaining -= extractedStack.count;
                    currentStack.count += extractedStack.count;
                }
            } else {
                if (getItem(i).isOf(item) && (meta == -1 || getItem(i).getDamage() == meta)) {
                    ItemStack extractedStack = extractItem(i, remaining);
                    remaining -= extractedStack.count;
                    currentStack = extractedStack;
                }
            }
        }

        return currentStack;
    }

    @Override
    public boolean canInsertItem() {
        return true;
    }

    @Override
    public ItemStack insertItem(ItemStack stack, int slot) {
        if (slot >= getItemSlots() || slot < 0) {
            return stack;
        }

        // Check if the current stack is null
        if (minecart.getStack(slot) == null) {
            if (stack.count <= minecart.getMaxCountPerStack()) {
                // If the stack fits into the slot, just set it and return null meaning the stack was inserted entirely
                minecart.setStack(slot, stack);
                return null;

            } else if (stack.count > minecart.getMaxCountPerStack()) {
                // If the stack is too large for the slot, insert a maximum size stack and then return a remainder stack
                minecart.setStack(slot, stack);

                ItemStack remainderStack = stack.copy();
                remainderStack.count = stack.count - minecart.getMaxCountPerStack();
                return remainderStack;
            }
        } else {
            // Check if the existing stack is equal to the one we are trying to insert
            ItemStack existingStack = minecart.getStack(slot);
            if (existingStack.isItemEqual(stack)) {
                int freeSpace = minecart.getMaxCountPerStack() - existingStack.count;

                // Add to the existing stack, the underlying setStack method will make sure to limit the final amount
                existingStack.count += stack.count;
                minecart.setStack(slot, existingStack);

                if (freeSpace >= stack.count) {
                    // If there is enough space to fit the inserted stack, just add to the stack and return null
                    return null;

                } else {
                    // If there isnt enough space, "fill" the stack up to maximum and return remainder
                    ItemStack remainderStack = stack.copy();
                    remainderStack.count = stack.count - freeSpace;
                    return remainderStack;
                }
            }
        }

        return stack;
    }

    @Override
    public ItemStack insertItem(ItemStack stack) {
        ItemStack insertedStack = stack.copy();

        for (int i = 0; i < minecart.size(); i++) {
            insertedStack = insertItem(insertedStack, i);

            // If its null, it was inserted entirely, no need to loop furthers
            if (insertedStack == null) {
                return null;
            }
        }

        return insertedStack;
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot >= getItemSlots() || slot < 0) {
            return null;
        }

        return minecart.getStack(slot);
    }

    @Override
    public boolean setItem(ItemStack stack, int slot) {
        if (slot >= getItemSlots() || slot < 0) {
            return false;
        }

        minecart.setStack(slot, stack);
        return true;
    }

    @Override
    public int getItemSlots() {
        return minecart.size();
    }

    @Override
    public ItemStack[] getInventory() {
        return minecart.inventory;
    }
}
