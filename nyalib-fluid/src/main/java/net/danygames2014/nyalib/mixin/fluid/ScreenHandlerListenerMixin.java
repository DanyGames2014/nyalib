package net.danygames2014.nyalib.mixin.fluid;

import net.danygames2014.nyalib.fluid.FluidStack;
import net.danygames2014.nyalib.screen.FluidScreenHandlerListener;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(ScreenHandlerListener.class)
public interface ScreenHandlerListenerMixin extends FluidScreenHandlerListener {
    @Override
    void onFluidSlotUpdate(ScreenHandler handler, int slot, FluidStack stack);
}
