package net.danygames2014.nyalib.mixin.fluid;

import net.danygames2014.nyalib.client.FluidInteractionManager;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.minecraft.client.InteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(InteractionManager.class)
public class InteractionManagerMixin implements FluidInteractionManager {
    @Override
    public FluidStack clickFluidSlot(int syncId, int slotId, int button, boolean shift, PlayerEntity player) {
        return player.currentScreenHandler.onFluidSlotClick(slotId, button, shift, player, player.inventory.getCursorStack());
    }
}
