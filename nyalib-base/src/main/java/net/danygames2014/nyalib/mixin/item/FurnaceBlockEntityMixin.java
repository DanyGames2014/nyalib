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

    @Override
    public boolean canExtractItem(@Nullable Direction direction) {
        // Simplified Handling
        if (NyaLib.ITEM_CONFIG.simplifiedFurnaceHandling) {
            return true;
        }

        // Normal Hnadling
        if (direction == null) {
            return false;
        }

        return direction != Direction.DOWN && direction != Direction.UP;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, @Nullable Direction direction) {
        // Simplified Handling
        if (NyaLib.ITEM_CONFIG.simplifiedFurnaceHandling) {
            return this.removeStack(slot, amount);
        }

        // Normal Handling
        if (canExtractItem(direction) && slot == 2) {
            return this.removeStack(slot, amount);
        } else {
            return null;
        }
    }

    @Override
    public boolean canInsertItem(@Nullable Direction direction) {
        // Simplified Handling
        if (NyaLib.ITEM_CONFIG.simplifiedFurnaceHandling) {
            return true;
        }

        // Normal Handling
        if (direction == null) {
            return false;
        }

        return direction == Direction.DOWN || direction == Direction.UP;
    }

    @Override
    public ItemStack insertItem(ItemStack stack, int slot, @Nullable Direction direction) {
        if (!NyaLib.ITEM_CONFIG.simplifiedFurnaceHandling) {
            if (direction == null) {
                return stack;
            }

            // Disallow inserting into Input from Bottom
            if (slot == 0 && direction == Direction.DOWN) {
                return stack;
            }

            // Disallow inserting Fuel from Top
            if (slot == 1 && direction == Direction.UP) {
                return stack;
            }
        }

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
