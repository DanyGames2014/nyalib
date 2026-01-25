package net.danygames2014.nyalib.multipart;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

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

    public abstract ObjectArrayList<Box> getBoundingBoxes();
    
    // Collision and Bounds checking
    public void getCollisionBoxes(ObjectArrayList<Box> boxes) {
        
    }

    @Nullable
    public MultipartHitResult raycast(Vec3d startPos, Vec3d endPos) {
        ObjectArrayList<Box> boxes = new ObjectArrayList<>();
        getCollisionBoxes(boxes);
        MultipartHitResult[] hitResults = new MultipartHitResult[boxes.size()];
        for(int i = 0; i < boxes.size(); i++){
            HitResult hitResult = boxes.get(i).raycast(startPos, endPos);
            if(hitResult != null) {
                hitResults[i] = new MultipartHitResult(x, y, z, hitResult.pos, Direction.byId(hitResult.side), this);
            }
        }

        double minimumLengthSquared = Double.POSITIVE_INFINITY;
        int minimumIndex = 0;
        for(int i = 0; i < hitResults.length; i++) {
            MultipartHitResult hitResult = hitResults[i];
            if(hitResult == null){
                continue;
            }

            double lengthSquared = hitResult.pos.squaredDistanceTo(startPos);

            if(lengthSquared < minimumLengthSquared) {
                minimumLengthSquared = lengthSquared;
                minimumIndex = i;
            }
        }

        return hitResults[minimumIndex];
    }
    
    // Rendering
    public void render(Tessellator tessellator, BlockRenderManager blockRenderManager, int renderLayer) {
        
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " { x=" + x + ", y=" + y + ", z=" + z + ", world=" + world + "}";
    }
}
