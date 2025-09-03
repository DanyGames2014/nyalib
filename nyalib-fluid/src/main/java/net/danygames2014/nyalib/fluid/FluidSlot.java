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
    
    // Slot Content
    public boolean canInsert(FluidStack fluidStack) {
        return true;
    }

    public FluidStack getStack() {
        return handler.getFluid(index, null);
    }

    public boolean hasStack() {
        return getStack() != null;
    }

    public void setStack(FluidStack fluidStack) {
        handler.setFluid(index, fluidStack, null);
        markDirty();
    }

    public FluidStack takeStack(int amount) {
        return handler.extractFluid(index, amount, null);
    }
    
    public int getMaxFluidAmount() {
        return handler.getFluidCapacity(index, null);
    }
    
    public void markDirty() {
        // TODO: hmmm oh oh Oh Oh OH OH
        //this.handler.markDirty(); OH OH
    }
    
    public boolean equals(FluidHandler otherHandler, int otherIndex) {
        return otherHandler == this.handler && otherIndex == this.index;
    }
    
    // Events
    public void onTakeFluid(FluidStack fluidStack) {
        markDirty();
    }
    
    // Rendering
    @Environment(EnvType.CLIENT)
    public int getBackgroundTextureId() {
        return -1;
    }
}
