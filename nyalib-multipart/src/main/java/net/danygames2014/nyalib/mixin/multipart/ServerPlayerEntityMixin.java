package net.danygames2014.nyalib.mixin.multipart;

import com.llamalad7.mixinextras.sugar.Local;
import net.danygames2014.nyalib.packet.MultipartDataS2CPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    public ServerPlayerEntityMixin(World world) {
        super(world);
    }

    @Inject(method = "playerTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V", ordinal = 1, shift = At.Shift.AFTER))
    public void sendMultipartData(boolean shouldSendChunkUpdates, CallbackInfo ci, @Local ChunkPos chunkPos) {
        if (!world.getChunk(chunkPos.x, chunkPos.z).getMultipartStates().isEmpty()) {
            // TODO: make custom multipart dirty on chunk
            PacketHelper.sendTo(this, new MultipartDataS2CPacket(this.world, chunkPos.x, chunkPos.z));
        }
    } 
}
