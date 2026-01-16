package net.danygames2014.nyalib.fluid.entity;

import net.danygames2014.nyalib.fluid.Fluid;
import net.danygames2014.nyalib.fluid.FluidStack;

public interface SimpleFluidHandlerEntity extends FluidHandlerEntity {
    @Override
    default boolean canExtractFluid() {
        return true;
    }

    @Override
    default FluidStack extractFluid(int slot, int amount) {
        if (!canExtractFluid()) {
            return null;
        }

        FluidStack[] fluidStacks = getFluids();

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
    default FluidStack extractFluid() {
        if (!canExtractFluid()) {
            return null;
        }

        return extractFluid(Integer.MAX_VALUE);
    }

    @Override
    default FluidStack extractFluid(int amount) {
        if (!canExtractFluid()) {
            return null;
        }

        for (int i = 0; i < getFluidSlots(); i++) {
            if (getFluid(i) != null) {
                return this.extractFluid(i, amount);
            }
        }

        return null;
    }

    @Override
    default FluidStack extractFluid(Fluid fluid, int amount) {
        if (!canExtractFluid()) {
            return null;
        }

        FluidStack currentStack = null;
        int remaining = amount;

        for (int i = 0; i < getFluidSlots(); i++) {
            if (remaining <= 0) {
                break;
            }

            if (currentStack != null) {
                if (this.getFluid(i).isFluidEqual(currentStack)) {
                    FluidStack extractedStack = extractFluid(i, remaining);
                    remaining -= extractedStack.amount;
                    currentStack.amount += extractedStack.amount;
                }
            } else {
                FluidStack extractedStack = extractFluid(i, remaining);
                remaining -= extractedStack.amount;
                currentStack = extractedStack;
            }
        }

        return currentStack;
    }

    @Override
    default boolean canInsertFluid() {
        return true;
    }

    @Override
    default FluidStack insertFluid(FluidStack stack, int slot) {
        if (!canInsertFluid()) {
            return stack;
        }

        FluidStack[] fluidStacks = getFluids();

        FluidStack currentStack = fluidStacks[slot];
        int remaining = stack.amount;

        if (currentStack == null) {
            currentStack = new FluidStack(stack.fluid, 0);
        } else {
            if (!currentStack.isFluidEqual(stack)) {
                return stack;
            }
        }

        int addedAmount = Math.min(remaining, getFluidCapacity(slot) - currentStack.amount);
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
    default FluidStack insertFluid(FluidStack stack) {
        if (!canInsertFluid()) {
            return stack;
        }

        FluidStack insertedStack = stack.copy();
        for (int i = 0; i < getFluidSlots(); i++) {
            insertedStack = insertFluid(insertedStack, i);
            if (insertedStack == null) {
                return null;
            }
        }
        return insertedStack;
    }

    @Override
    default FluidStack getFluid(int slot) {
        if (slot >= getFluidSlots()) {
            return null;
        }

        return getFluids()[slot];
    }

    @Override
    default boolean setFluid(int slot, FluidStack stack) {
        if (slot >= getFluidSlots()) {
            return false;
        }

        getFluids()[slot] = stack;
        return true;
    }

    @Override
    default int getFluidSlots() {
        return getFluids().length;
    }

    @Override
    int getFluidCapacity(int slot);

    @Override
    default int getRemainingFluidCapacity(int slot) {
        if (slot >= getFluidSlots()) {
            return 0;
        }

        if (getFluid(slot) == null) {
            return getFluidCapacity(slot);
        }

        return getFluidCapacity(slot) - getFluid(slot).amount;
    }

    @Override
    FluidStack[] getFluids();
}
