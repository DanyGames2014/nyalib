package net.danygames2014.nyalibtest.fluid.entity;

import net.danygames2014.nyalib.fluid.Fluids;
import net.danygames2014.nyalib.fluid.ManagedFluidHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.math.Direction;

public class ManagedFluidTankBlockEntity extends BlockEntity implements ManagedFluidHandler, Inventory {
    public ManagedFluidTankBlockEntity() {
        this.addSlot(2000).setAllowedSides(Direction.UP, Direction.NORTH).setAllowedFluids(Fluids.WATER);
        this.addSlot(18000);
        this.addSlot(6877).setAllowedFluids(Fluids.MILK);
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
