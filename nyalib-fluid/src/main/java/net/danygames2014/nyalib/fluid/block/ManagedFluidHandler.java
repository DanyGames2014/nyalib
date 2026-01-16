package net.danygames2014.nyalib.fluid.block;

import net.danygames2014.nyalib.fluid.Fluid;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.danygames2014.nyalib.fluid.TankManager;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public interface ManagedFluidHandler extends FluidHandler {
    @Override
    default boolean canExtractFluid(@Nullable Direction direction) {
        return true;
    }

    @Override
    default FluidStack extractFluid(int slot, int amount, @Nullable Direction direction) {
        if (!canExtractFluid(direction) || !getTankManager().getSlot(slot, direction).isSideAllowed(direction)) {
            return null;
        }

        FluidStack currentStack = getFluid(slot, direction);
        
        if (currentStack == null) {
            return null;
        }

        FluidStack extractedStack = new FluidStack(currentStack.fluid, Math.min(amount, currentStack.amount));

        currentStack.amount -= extractedStack.amount;

        if (currentStack.amount <= 0) {
            setFluid(slot, null, direction);
        }

        return extractedStack;
    }

    @Override
    default FluidStack extractFluid(@Nullable Direction direction) {
        return extractFluid(Integer.MAX_VALUE, direction);
    }

    @Override
    default FluidStack extractFluid(int amount, @Nullable Direction direction) {
        if (!canExtractFluid(direction)) {
            return null;
        }
        
        for (int i = 0; i < getFluidSlots(null); i++) {
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

        for (int i = 0; i < getFluidSlots(null); i++) {
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
        if (!canInsertFluid(direction) || !getTankManager().getSlot(0, direction).isSideAllowed(direction) || !getSlot(slot, direction).isFluidAllowed(stack.fluid)) {
            return stack;
        }
        
        FluidStack currentStack = getFluid(slot, direction);
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

        setFluid(slot, currentStack, direction);

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
        for (int i = 0; i < getFluidSlots(null); i++) {
            insertedStack = insertFluid(insertedStack, i, direction);
            if (insertedStack == null) {
                return null;
            }
        }
        return insertedStack;
    }

    @Override
    default FluidStack getFluid(int slot, @Nullable Direction direction) {
        return getTankManager().getFluid(slot, direction);
    }
    
    @Override
    default boolean setFluid(int slot, FluidStack stack, @Nullable Direction direction) {
        return getTankManager().setFluid(slot, stack, direction);
    }

    @Override
    default int getFluidSlots(@Nullable Direction direction) {
        return getTankManager().getFluidSlots(direction);
    }

    @Override
    default int getFluidCapacity(int slot, @Nullable Direction direction) {
        return getTankManager().getFluidCapacity(slot, direction);
    }

    @Override
    default int getRemainingFluidCapacity(int slot, @Nullable Direction direction) {
        return getTankManager().getRemainingFluidCapacity(slot, direction);
    }

    @Override
    default FluidStack[] getFluids(@Nullable Direction direction) {
        return getTankManager().getFluids(direction);
    }

    // FluidCapable
    @Override
    default boolean canConnectFluid(Direction direction) {
        return true;
    }

    // Managed Fluid Handler
    default TankManager getTankManager() {
        return Util.assertImpl();
    }
    
    default TankManager.FluidSlotEntry addSlot(int capacity) {
        return getTankManager().addSlot(capacity);
    }

    default TankManager.FluidSlotEntry getSlot(int slot, @Nullable Direction direction) {
        return getTankManager().getSlot(slot, direction);
    }
}
