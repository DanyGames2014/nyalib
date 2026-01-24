package net.danygames2014.nyalib.mixin.multipart;

import net.danygames2014.nyalib.mixininterface.MultipartWorld;
import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.danygames2014.nyalib.multipart.MultipartState;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.modificationstation.stationapi.api.world.StationFlatteningWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(World.class)
public abstract class WorldMixin implements MultipartWorld, StationFlatteningWorld {
    @Shadow
    public abstract Chunk getChunk(int chunkX, int chunkZ);

    @Override
    public MultipartState getMultipartState(int x, int y, int z) {
        if (x < -32000000 || z < -32000000 || x > 32000000 || z > 32000000 || y < this.getBottomY() || y > this.getTopY()) {
            return null;
        }

        Chunk chunk = this.getChunk(x >> 4, z >> 4);
        return chunk.getMultipartState(x & 15, y, z & 15);
    }

    @Override
    public boolean setMultipartState(int x, int y, int z, MultipartState state) {
        if (x < -32000000 || z < -32000000 || x > 32000000 || z > 32000000 || y < this.getBottomY() || y > this.getTopY()) {
            return false;
        }

        Chunk chunk = this.getChunk(x >> 4, z >> 4);
        return chunk.setMultipartState(x & 15, y, z & 15, state);
    }

    @Override
    public boolean addMultipartComponent(int x, int y, int z, MultipartComponent component) {
        if (x < -32000000 || z < -32000000 || x > 32000000 || z > 32000000 || y < this.getBottomY() || y > this.getTopY()) {
            return false;
        }

        MultipartState state = this.getMultipartState(x,y,z);
        
        if (state == null) {
            state = new MultipartState();
            if (!this.setMultipartState(x,y,z,state)) {
                return false;
            }
        }
        
        return state.addComponent(component);
    }
}
