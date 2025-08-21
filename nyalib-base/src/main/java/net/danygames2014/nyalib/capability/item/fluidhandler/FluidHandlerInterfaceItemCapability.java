package net.danygames2014.nyalib.capability.item.fluidhandler;

import net.danygames2014.nyalib.capability.entity.fluidhandler.FluidHandlerEntityCapability;
import net.danygames2014.nyalib.fluid.Fluid;
import net.danygames2014.nyalib.fluid.FluidHandlerItem;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.minecraft.item.ItemStack;

public class FluidHandlerInterfaceItemCapability extends FluidHandlerItemCapability {
    private final FluidHandlerItem fluidHandler;
    private final ItemStack itemStack;

    public FluidHandlerInterfaceItemCapability(FluidHandlerItem fluidHandler, ItemStack itemStack) {
        this.fluidHandler = fluidHandler;
        this.itemStack = itemStack;
    }

    @Override
    public boolean canExtractFluid() {
        return fluidHandler.canExtractFluid(itemStack);
    }

    @Override
    public FluidStack extractFluid(int slot, int amount) {
        return fluidHandler.extractFluid(itemStack, slot, amount);
    }

    @Override
    public FluidStack extractFluid(Fluid fluid, int amount) {
        return fluidHandler.extractFluid(itemStack, fluid, amount);
    }

    @Override
    public FluidStack extractFluid(int amount) {
        return fluidHandler.extractFluid(itemStack, amount);
    }

    @Override
    public FluidStack extractFluid() {
        return fluidHandler.extractFluid(itemStack);
    }

    @Override
    public boolean canInsertFluid() {
        return fluidHandler.canInsertFluid(itemStack);
    }

    @Override
    public FluidStack insertFluid(FluidStack stack, int slot) {
        return fluidHandler.insertFluid(itemStack, stack, slot);
    }

    @Override
    public FluidStack insertFluid(FluidStack stack) {
        return fluidHandler.insertFluid(itemStack, stack);
    }

    @Override
    public FluidStack getFluid(int slot) {
        return fluidHandler.getFluid(itemStack, slot);
    }

    @Override
    public boolean setFluid(int slot, FluidStack stack) {
        return fluidHandler.setFluid(itemStack, slot, stack);
    }

    @Override
    public int getFluidSlots() {
        return fluidHandler.getFluidSlots(itemStack);
    }

    @Override
    public int getFluidCapacity(int slot) {
        return fluidHandler.getFluidCapacity(itemStack, slot);
    }

    @Override
    public int getRemainingFluidCapacity(int slot) {
        return fluidHandler.getRemainingFluidCapacity(itemStack, slot);
    }

    @Override
    public FluidStack[] getFluids() {
        return fluidHandler.getFluids(itemStack);
    }
}
