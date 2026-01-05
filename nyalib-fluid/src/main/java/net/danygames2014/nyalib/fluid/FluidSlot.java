package net.danygames2014.nyalib.fluid;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class FluidSlot {
    private final int index;
    private final FluidHandler handler;
    public int id;
    public int x;
    public int y;

    // TODO: Allow the slot to not be drawn, useful for custom representation of the fluid contents
    public FluidSlot(FluidHandler handler, int index, int x, int y) {
        this.handler = handler;
        this.index = index;
        this.x = x;
        this.y = y;
    }

    // Manipulating the FluidStack itself
    public FluidStack getStack() {
        return handler.getFluid(index, null);
    }

    public boolean hasStack() {
        return getStack() != null;
    }

    public void setStack(FluidStack fluidStack) {
        handler.setFluid(index, fluidStack, null);
    }

    public FluidStack takeStack(int amount) {
        return handler.extractFluid(index, amount, null);
    }

    // Fluid Amount
    public int getFluidAmount() {
        return handler.getFluid(index, null).amount;
    }

    public void setFluidAmount(int amount) {
        handler.getFluid(index, null).amount = Math.max(0, Math.min(getMaxFluidAmount(), amount));
    }

    public int getMaxFluidAmount() {
        return handler.getFluidCapacity(index, null);
    }

    public FluidHandler getHandler() {
        return handler;
    }

    // Slot Rules
    public boolean canInsert(FluidStack fluidStack) {
        return true;
    }

    // Comparable
    public boolean equals(FluidHandler otherHandler, int otherIndex) {
        return otherHandler == this.handler && otherIndex == this.index;
    }

    // Rendering
    @Environment(EnvType.CLIENT)
    public int getBackgroundTextureId() {
        return -1;
    }
}
