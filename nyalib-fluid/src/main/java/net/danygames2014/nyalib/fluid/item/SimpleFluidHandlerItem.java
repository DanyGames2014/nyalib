package net.danygames2014.nyalib.fluid.item;

import net.danygames2014.nyalib.fluid.Fluid;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.minecraft.item.ItemStack;

public interface SimpleFluidHandlerItem extends FluidHandlerItem {
    @Override
    default boolean canExtractFluid(ItemStack thiz) {
        return true;
    }

    @Override
    default FluidStack extractFluid(ItemStack thiz, int slot, int amount) {
        if (!canExtractFluid(thiz)) {
            return null;
        }

        FluidStack[] fluidStacks = getFluids(thiz);

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
    default FluidStack extractFluid(ItemStack thiz) {
        if (!canExtractFluid(thiz)) {
            return null;
        }

        return extractFluid(thiz, Integer.MAX_VALUE);
    }

    @Override
    default FluidStack extractFluid(ItemStack thiz, int amount) {
        if (!canExtractFluid(thiz)) {
            return null;
        }

        for (int i = 0; i < getFluidSlots(thiz); i++) {
            if (getFluid(thiz, i) != null) {
                return this.extractFluid(thiz,i, amount);
            }
        }

        return null;
    }

    @Override
    default FluidStack extractFluid(ItemStack thiz, Fluid fluid, int amount) {
        if (!canExtractFluid(thiz)) {
            return null;
        }

        FluidStack currentStack = null;
        int remaining = amount;

        for (int i = 0; i < getFluidSlots(thiz); i++) {
            if (remaining <= 0) {
                break;
            }

            if (currentStack != null) {
                if (this.getFluid(thiz, i).isFluidEqual(currentStack)) {
                    FluidStack extractedStack = extractFluid(thiz, i, remaining);
                    remaining -= extractedStack.amount;
                    currentStack.amount += extractedStack.amount;
                }
            } else {
                FluidStack extractedStack = extractFluid(thiz, i, remaining);
                remaining -= extractedStack.amount;
                currentStack = extractedStack;
            }
        }

        return currentStack;
    }

    @Override
    default boolean canInsertFluid(ItemStack thiz) {
        return true;
    }

    @Override
    default FluidStack insertFluid(ItemStack thiz, FluidStack stack, int slot) {
        if (!canInsertFluid(thiz)) {
            return stack;
        }

        FluidStack[] fluidStacks = getFluids(thiz);

        FluidStack currentStack = fluidStacks[slot];
        int remaining = stack.amount;

        if (currentStack == null) {
            currentStack = new FluidStack(stack.fluid, 0);
        } else {
            if (!currentStack.isFluidEqual(stack)) {
                return stack;
            }
        }

        int addedAmount = Math.min(remaining, getFluidCapacity(thiz, slot) - currentStack.amount);
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
    default FluidStack insertFluid(ItemStack thiz, FluidStack stack) {
        if (!canInsertFluid(thiz)) {
            return stack;
        }

        FluidStack insertedStack = stack.copy();
        for (int i = 0; i < getFluidSlots(thiz); i++) {
            insertedStack = insertFluid(thiz, insertedStack, i);
            if (insertedStack == null) {
                return null;
            }
        }
        return insertedStack;
    }

    @Override
    default FluidStack getFluid(ItemStack thiz, int slot) {
        if (slot >= getFluidSlots(thiz)) {
            return null;
        }

        return getFluids(thiz)[slot];
    }

    @Override
    default boolean setFluid(ItemStack thiz, int slot, FluidStack stack) {
        if (slot >= getFluidSlots(thiz)) {
            return false;
        }

        getFluids(thiz)[slot] = stack;
        return true;
    }

    @Override
    default int getFluidSlots(ItemStack thiz) {
        return getFluids(thiz).length;
    }

    @Override
    int getFluidCapacity(ItemStack thiz, int slot);

    @Override
    default int getRemainingFluidCapacity(ItemStack thiz, int slot) {
        if (slot >= getFluidSlots(thiz)) {
            return 0;
        }

        if (getFluid(thiz, slot) == null) {
            return getFluidCapacity(thiz, slot);
        }

        return getFluidCapacity(thiz, slot) - getFluid(thiz, slot).amount;
    }

    @Override
    FluidStack[] getFluids(ItemStack thiz);
}
