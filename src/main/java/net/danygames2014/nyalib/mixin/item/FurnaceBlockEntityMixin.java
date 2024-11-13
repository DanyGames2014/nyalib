package net.danygames2014.nyalib.mixin.item;

import net.danygames2014.nyalib.item.ItemHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.item.ItemStack;
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

    @Shadow private ItemStack[] inventory;

    @Override
    public boolean canExtractItem(@Nullable Direction direction) {
//        if (direction == null) {
//            return false;
//        }
//
//        return direction != Direction.DOWN && direction != Direction.UP;
        return true;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, @Nullable Direction direction) {
        return this.removeStack(slot, amount);
    }

    @Override
    public boolean canInsertItem(@Nullable Direction direction) {
//        if (direction == null) {
//            return false;
//        }
//
//        return direction == Direction.DOWN || direction == Direction.UP;
        return true;
    }

    @Override
    public ItemStack insertItem(ItemStack stack, int slot, @Nullable Direction direction) {
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

        for (int i = 0; i < this.getSize(direction); ++i) {
            insertedStack = insertItem(insertedStack, i, direction);
            if (insertedStack == null) {
                return insertedStack;
            }
        }

        return insertedStack;
    }

    @Override
    public ItemStack getStackInSlot(int slot, @Nullable Direction direction) {
        return this.getStack(slot);
    }

    @Override
    public ItemStack[] getInventory(@Nullable Direction direction) {
        return this.inventory;
    }

    @Override
    public int getSize(Direction direction) {
        return this.size();
    }
}
