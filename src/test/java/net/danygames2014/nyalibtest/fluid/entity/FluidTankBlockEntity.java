package net.danygames2014.nyalibtest.fluid.entity;

import net.danygames2014.nyalib.fluid.FluidHandler;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class FluidTankBlockEntity extends BlockEntity implements FluidHandler, Inventory {
    public FluidStack[] fluidStacks;

    public FluidTankBlockEntity() {
        this.fluidStacks = new FluidStack[3];
    }

    @Override
    public boolean canExtractFluid(@Nullable Direction direction) {
        return true;
    }

    @Override
    public FluidStack extractFluid(int slot, int amount, @Nullable Direction direction) {
        if (fluidStacks[slot] == null) {
            return null;
        }
        
        FluidStack currentStack = fluidStacks[slot];
        FluidStack extractedStack = new FluidStack(currentStack.fluid, Math.min(amount, currentStack.amount));
        
        currentStack.amount -= extractedStack.amount;

        if (currentStack.amount <= 0) {
            fluidStacks[slot] = null;
        }
        
        return extractedStack;
    }

    @Override
    public boolean canInsertFluid(@Nullable Direction direction) {
        return true;
    }

    @Override
    public FluidStack insertFluid(FluidStack stack, int slot, @Nullable Direction direction) {
        FluidStack currentStack = fluidStacks[slot];
        int remaining = stack.amount;

        if (currentStack == null) {
            currentStack = new FluidStack(stack.fluid, 0);
        } else {
            if (!currentStack.isFluidEqual(stack)) {
                return stack;
            }
        }

        int addedAmount = Math.min(remaining, getFluidCapacity(slot, direction) - currentStack.amount);
        currentStack.amount += addedAmount;
        remaining -= addedAmount;

        fluidStacks[slot] = currentStack;
        
        if (remaining > 0) {
            stack.amount -= remaining;
            return new FluidStack(stack.fluid, remaining);
        }

        return null;
    }

    @Override
    public FluidStack insertFluid(FluidStack stack, @Nullable Direction direction) {
        FluidStack insertedStack = stack.copy();
        for (int i = 0; i < getFluidSlots(direction); i++) {
            insertedStack = insertFluid(insertedStack, i, direction);
            if (insertedStack == null) {
                return null;
            }
        }
        return insertedStack;
    }

    @Override
    public FluidStack getFluid(int slot, @Nullable Direction direction) {
        return fluidStacks[slot];
    }

    @Override
    public boolean setFluid(int slot, FluidStack stack, @Nullable Direction direction) {
        fluidStacks[slot] = stack;
        return true;
    }

    @Override
    public int getFluidSlots(@Nullable Direction direction) {
        return fluidStacks.length;
    }

    @Override
    public int getFluidCapacity(int slot, @Nullable Direction direction) {
        if (slot < fluidStacks.length && slot >= 0) {
            return 10000;
        }

        return 0;
    }

    @Override
    public FluidStack[] getFluids(@Nullable Direction direction) {
        return fluidStacks;
    }

    @Override
    public boolean canConnectFluid(Direction direction) {
        return true;
    }

    // Inventory
    @Override
    public int size() {
        return 0;
    }

    @Override
    public ItemStack getStack(int slot) {
        return null;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return null;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {

    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public int getMaxCountPerStack() {
        return 64;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }
}
