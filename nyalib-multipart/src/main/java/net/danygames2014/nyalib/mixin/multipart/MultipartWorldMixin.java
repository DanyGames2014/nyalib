package net.danygames2014.nyalib.mixin.multipart;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.danygames2014.nyalib.mixininterface.MultipartWorld;
import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.danygames2014.nyalib.multipart.MultipartState;
import net.danygames2014.nyalib.multipart.TickableComponent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.modificationstation.stationapi.api.world.StationFlatteningWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(World.class)
public abstract class MultipartWorldMixin implements MultipartWorld, StationFlatteningWorld {
    @Shadow
    public abstract Chunk getChunk(int chunkX, int chunkZ);

    @Unique
    private ObjectOpenHashSet<TickableComponent> tickableComponents;

    @Inject(method = "tickEntities", at = @At("TAIL"))
    public void tickMultipartComponents(CallbackInfo ci) {

        tickableComponents.removeIf(component -> component.getState().removed);

        for(TickableComponent tickableComponent : tickableComponents) {
            tickableComponent.tick();
        }
    }

    @Inject(
            method = {
                    "<init>(Lnet/minecraft/world/storage/WorldStorage;Ljava/lang/String;Lnet/minecraft/world/dimension/Dimension;J)V",
                    "<init>(Lnet/minecraft/world/World;Lnet/minecraft/world/dimension/Dimension;)V",
                    "<init>(Lnet/minecraft/world/storage/WorldStorage;Ljava/lang/String;JLnet/minecraft/world/dimension/Dimension;)V"
            },
            at = @At("TAIL")
    )
    public void initializeMultipartComponentLists(CallbackInfo ci) {
        tickableComponents = new ObjectOpenHashSet<>();
    }

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
        
        return state.addComponent(component, true);
    }

    @Override
    public boolean addTickableMultipartComponent(TickableComponent component) {
        return tickableComponents.add(component);
    }

    @Override
    public boolean removeTickableMultipartComponent(TickableComponent component) {
        return tickableComponents.remove(component);
    }
}
