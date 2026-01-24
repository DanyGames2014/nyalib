package net.danygames2014.nyalib.mixin.multipart;

import net.danygames2014.nyalib.mixininterface.MultipartPlayerManager;
import net.minecraft.server.ChunkMap;
import net.minecraft.server.PlayerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin implements MultipartPlayerManager {
    @Shadow
    protected abstract ChunkMap getChunkMap(int dimensionId);

    @Shadow
    private ChunkMap[] chunkMaps;

    @Override
    public void markMultipartDirty(int x, int y, int z, int dimensionId) {
        this.getChunkMap(dimensionId).markMultipartForUpdate(x, y, z);
    }
    
    @Inject(method = "updateAllChunks", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/ChunkMap;updateChunks()V", shift = At.Shift.AFTER))
    public void updateMultipartInChunks(CallbackInfo ci) {
        for (ChunkMap chunkMap : this.chunkMaps) {
            chunkMap.updateChunkMultiparts();
        }
    }
}
