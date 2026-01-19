package net.danygames2014.nyalib.fluid.block;

import net.danygames2014.nyalib.fluid.TankManager;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.util.math.Direction;

/**
 * An simple implementation of a tank with multiple slots based on the {@link ManagedFluidHandler}.
 * This should only be used with {@link net.minecraft.block.entity.BlockEntity} as it does take directions into account
 */
public class SimpleTank implements ManagedFluidHandler {
    private final TankManager tankManager;
    
    public SimpleTank() {
        tankManager = new TankManager();
    }
    
    // Expose TankManager specific methods
    public TankManager.FluidSlotEntry addFluidSlot(int capacity) {
        return tankManager.addSlot(capacity);
    }
    
    public TankManager.FluidSlotEntry getFluidSlot(int slot, Direction direction) {
        return tankManager.getSlot(slot, direction);
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
