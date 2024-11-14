package net.danygames2014.nyalibtest.blockentity;

import net.danygames2014.nyalib.fluid.FluidHandler;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.minecraft.block.entity.BlockEntity;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class FluidTankBlockEntity extends BlockEntity implements FluidHandler {
    public FluidStack[] fluidStacks;

    public FluidTankBlockEntity() {
        this.fluidStacks = new FluidStack[3];
    }

    @Override
    public boolean canExtractFluid(@Nullable Direction direction) {
        return false;
    }

    @Override
    public FluidStack extractFluid(int slot, int amount, @Nullable Direction direction) {
        return null;
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
    public FluidStack getFluidInSlot(int slot, @Nullable Direction direction) {
        return fluidStacks[slot];
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
}
