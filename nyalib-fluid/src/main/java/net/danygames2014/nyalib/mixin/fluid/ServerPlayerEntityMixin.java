package net.danygames2014.nyalib.mixin.fluid;

import net.danygames2014.nyalib.fluid.FluidStack;
import net.danygames2014.nyalib.network.FluidInventoryS2CPacket;
import net.danygames2014.nyalib.network.ScreenHandlerFluidSlotUpdateS2CPacket;
import net.danygames2014.nyalib.screen.FluidScreenHandlerListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements FluidScreenHandlerListener {
    @Shadow
    public boolean skipPacketSlotUpdates;

    @Shadow
    public ServerPlayNetworkHandler networkHandler;

    public ServerPlayerEntityMixin(World world) {
        super(world);
    }

    @Override
    public void onFluidSlotUpdate(ScreenHandler handler, int slot, FluidStack stack) {
        if (!this.skipPacketSlotUpdates) {
            this.networkHandler.sendPacket(new ScreenHandlerFluidSlotUpdateS2CPacket(handler.syncId, slot, stack));
        }
    }

    @Override
    public void onFluidContentsUpdate(ScreenHandler handler, ArrayList<FluidStack> stacks) {
        this.networkHandler.sendPacket(new FluidInventoryS2CPacket(handler.syncId, stacks));
        this.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(-1, -1, this.inventory.getCursorStack()));
    }
}
