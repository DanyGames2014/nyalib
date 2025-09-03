package net.danygames2014.nyalib.screen;

import net.danygames2014.nyalib.fluid.FluidStack;
import net.minecraft.screen.ScreenHandler;
import net.modificationstation.stationapi.api.util.Util;

import java.util.ArrayList;

public interface FluidScreenHandlerListener {
    default void onFluidSlotUpdate(ScreenHandler handler, int slot, FluidStack stack) {
        Util.assertImpl();
    }
    
    default void onFluidContentsUpdate(ScreenHandler handler, ArrayList<FluidStack> stacks) {
        Util.assertImpl();
    }
}
