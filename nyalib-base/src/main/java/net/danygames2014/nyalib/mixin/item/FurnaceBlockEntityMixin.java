package net.danygames2014.nyalib.mixin.item;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.item.ItemHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.recipe.FuelRegistry;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings({"AddedMixinMembersNamePattern", "DuplicatedCode"})
@Mixin(FurnaceBlockEntity.class)
public abstract class FurnaceBlockEntityMixin extends BlockEntity implements ItemHandler {
    @Shadow
    public abstract ItemStack removeStack(int slot, int amount);

    @Shadow
    public abstract ItemStack getStack(int slot);

    @Shadow
    public abstract void setStack(int slot, ItemStack stack);

    @Shadow
    public abstract int size();

    @Shadow
    private ItemStack[] inventory;

    // Slots
    // 0 - Input
    // 1 - Fuel
    // 2 - Output

    // Sides
    // Top - Input Insert / Input Extract
    // Sides - Fuel Insert / Output Extract
    // Bottom - Fuel Insert / Output Extract

    @Override
    public boolean canExtractItem(@Nullable Direction direction) {
        return true;
    }

    @Override
    public ItemStack extractItem(int amount, @Nullable Direction direction) {
        if (!canExtractItem(direction)) {
            return null;
        }
        
        if (!NyaLib.ITEM_CONFIG.simplifiedFurnaceHandling && direction != null) {
            switch (direction) {
                // Extract from UP -> Input Slot
                case UP -> {
                    if (getItemInSlot(0, direction) != null) {
                        return extractItem(0, amount, direction);
                    }
                }

                // Extract from any other side -> Output Slot
                case DOWN, NORTH, SOUTH, EAST, WEST -> {
                    if (getItemInSlot(2, direction) != null) {
                        return extractItem(2, amount, direction);
                    }
                }
            }
        }

        // If direction is null, keep super behavior
        return ItemHandler.super.extractItem(amount, direction);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, @Nullable Direction direction) {
        if (slot >= 0 && slot < inventory.length) {
            return this.removeStack(slot, amount);
        }

        return null;
    }

    @Override
    public boolean canInsertItem(@Nullable Direction direction) {
        return true;
    }

    @Override
    public ItemStack insertItem(ItemStack stack, int slot, @Nullable Direction direction) {
        // Only allow fuels into the fuel slot
        if (slot == 1 && FuelRegistry.getFuelTime(stack) <= 0) {
            return stack;
        }

        ItemStack slotStack;

        slotStack = this.getStack(slot);

        if (slotStack == null) {
            this.setStack(slot, stack);
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

        if (!NyaLib.ITEM_CONFIG.simplifiedFurnaceHandling && direction != null) {
            switch (direction) {
                case UP -> {
                    // Insert into input
                    insertedStack = insertItem(insertedStack, 0, direction);
                }
                
                case NORTH, SOUTH, EAST, WEST -> {
                    if (FuelRegistry.getFuelTime(insertedStack) >= 0) {
                        // If the item has a fuel value, insert into fuel slot
                        insertedStack = insertItem(insertedStack, 1, direction);
                    } else {
                        // If the item does not have fuel value, insert into input
                        insertedStack = insertItem(insertedStack, 0, direction);
                    }
                }
                
                case DOWN -> {
                    // Insert into fuel
                    insertedStack = insertItem(insertedStack, 1, direction);
                }
            }
        } else {
            // If direction is not specified, use default behavior
            for (int i = 0; i < this.getItemSlots(direction); ++i) {
                insertedStack = insertItem(insertedStack, i, direction);
                if (insertedStack == null) {
                    return insertedStack;
                }
            }
        }

        return insertedStack;
    }

    @Override
    public ItemStack getItemInSlot(int slot, @Nullable Direction direction) {
        return this.getStack(slot);
    }

    @Override
    public ItemStack[] getInventory(@Nullable Direction direction) {
        return this.inventory;
    }

    @Override
    public int getItemSlots(Direction direction) {
        return this.size();
    }

    @Override
    public boolean canConnectItem(Direction direction) {
        return true;
    }
}
