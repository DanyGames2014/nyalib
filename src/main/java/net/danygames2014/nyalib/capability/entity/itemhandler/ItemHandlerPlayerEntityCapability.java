package net.danygames2014.nyalib.capability.entity.itemhandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class ItemHandlerPlayerEntityCapability extends ItemHandlerEntityCapability {
    PlayerEntity player;

    public ItemHandlerPlayerEntityCapability(PlayerEntity player) {
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
        return true;
    }

    @Override
    public ItemStack insertItem(ItemStack stack, int slot) {
        // Check if the current stack is null
        if (player.inventory.getStack(slot) == null) {
            if (stack.count <= player.inventory.getMaxCountPerStack()) {
                // If the stack fits into the slot, just set it and return null meaning the stack was inserted entirely
                player.inventory.setStack(slot, stack);
                return null;

            } else if (stack.count > player.inventory.getMaxCountPerStack()) {
                // If the stack is too large for the slot, insert a maximum size stack and then return a remainder stack
                player.inventory.setStack(slot, stack);

                ItemStack remainderStack = stack.copy();
                remainderStack.count = stack.count - player.inventory.getMaxCountPerStack();
                return remainderStack;
            }
        } else {
            // Check if the existing stack is equal to the one we are trying to insert
            ItemStack existingStack = player.inventory.getStack(slot);
            if (existingStack.isItemEqual(stack)) {
                int freeSpace = player.inventory.getMaxCountPerStack() - existingStack.count;

                // Add to the existing stack, the underlying setStack method will make sure to limit the final amount
                existingStack.count += stack.count;
                player.inventory.setStack(slot, existingStack);

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

        for (int i = 0; i < player.inventory.size(); i++) {
            insertedStack = insertItem(insertedStack, i);

            // If its null, it was inserted entirely, no need to loop furthers
            if (insertedStack == null) {
                return null;
            }
        }

        return insertedStack;
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
        ArrayList<ItemStack> inventory = new ArrayList<ItemStack>(player.inventory.size());
        
        for (int i = 0; i < player.inventory.size(); i++) {
            inventory.add(player.inventory.getStack(i));
        }
        
        return inventory.toArray(new ItemStack[0]);
    }
}
