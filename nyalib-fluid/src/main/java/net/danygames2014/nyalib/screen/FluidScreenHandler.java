package net.danygames2014.nyalib.screen;

import net.danygames2014.nyalib.fluid.FluidHandler;
import net.danygames2014.nyalib.fluid.FluidSlot;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.Util;

import java.util.ArrayList;

public interface FluidScreenHandler {
    default void addFluidSlot(FluidSlot slot) {
        Util.assertImpl();
    }

    default ArrayList<FluidStack> getFluidStacks() {
        return Util.assertImpl();
    }

    default ArrayList<FluidSlot> getFluidSlots() {
        return Util.assertImpl();
    }

    default FluidSlot getFluidSlot(int index) {
        return Util.assertImpl();
    }

    default FluidStack onFluidSlotClick(int index, int button, boolean shift, PlayerEntity player, ItemStack cursorStack) {
        return Util.assertImpl();
    }

    default void onFluidSlotUpdate(FluidHandler handler) {
        Util.assertImpl();
    }

    @Environment(EnvType.CLIENT)
    default void setFluidStackInSlotClient(int index, FluidStack fluidStack) {
        Util.assertImpl();
    }

    @Environment(EnvType.CLIENT)
    default void updateFluidSlotStacksClient(FluidStack[] fluidStacks) {
        Util.assertImpl();
    }
}
