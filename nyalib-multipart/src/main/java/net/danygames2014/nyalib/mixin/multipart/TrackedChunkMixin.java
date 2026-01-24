package net.danygames2014.nyalib.mixin.multipart;

import net.danygames2014.nyalib.mixininterface.MultipartTrackedChunk;
import net.danygames2014.nyalib.packet.MultipartDataS2CPacket;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.ChunkMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(ChunkMap.TrackedChunk.class)
public abstract class TrackedChunkMixin implements MultipartTrackedChunk {
    @Shadow
    public abstract void sendPacketToPlayers(Packet packet);

    @Shadow
    @Final
    ChunkMap field_2136;

    @Override
    public void updatePlayerMultipart(int x, int y, int z) {
        this.sendPacketToPlayers(new MultipartDataS2CPacket(this.field_2136.getWorld(), x,y,z));
    }
}
