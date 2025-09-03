package net.danygames2014.nyalib.mixin.fluid;

import net.danygames2014.nyalib.fluid.FluidStack;
import net.danygames2014.nyalib.network.ClickFluidSlotC2SPacket;
import net.minecraft.client.MultiplayerInteractionManager;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(MultiplayerInteractionManager.class)
public class MultiplayerInteractionManagerMixin extends InteractionManagerMixin {
    @Shadow
    private ClientNetworkHandler networkHandler;

    @Override
    public FluidStack clickFluidSlot(int syncId, int slotId, int button, boolean shift, PlayerEntity player) {
        short nextRevisionId = player.currentScreenHandler.nextRevision(player.inventory);
        FluidStack fluidStack = super.clickFluidSlot(syncId, slotId, button, shift, player);
        this.networkHandler.sendPacket(new ClickFluidSlotC2SPacket(syncId, slotId, button, shift, fluidStack, nextRevisionId));
        return fluidStack;
    }
}
