package net.danygames2014.nyalib.entity.mixin.datatracker;

import net.danygames2014.nyalib.entity.datatracker.ExtendedDataTracker;
import net.danygames2014.nyalib.entity.datatracker.network.ExtendedEntityTrackerSpawnS2CPacket;
import net.danygames2014.nyalib.entity.datatracker.network.ExtendedEntityTrackerUpdateS2CPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.entity.EntityTrackerEntry;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@SuppressWarnings("rawtypes")
@Mixin(EntityTrackerEntry.class)
public abstract class EntityTrackerEntryMixin {
    @Shadow
    public Entity currentTrackedEntity;

    @Shadow
    public abstract void sendToAround(Packet packet);

    @Inject(method = "updateListener", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V", shift = At.Shift.AFTER, ordinal = 0))
    public void sendExtendedDataTrackerPacket(ServerPlayerEntity player, CallbackInfo ci) {
        PacketHelper.sendTo(player, new ExtendedEntityTrackerSpawnS2CPacket(this.currentTrackedEntity));
    }

    @Inject(method = "notifyNewLocation", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/data/DataTracker;isDirty()Z"))
    public void sendExtendedDataTrackerUpdatePacket(List players, CallbackInfo ci) {
        ExtendedDataTracker dataTracker = this.currentTrackedEntity.getExtendedDataTracker();
        if (dataTracker.isDirty()) {
            this.sendToAround(new ExtendedEntityTrackerUpdateS2CPacket(this.currentTrackedEntity.id, dataTracker));
        }
    }
}
