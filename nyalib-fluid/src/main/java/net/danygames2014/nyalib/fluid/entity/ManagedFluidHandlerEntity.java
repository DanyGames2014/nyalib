package net.danygames2014.nyalib.fluid.entity;

import net.danygames2014.nyalib.fluid.Fluid;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.danygames2014.nyalib.fluid.TankManager;
import net.modificationstation.stationapi.api.util.Util;

public interface ManagedFluidHandlerEntity extends FluidHandlerEntity {
    @Override
    default boolean canExtractFluid() {
        return true;
        // TODO: IDK actually do logic based on theavailibity of slots? dingus, this aint a self-serve checkout
    }

    @Override
    default FluidStack extractFluid(int slot, int amount) {
        TankManager.FluidSlotEntry fluidSlot = getFluidSlot(slot);
        if (!canExtractFluid() || fluidSlot == null) {
            return null;
        }

        FluidStack currentStack = getFluid(slot);

        if (currentStack == null) {
            return null;
        }

        FluidStack extractedStack = new FluidStack(currentStack.fluid, Math.min(amount, currentStack.amount));

        currentStack.amount -= extractedStack.amount;

        if (currentStack.amount <= 0) {
            setFluid(slot, null);
        }

        return extractedStack;
    }

    @Override
    default FluidStack extractFluid() {
        return FluidHandlerEntity.super.extractFluid();
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
                FluidStack slotStack = getFluid(i);
                if (slotStack == null) {
                    continue;
                }
                
                if (slotStack.isFluidEqual(currentStack)) {
                    FluidStack extractedStack = extractFluid(i, remaining);
                    remaining -= extractedStack.amount;
                    currentStack.amount += extractedStack.amount;
                }
            } else {
                FluidStack extractedStack = extractFluid(i, remaining);
                if (extractedStack != null) {
                    remaining -= extractedStack.amount;
                    currentStack = extractedStack;
                }
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
        TankManager.FluidSlotEntry fluidSlot = getFluidSlot(slot);
        if (!canInsertFluid() || fluidSlot == null || stack == null || !fluidSlot.isFluidAllowed(stack.fluid)) {
            return stack;
        }

        FluidStack currentStack = getFluid(slot);
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

        setFluid(slot, currentStack);

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
        return getTankManager().getFluid(slot, null);
    }

    @Override
    default boolean setFluid(int slot, FluidStack stack) {
        return getTankManager().setFluid(slot, stack, null);
    }

    @Override
    default int getFluidSlots() {
        return getTankManager().getFluidSlots(null);
    }

    @Override
    default int getFluidCapacity(int slot) {
        return getTankManager().getFluidCapacity(slot, null);
    }

    @Override
    default int getRemainingFluidCapacity(int slot) {
        return getTankManager().getRemainingFluidCapacity(slot, null);
    }

    @Override
    default FluidStack[] getFluids() {
        return getTankManager().getFluids(null);
    }

    // Managed Fluid Handler
    default TankManager getTankManager() {
        return Util.assertImpl();
    }

    default TankManager.FluidSlotEntry addFluidSlot(int capacity) {
        return getTankManager().addSlot(capacity);
    }

    default TankManager.FluidSlotEntry getFluidSlot(int slot) {
        return getTankManager().getSlot(slot);
    }
}
