package net.danygames2014.nyalib.mixin.multipart;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.danygames2014.nyalib.mixininterface.MultipartChunkMap;
import net.minecraft.server.ChunkMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(ChunkMap.class)
public abstract class ChunkMapMixin implements MultipartChunkMap {
    @Shadow
    protected abstract ChunkMap.TrackedChunk getOrCreateChunk(int chunkX, int chunkZ, boolean createIfAbsent);

    @Shadow
    private MinecraftServer server;
    @Unique
    public ObjectOpenHashSet<BlockPos> multipartUpdateQueue = new ObjectOpenHashSet<>();
    
    @Override
    public void markMultipartForUpdate(int x, int y, int z) {
        multipartUpdateQueue.add(new BlockPos(x, y, z));
    }

    @Override
    public void updateChunkMultiparts() {
        for (BlockPos pos : multipartUpdateQueue) {
            int chunkX = pos.x >> 4;
            int chunkZ = pos.z >> 4;
            ChunkMap.TrackedChunk chunk = this.getOrCreateChunk(chunkX, chunkZ, false);
            if (chunk != null) {
                chunk.updatePlayerMultipart(pos.x, pos.y, pos.z);
                System.err.println("Sending update for block " + pos.x + ", " + pos.y + ", " + pos.z);
            }
        }
        
        multipartUpdateQueue.clear();
    }
}
