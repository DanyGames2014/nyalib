package net.danygames2014.nyalib.fluid;

import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public interface SimpleFluidHandler extends FluidHandler {
    @Override
    default boolean canExtractFluid(@Nullable Direction direction) {
        return true;
    }

    @Override
    default FluidStack extractFluid(int slot, int amount, @Nullable Direction direction) {
        if (!canExtractFluid(direction)) {
            return null;
        }
        
        FluidStack[] fluidStacks = getFluids(direction);
        
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
    default FluidStack extractFluid(@Nullable Direction direction) {
        if (!canExtractFluid(direction)) {
            return null;
        }
        
        return extractFluid(Integer.MAX_VALUE, direction);
    }

    @Override
    default FluidStack extractFluid(int amount, @Nullable Direction direction) {
        if (!canExtractFluid(direction)) {
            return null;
        }
        
        for (int i = 0; i < getFluidSlots(direction); i++) {
            if (getFluid(i, direction) != null) {
                return this.extractFluid(i, amount, direction);
            }
        }
        
        return null;
    }

    @Override
    default FluidStack extractFluid(Fluid fluid, int amount, @Nullable Direction direction) {
        if (!canExtractFluid(direction)) {
            return null;
        }
        
        FluidStack currentStack = null;
        int remaining = amount;

        for (int i = 0; i < getFluidSlots(direction); i++) {
            if (remaining <= 0) {
                break;
            }

            if (currentStack != null) {
                if (this.getFluid(i, direction).isFluidEqual(currentStack)) {
                    FluidStack extractedStack = extractFluid(i, remaining, direction);
                    remaining -= extractedStack.amount;
                    currentStack.amount += extractedStack.amount;
                }
            } else {
                FluidStack extractedStack = extractFluid(i, remaining, direction);
                remaining -= extractedStack.amount;
                currentStack = extractedStack;
            }
        }

        return currentStack;
    }

    @Override
    default boolean canInsertFluid(@Nullable Direction direction) {
        return true;
    }

    @Override
    default FluidStack insertFluid(FluidStack stack, int slot, @Nullable Direction direction) {
        if (!canInsertFluid(direction)) {
            return stack;
        }
        
        FluidStack[] fluidStacks = getFluids(direction);
        
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
    default FluidStack insertFluid(FluidStack stack, @Nullable Direction direction) {
        if (!canInsertFluid(direction)) {
            return stack;
        }
        
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
    default FluidStack getFluid(int slot, @Nullable Direction direction) {
        if (slot >= getFluidSlots(direction)) {
            return null;
        }
        
        return getFluids(direction)[slot];
    }

    @Override
    default boolean setFluid(int slot, FluidStack stack, @Nullable Direction direction) {
        if (slot >= getFluidSlots(direction)) {
            return false;
        }

        getFluids(direction)[slot] = stack;
        return true;
    }

    @Override
    default int getFluidSlots(@Nullable Direction direction) {
        return getFluids(direction).length;
    }

    @Override
    int getFluidCapacity(int slot, @Nullable Direction direction);

    @Override
    default int getRemainingFluidCapacity(int slot, @Nullable Direction direction) {
        if (slot >= getFluidSlots(direction)) {
            return 0;
        }

        if (getFluid(slot, direction) == null) {
            return getFluidCapacity(slot, direction);
        }

        return getFluidCapacity(slot, direction) - getFluid(slot, direction).amount;
    }

    @Override
    FluidStack[] getFluids(@Nullable Direction direction);

    default TankManager getTankManager() {
        return Util.assertImpl();
    }
    
    @Override
    default boolean canConnectFluid(Direction direction) {
        return true;
    }
}
