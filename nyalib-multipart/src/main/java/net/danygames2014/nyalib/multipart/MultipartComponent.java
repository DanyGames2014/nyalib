package net.danygames2014.nyalib.multipart;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.danygames2014.nyalib.sound.SoundHelper;
import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.BlockSoundGroup;
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

    public float hardness = 1.0F;

    public void markDirty() {
        state.markDirty();
    }

    public boolean isHandHarvestable() {
        return true;
    }

    public float getHardness(PlayerEntity player) {
        if (this.hardness < 0.0F) {
            return 0.0F;
        } else {
            if (!player.canHarvestMultipart(x, y, z, this)) {
                return 1.0F / this.hardness / 100.0F;
            }

            return player.getMultipartBreakingSpeed(x, y, z, this) / this.hardness / 30.0F;
        }
    }

    public BlockSoundGroup getSoundGroup() {
        return Block.DEFAULT_SOUND_GROUP;
    }

    public void onPlaced() {
        
    }
    
    public void onBreakStart() {

    }

    public void onBreak() {
        if (!world.isRemote) {
            ObjectArrayList<ItemStack> dropList = getDropList();
            if (dropList != null) {
                for (ItemStack stack : dropList) {
                    ItemEntity itemEntity = new ItemEntity(world, x + 0.5, y + 0.5, z + 0.5, stack);
                    itemEntity.pickupDelay = 10;
                    world.spawnEntity(itemEntity);
                }
            }

            SoundHelper.playSound(world, x + 0.5, y + 0.5, z + 0.5, getSoundGroup().getBreakSound(), (getSoundGroup().getVolume() + 1.0F) / 2.0F, getSoundGroup().getPitch() * 0.8F);
        }
        markDirty();
    }
    
    public ObjectArrayList<ItemStack> getDropList() {
        return null;
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
        for (int i = 0; i < boxes.size(); i++) {
            HitResult hitResult = boxes.get(i).raycast(startPos, endPos);
            if (hitResult != null) {
                hitResults[i] = new MultipartHitResult(x, y, z, hitResult.pos, Direction.byId(hitResult.side), this);
            }
        }

        double minimumLengthSquared = Double.POSITIVE_INFINITY;
        int minimumIndex = 0;
        for (int i = 0; i < hitResults.length; i++) {
            MultipartHitResult hitResult = hitResults[i];
            if (hitResult == null) {
                continue;
            }

            double lengthSquared = hitResult.pos.squaredDistanceTo(startPos);

            if (lengthSquared < minimumLengthSquared) {
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
