package net.danygames2014.nyalib.multipart;

import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public abstract class MultipartComponent {
    public int x;
    public int y;
    public int z;
    public World world;
    public MultipartState state;

    public void markDirty() {
        state.markDirty();
    }
    
    // NBT
    public void writeNbt(NbtCompound nbt) {

    }

    public void readNbt(NbtCompound nbt) {

    }
    
    // Rendering
    public void render(Tessellator tessellator, BlockRenderManager blockRenderManager, int renderLayer) {
        
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " { x=" + x + ", y=" + y + ", z=" + z + ", world=" + world + "}";
    }
}
