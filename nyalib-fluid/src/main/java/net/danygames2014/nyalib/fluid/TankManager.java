package net.danygames2014.nyalib.fluid;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class TankManager {
    private FluidSlotEntry[] fluidSlotEntries;
    private final boolean forbidSideChecks;

    public TankManager(boolean forbidSideChecks) {
        fluidSlotEntries = new FluidSlotEntry[0];
        this.forbidSideChecks = forbidSideChecks;
    }

    public TankManager() {
        this(false);
    }

    /**
     * @param capacity The capacity of this slot
     * @return The entry for further chaining.
     */
    public FluidSlotEntry addSlot(int capacity) {
        FluidSlotEntry entry = new FluidSlotEntry(capacity);
        entry.forbidSideChecks = forbidSideChecks;

        fluidSlotEntries = Arrays.copyOf(fluidSlotEntries, fluidSlotEntries.length + 1);
        fluidSlotEntries[fluidSlotEntries.length - 1] = entry;

        return entry;
    }
    
    public void addSlot(FluidSlotEntry entry) {
        fluidSlotEntries = Arrays.copyOf(fluidSlotEntries, fluidSlotEntries.length + 1);
        fluidSlotEntries[fluidSlotEntries.length - 1] = entry;
    }

    public FluidSlotEntry getSlot(int slot) {
        return getSlot(slot, null);
    }
    
    public FluidSlotEntry getSlot(int slot, @Nullable Direction side) {
        if (slot > fluidSlotEntries.length - 1 || slot < 0) {
            return null;
        }

        if (!fluidSlotEntries[slot].isSideAllowed(side)) {
            return null;
        }

        return fluidSlotEntries[slot];
    }

    // FluidHandler methods
    public FluidStack getFluid(int slot, @Nullable Direction side) {
        FluidSlotEntry entry = getSlot(slot, side);

        if (entry == null) {
            return null;
        }

        return entry.stack;
    }

    public boolean setFluid(int slot, FluidStack stack, @Nullable Direction side) {
        FluidSlotEntry entry = getSlot(slot, side);

        if (entry == null) {
            return false;
        }

        entry.stack = stack;
        return true;
    }

    public int getFluidSlots(@Nullable Direction side) {
        int slotCount = 0;

        for (FluidSlotEntry fluidSlotEntry : fluidSlotEntries) {
            if (fluidSlotEntry.isSideAllowed(side)) {
                slotCount++;
            }
        }

        return slotCount;
    }

    public FluidStack[] getFluids(@Nullable Direction side) {
        ObjectArrayList<FluidStack> stacks = new ObjectArrayList<>();

        for (FluidSlotEntry fluidSlotEntry : fluidSlotEntries) {
            if (fluidSlotEntry.isSideAllowed(side)) {
                stacks.add(fluidSlotEntry.stack);
            } else {
                stacks.add(null);
            }
        }

        return stacks.toArray(FluidStack[]::new);
    }

    public int getFluidCapacity(int slot, @Nullable Direction side) {
        FluidSlotEntry entry = getSlot(slot, side);

        if (entry == null) {
            return 0;
        }

        return entry.capacity;
    }

    public int getRemainingFluidCapacity(int slot, @Nullable Direction side) {
        FluidSlotEntry entry = getSlot(slot, side);

        if (entry == null) {
            return 0;
        }

        if (entry.stack == null) {
            return entry.capacity;
        }

        return entry.capacity - entry.stack.amount;
    }

    // NBT
    public void writeNbt(NbtCompound nbt) {
        NbtList entriesList = new NbtList();

        for (int slot = 0; slot < fluidSlotEntries.length; slot++) {
            NbtCompound entryNbt = new NbtCompound();
            entryNbt.putInt("Slot", slot);
            fluidSlotEntries[slot].writeNbt(entryNbt);
            entriesList.add(entryNbt);
        }

        nbt.put("FluidSlots", entriesList);
    }

    public void readNbt(NbtCompound nbt) {
        NbtList entriesList = nbt.getList("FluidSlots");

        for (int i = 0; i < entriesList.size(); i++) {
            NbtCompound entryNbt = (NbtCompound) entriesList.get(i);
            if (entryNbt.getInt("Slot") < fluidSlotEntries.length) {
                fluidSlotEntries[entryNbt.getInt("Slot")].readNbt(entryNbt);
            }
        }
    }

    @SuppressWarnings({"UnusedReturnValue"})
    public static class FluidSlotEntry {
        public ObjectArrayList<Fluid> allowedFluids;
        public ObjectArrayList<Direction> allowedSides;
        public int capacity;

        public FluidStack stack;
        
        public boolean forbidSideChecks = false;

        public FluidSlotEntry(int capacity) {
            this(capacity, new Fluid[0]);
        }

        public FluidSlotEntry(int capacity, Fluid... allowedFluids) {
            this.capacity = capacity;
            if (allowedFluids.length > 0) {
                this.allowedFluids = new ObjectArrayList<>(allowedFluids);
            }
        }

        public FluidSlotEntry setAllowedSides(Direction... allowedSides) {
            if (forbidSideChecks) {
                throw new UnsupportedOperationException("Setting allowed sides is not applicable for this TankManager.");
            }
            
            this.allowedSides = new ObjectArrayList<>(allowedSides);
            return this;
        }

        public FluidSlotEntry setAllowedFluids(Fluid... allowedFluids) {
            this.allowedFluids = new ObjectArrayList<>(allowedFluids);
            return this;
        }

        // Allow checks
        public boolean isFluidAllowed(Fluid fluid) {
            return allowedFluids == null || allowedFluids.contains(fluid);
        }

        public boolean isSideAllowed(@Nullable Direction side) {
            return side == null || allowedSides == null || allowedSides.contains(side);
        }

        // NBT
        public void writeNbt(NbtCompound nbt) {
            if (stack != null) {
                nbt.put("FluidStack", stack.writeNbt(new NbtCompound()));
            }
        }

        public void readNbt(NbtCompound nbt) {
            if (nbt.contains("FluidStack")) {
                stack = new FluidStack(nbt.getCompound("FluidStack"));
                stack.amount = Math.min(stack.amount, capacity);
            }
        }

        public FluidSlotEntry copy(boolean withStack) {
            FluidSlotEntry entry = new FluidSlotEntry(capacity);
            entry.allowedFluids = allowedFluids == null ? null : new ObjectArrayList<>(allowedFluids);
            entry.allowedSides = allowedSides == null ? null : new ObjectArrayList<>(allowedSides);
            
            if (withStack) {
                entry.stack = stack == null ? null : stack.copy();
            }
            
            return entry;
        }
    }
}
