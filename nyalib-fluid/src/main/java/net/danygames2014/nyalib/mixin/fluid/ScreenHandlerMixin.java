package net.danygames2014.nyalib.mixin.fluid;

import net.danygames2014.nyalib.fluid.FluidHandler;
import net.danygames2014.nyalib.fluid.FluidSlot;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.danygames2014.nyalib.screen.FluidScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin implements FluidScreenHandler {
    @Shadow
    public abstract void sendContentUpdates();

    @Shadow
    protected List listeners;
    @Unique
    public ArrayList<FluidStack> trackedFluidStacks = new ArrayList<>();
    @Unique
    public ArrayList<FluidSlot> fluidSlots = new ArrayList<>();

    @Override
    public void addFluidSlot(FluidSlot slot) {
        slot.id = fluidSlots.size();
        fluidSlots.add(slot);
        trackedFluidStacks.add(null);
    }

    @Override
    public ArrayList<FluidSlot> getFluidSlots() {
        return fluidSlots;
    }

    @Override
    public ArrayList<FluidStack> getFluidStacks() {
        ArrayList<FluidStack> fluidStacks = new ArrayList<>();

        for (FluidSlot slot : fluidSlots) {
            fluidStacks.add(slot.getStack());
        }

        return fluidStacks;
    }

    @Inject(method = "sendContentUpdates", at = @At(value = "TAIL"))
    public void sendFluidContentUpdates(CallbackInfo ci) {
        for (int slot = 0; slot < this.fluidSlots.size(); ++slot) {
            FluidStack stack = this.fluidSlots.get(slot).getStack();
            FluidStack trackedStack = this.trackedFluidStacks.get(slot);
            if (!FluidStack.areEqual(trackedStack, stack)) {
                trackedStack = stack == null ? null : stack.copy();
                this.trackedFluidStacks.set(slot, trackedStack);

                for (Object listenerO : this.listeners) {
                    if (listenerO instanceof ScreenHandlerListener listener) {
                        listener.onFluidSlotUpdate((ScreenHandler) (Object) this, slot, trackedStack);
                    }
                }
            }
        }
    }

    @Override
    public FluidSlot getFluidSlot(int index) {
        if (index < 0 || index >= fluidSlots.size()) {
            return null;
        }
        
        return fluidSlots.get(index);
    }

    // TODO: This should probably return the cursor stack, since why would it return the fluid stack, literally not gonna get compared with client
    @Override
    public FluidStack onFluidSlotClick(int index, int button, boolean shift, PlayerEntity player, ItemStack cursorStack) {
        // TODO: DO
        return null;
    }

    @Override
    public void onFluidSlotUpdate(FluidHandler handler) {
        this.sendContentUpdates();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void setFluidStackInSlotClient(int index, FluidStack fluidStack) {
        FluidSlot slot = this.getFluidSlot(index);
        
        if (slot != null) {
            slot.setStack(fluidStack);
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void updateFluidSlotStacksClient(FluidStack[] fluidStacks) {
        for (int i = 0; i < fluidStacks.length; i++) {
            this.setFluidStackInSlotClient(i, fluidStacks[i]);
        }
    }
}
