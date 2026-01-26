package net.danygames2014.nyalib.mixin.multipart;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import net.danygames2014.nyalib.mixininterface.ChunkWithMultipart;
import net.danygames2014.nyalib.multipart.MultipartState;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.modificationstation.stationapi.api.world.chunk.StationFlatteningChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(Chunk.class)
public abstract class FlattenedChunkMixin implements ChunkWithMultipart, StationFlatteningChunk {
    @Shadow
    public World world;
    @Shadow
    @Final
    public int x;
    @Shadow
    @Final
    public int z;
    @Unique
    private Int2ObjectOpenHashMap<MultipartState> multipartStates;
    
    @Inject(method = "<init>(Lnet/minecraft/world/World;II)V", at = @At(value = "TAIL"))
    public void initMultipartStates(World world, int xPos, int zPos, CallbackInfo ci) {
        multipartStates = new Int2ObjectOpenHashMap<>();
    }
    
    @Override
    public MultipartState getMultipartState(int chunkX, int y, int chunkZ) {
        int hashCode = (chunkX & 15) << 16 | (y << 8) | (chunkZ & 15);
        return multipartStates.get(hashCode);
    }

    @Override
    public boolean setMultipartState(int chunkX, int y, int chunkZ, MultipartState state) {
        int hashCode = (chunkX & 15) << 16 | (y << 8) | (chunkZ & 15);
        
        if (state == null) {
            multipartStates.remove(hashCode);
            return true;
        }
        
        state.world = this.world;
        state.x = this.x * 16 + chunkX;
        state.y = y;
        state.z = this.z * 16 + chunkZ;
        multipartStates.put(hashCode, state);
        state.markDirty();
        return true;
    }

    @Override
    public ObjectCollection<MultipartState> getMultipartStates() {
        return multipartStates.values();
    }
}
