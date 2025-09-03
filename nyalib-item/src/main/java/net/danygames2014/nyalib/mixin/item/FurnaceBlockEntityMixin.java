package net.danygames2014.nyalib.mixin.item;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.item.ItemHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.item.Item;
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
    public boolean canExtractItem(@Nullable Direction side) {
        return true;
    }

    @Override
    public ItemStack extractItem(int amount, @Nullable Direction side) {
        if (!canExtractItem(side)) {
            return null;
        }
        
        if (!NyaLib.ITEM_CONFIG.simplifiedFurnaceHandling && side != null) {
            switch (side) {
                // Extract from UP -> Input Slot
                case UP -> {
                    if (getItem(0, side) != null) {
                        return extractItem(0, amount, side);
                    }
                }

                // Extract from any other side -> Output Slot
                case DOWN, NORTH, SOUTH, EAST, WEST -> {
                    if (getItem(2, side) != null) {
                        return extractItem(2, amount, side);
                    }
                }
            }
        }

        // If direction is null, keep super behavior
        return ItemHandler.super.extractItem(amount, side);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, @Nullable Direction side) {
        if (slot >= 0 && slot < inventory.length) {
            return this.removeStack(slot, amount);
        }
        
        return null;
    }

    @Override
    public ItemStack extractItem(Item item, int meta, int amount, @Nullable Direction side) {
        if (!NyaLib.ITEM_CONFIG.simplifiedFurnaceHandling && side != null) {
            switch (side) {
                // Extract from UP -> Input Slot
                case UP -> {
                    if (getItem(0, side) != null && getItem(0, side).itemId == item.id && (meta == -1 || getItem(0, side).getDamage() == meta)) {
                        return extractItem(0, amount, side);
                    }
                }

                // Extract from any other side -> Output Slot
                case DOWN, NORTH, SOUTH, EAST, WEST -> {
                    if (getItem(2, side) != null && getItem(2, side).itemId == item.id && (meta == -1 || getItem(2, side).getDamage() == meta)) {
                        return extractItem(2, amount, side);
                    }
                }
            }
            
            return null;
        }

        // If direction is null, keep super behavior
        return ItemHandler.super.extractItem(item, amount, side);
    }

    @Override
    public boolean canInsertItem(@Nullable Direction side) {
        return true;
    }

    @Override
    public ItemStack insertItem(ItemStack stack, int slot, @Nullable Direction side) {
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
    public ItemStack insertItem(ItemStack stack, @Nullable Direction side) {
        ItemStack insertedStack = stack.copy();

        if (!NyaLib.ITEM_CONFIG.simplifiedFurnaceHandling && side != null) {
            switch (side) {
                case UP -> {
                    // Insert into input
                    insertedStack = insertItem(insertedStack, 0, side);
                }
                
                case NORTH, SOUTH, EAST, WEST -> {
                    if (FuelRegistry.getFuelTime(insertedStack) >= 0) {
                        // If the item has a fuel value, insert into fuel slot
                        insertedStack = insertItem(insertedStack, 1, side);
                    } else {
                        // If the item does not have fuel value, insert into input
                        insertedStack = insertItem(insertedStack, 0, side);
                    }
                }
                
                case DOWN -> {
                    // Insert into fuel
                    insertedStack = insertItem(insertedStack, 1, side);
                }
            }
        } else {
            // If direction is not specified, use default behavior
            for (int i = 0; i < this.getItemSlots(side); ++i) {
                insertedStack = insertItem(insertedStack, i, side);
                if (insertedStack == null) {
                    return insertedStack;
                }
            }
        }

        return insertedStack;
    }

    @Override
    public ItemStack getItem(int slot, @Nullable Direction side) {
        return this.getStack(slot);
    }

    @Override
    public ItemStack[] getInventory(@Nullable Direction side) {
        return this.inventory;
    }

    @Override
    public int getItemSlots(Direction side) {
        return this.size();
    }

    @Override
    public boolean canConnectItem(Direction side) {
        return true;
    }
}
