package net.danygames2014.nyalibtest.block.fluid.entity;

import net.danygames2014.nyalib.fluid.Fluids;
import net.danygames2014.nyalib.fluid.FluidHandler;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.minecraft.block.entity.BlockEntity;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class InfiniteWaterBlockEntity extends BlockEntity implements FluidHandler {
    @Override
    public void tick() {
        for (Direction dir : Direction.values()) {
            BlockEntity blockEntity = world.getBlockEntity(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ());
            if (blockEntity instanceof FluidHandler fluidHandler) {
                if (fluidHandler.canInsertFluid(dir.getOpposite())) {
                    fluidHandler.insertFluid(new FluidStack(Fluids.WATER, 10), dir.getOpposite());
                }
            }
        }
    }

    @Override
    public boolean canExtractFluid(@Nullable Direction direction) {
        return true;
    }

    @Override
    public FluidStack extractFluid(int slot, int amount, @Nullable Direction direction) {
        if (slot <= getFluidSlots(direction)) {
            return new FluidStack(Fluids.WATER, amount);
        }
        return null;
    }

    @Override
    public boolean canInsertFluid(@Nullable Direction direction) {
        return false;
    }

    @Override
    public FluidStack insertFluid(FluidStack stack, int slot, @Nullable Direction direction) {
        return null;
    }

    @Override
    public FluidStack insertFluid(FluidStack stack, @Nullable Direction direction) {
        return null;
    }

    @Override
    public FluidStack getFluidInSlot(int slot, @Nullable Direction direction) {
        if (slot <= getFluidSlots(direction)) {
            return new FluidStack(Fluids.WATER, Integer.MAX_VALUE);
        }
        return null;
    }

    @Override
    public int getFluidSlots(@Nullable Direction direction) {
        return 1;
    }

    @Override
    public int getFluidCapacity(int slot, @Nullable Direction direction) {
        return Integer.MAX_VALUE;
    }

    @Override
    public FluidStack[] getFluids(@Nullable Direction direction) {
        return new FluidStack[]{new FluidStack(Fluids.WATER, Integer.MAX_VALUE)};
    }

    @Override
    public boolean canConnectFluid(Direction direction) {
        return false;
    }
}
