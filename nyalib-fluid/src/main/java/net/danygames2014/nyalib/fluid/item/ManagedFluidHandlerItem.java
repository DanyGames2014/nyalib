package net.danygames2014.nyalib.fluid.item;

import net.danygames2014.nyalib.fluid.Fluid;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.danygames2014.nyalib.fluid.TankManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.util.Util;

public interface ManagedFluidHandlerItem extends FluidHandlerItem {
    @Override
    default boolean canExtractFluid(ItemStack thiz) {
        return true;
    }

    @Override
    default FluidStack extractFluid(ItemStack thiz, int slot, int amount) {
        TankManager.FluidSlotEntry fluidSlot = getFluidSlot(thiz, slot);
        if (!canExtractFluid(thiz) || fluidSlot == null) {
            return null;
        }

        FluidStack currentStack = getFluid(thiz, slot);

        if (currentStack == null) {
            return null;
        }

        FluidStack extractedStack = new FluidStack(currentStack.fluid, Math.min(amount, currentStack.amount));

        currentStack.amount -= extractedStack.amount;

        if (currentStack.amount <= 0) {
            setFluid(thiz, slot, null);
        }

        return extractedStack;
    }

    @Override
    default FluidStack extractFluid(ItemStack thiz) {
        return FluidHandlerItem.super.extractFluid(thiz);
    }

    @Override
    default FluidStack extractFluid(ItemStack thiz, int amount) {
        if (!canExtractFluid(thiz)) {
            return null;
        }

        for (int i = 0; i < getFluidSlots(thiz); i++) {
            if (getFluid(thiz, i) != null) {
                return this.extractFluid(thiz, i, amount);
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
                FluidStack slotStack = getFluid(thiz, i);
                if (slotStack == null) {
                    continue;
                }
                
                if (slotStack.isFluidEqual(currentStack)) {
                    FluidStack extractedStack = extractFluid(thiz, i, remaining);
                    remaining -= extractedStack.amount;
                    currentStack.amount += extractedStack.amount;
                }
            } else {
                FluidStack extractedStack = extractFluid(thiz, i, remaining);
                if (extractedStack != null) {
                    remaining -= extractedStack.amount;
                    currentStack = extractedStack;
                }
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
        TankManager.FluidSlotEntry fluidSlot = getFluidSlot(thiz, slot);
        if (!canInsertFluid(thiz) || fluidSlot == null || stack == null || !fluidSlot.isFluidAllowed(stack.fluid)) {
            return stack;
        }

        FluidStack currentStack = getFluid(thiz, slot);
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

        setFluid(thiz, slot, currentStack);

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
        return getTankManager(thiz).getFluid(slot, null);
    }

    @Override
    default boolean setFluid(ItemStack thiz, int slot, FluidStack stack) {
        TankManager tankManager = getTankManager(thiz);
        
        boolean result = tankManager.setFluid(slot, stack, null);
        
        if (result) {
            NbtCompound managedTankNbt = new NbtCompound();
            tankManager.writeNbt(managedTankNbt);
            thiz.getStationNbt().put("ManagedTankData", managedTankNbt);
        }
        
        return result;
    }

    @Override
    default int getFluidSlots(ItemStack thiz) {
        return getTankManager(thiz).getFluidSlots(null);
    }

    @Override
    default int getFluidCapacity(ItemStack thiz, int slot) {
        return getTankManager(thiz).getFluidCapacity(slot, null);
    }

    @Override
    default int getRemainingFluidCapacity(ItemStack thiz, int slot) {
        return getTankManager(thiz).getRemainingFluidCapacity(slot, null);
    }

    @Override
    default FluidStack[] getFluids(ItemStack thiz) {
        return getTankManager(thiz).getFluids(null);
    }

    // Managed Fluid Handler
    default TankManager getTankManager(ItemStack thiz) {
        return Util.assertImpl();
    }

    default TankManager.FluidSlotEntry addFluidSlot(int capacity) {
        return Util.assertImpl();
    }
    
    default TankManager.FluidSlotEntry addFluidSlot(ItemStack thiz, int capacity) {
        return getTankManager(thiz).addSlot(capacity);
    }

    default TankManager.FluidSlotEntry getFluidSlot(ItemStack thiz, int slot) {
        return getTankManager(thiz).getSlot(slot);
    }
}
