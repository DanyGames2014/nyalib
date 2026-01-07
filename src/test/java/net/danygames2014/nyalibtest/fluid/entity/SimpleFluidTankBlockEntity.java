package net.danygames2014.nyalibtest.fluid.entity;

import net.danygames2014.nyalib.fluid.FluidStack;
import net.danygames2014.nyalib.fluid.SimpleFluidHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class SimpleFluidTankBlockEntity extends BlockEntity implements SimpleFluidHandler, Inventory {
    FluidStack[] fluidStacks;

    public SimpleFluidTankBlockEntity() {
        this.fluidStacks = new FluidStack[3];
    }

    @Override
    public int getFluidCapacity(int slot, @Nullable Direction direction) {
        return 11635;
    }

    @Override
    public FluidStack[] getFluids(@Nullable Direction direction) {
        return fluidStacks;
    }

    // Inventory
    @Override
    public int size() {
        return 0;
    }

    @Override
    public ItemStack getStack(int slot) {
        return null;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return null;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {

    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public int getMaxCountPerStack() {
        return 64;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }
}
