package net.danygames2014.nyalib.mixin.item;

import net.danygames2014.nyalib.item.ItemHandler;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings({"AddedMixinMembersNamePattern", "DuplicatedCode"})
@Mixin(ChestBlockEntity.class)
public abstract class ChestBlockEntityMixin extends BlockEntity implements ItemHandler {

    @Shadow
    public abstract ItemStack getStack(int slot);

    @Shadow
    public abstract ItemStack removeStack(int slot, int amount);

    @Shadow
    public abstract int size();

    @Shadow
    public abstract void setStack(int slot, ItemStack stack);


    // API Methods

    @Shadow private ItemStack[] inventory;

    @Override
    public boolean canExtractItem(@Nullable Direction direction) {
        return true;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, @Nullable Direction direction) {
        if (slot > 26 && isDoubleChest()) {
            return getSecondChest().removeStack((slot - 27), amount);
        }

        return this.removeStack(slot, amount);
    }

    @Override
    public boolean canInsertItem(@Nullable Direction direction) {
        return true;
    }

    @Override
    public ItemStack insertItem(ItemStack stack, int slot, @Nullable Direction direction) {
        ItemStack slotStack;

        if (slot > 26 && isDoubleChest()) {
            slotStack = getSecondChest().getStack(slot - 27);
        } else {
            slotStack = this.getStack(slot);
        }


        if (slotStack == null) {
            if (slot > 26 && isDoubleChest()) {
                getSecondChest().setStack(slot - 27, stack);
            } else {
                this.setStack(slot, stack);
            }
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
        if (slot > 26 && isDoubleChest()) {
            return getSecondChest().getStack(slot - 27);
        }

        return this.getStack(slot);
    }

    @Override
    public int getItemSlots(Direction direction) {
        if (isDoubleChest()) {
            return this.size() + getSecondChest().size();
        }

        return this.size();
    }

    @Override
    public ItemStack[] getInventory(@Nullable Direction direction) {
        return this.inventory;
    }

    // Double Chest Helper Methods
    @Unique
    public ChestBlockEntity getSecondChest() {
        BlockPos chestPos = null;

        // Find the chest
        if (isChestAtPos(x + 1, y, z)) {
            chestPos = new BlockPos(x + 1, y, z);
        }

        if (isChestAtPos(x - 1, y, z)) {
            chestPos = new BlockPos(x - 1, y, z);
        }

        if (isChestAtPos(x, y, z + 1)) {
            chestPos = new BlockPos(x, y, z + 1);
        }

        if (isChestAtPos(x, y, z - 1)) {
            chestPos = new BlockPos(x, y, z - 1);
        }

        // If no chest return null
        if (chestPos == null) {
            return null;
        }

        // Return the Item Handler for second chest
        if (world.getBlockEntity(chestPos.x, chestPos.y, chestPos.z) instanceof ChestBlockEntity chestEntity) {
            return chestEntity;
        }

        return null;
    }

    @Unique
    public boolean isDoubleChest() {
        return isChestAtPos(x + 1, y, z) || isChestAtPos(x - 1, y, z) || isChestAtPos(x, y, z + 1) || isChestAtPos(x, y, z - 1);
    }

    @Unique
    public boolean isChestAtPos(int x, int y, int z) {
        return world.getBlockId(x, y, z) == Block.CHEST.id;
    }

    @Override
    public boolean canConnectItem(Direction direction) {
        return true;
    }
}
