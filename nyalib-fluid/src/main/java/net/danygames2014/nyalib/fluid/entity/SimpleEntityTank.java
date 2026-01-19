package net.danygames2014.nyalib.fluid.entity;

import net.danygames2014.nyalib.fluid.TankManager;
import net.minecraft.nbt.NbtCompound;

/**
 * An simple implementation of a tank with multiple slots based on the {@link ManagedFluidHandlerEntity}.
 * This should only be used with {@link net.minecraft.entity.Entity}
 */
public class SimpleEntityTank implements ManagedFluidHandlerEntity {
    private final TankManager tankManager;
    
    public SimpleEntityTank() {
        tankManager = new TankManager(true);
    }
    
    // Expose TankManager specific methods
    public TankManager.FluidSlotEntry addFluidSlot(int capacity) {
        return tankManager.addSlot(capacity);
    }
    
    public TankManager.FluidSlotEntry getFluidSlot(int slot) {
        return tankManager.getSlot(slot);
    }

    // Implement getting the tank manager
    @Override
    public TankManager getTankManager() {
        return tankManager;
    }
    
    // Saving and loading
    public void writeNbt(NbtCompound nbt) {
        tankManager.writeNbt(nbt);
    }
    
    public void readNbt(NbtCompound nbt) {
        tankManager.readNbt(nbt);
    }
}
