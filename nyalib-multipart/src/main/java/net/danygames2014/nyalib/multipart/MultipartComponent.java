package net.danygames2014.nyalib.multipart;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.danygames2014.nyalib.sound.SoundHelper;
import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.entity.Entity;
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
    /**
     * The x-coordinate of the {@link MultipartState} this component is in
     */
    public int x;
    /**
     * The y-coordinate of the {@link MultipartState} this component is in
     */
    public int y;
    /**
     * The z-coordinate of the {@link MultipartState} this component is in
     */
    public int z;
    /**
     * The world this component is in
     */
    public World world;
    /**
     * The {@link MultipartState} this component is in
     */
    public MultipartState state;

    // Properties
    public float hardness = 1.0F;

    /**
     * Marks the component dirty, subsequently marking the {@link MultipartState} dirty causing an update to be sent
     */
    public void markDirty() {
        state.markDirty();
    }

    // Events
    /**
     * Gets called when the component is added into a {@link MultipartState}
     */
    public void onPlaced() {

    }

    /**
     * Gets called when the {@link MultipartState} this component is in is updated
     * @param updateSource The component which caused the update
     * @param updateType The type of the update (If a component was added, removed etc.)
     */
    public void onStateUpdated(MultipartComponent updateSource, MultipartState.StateUpdateType updateType) {

    }

    /**
     * Called when a player right clicks this component
     * @param player The player interacting with the component
     * @param pos The position that the raycast hit
     * @param face The face that the raycast hit
     * @return <code>true</code> if the action was succesfull, cancelling subsequent actions
     */
    public boolean onUse(PlayerEntity player, Vec3d pos, Direction face) {
        return false;
    }

    /**
     * Called when a player starts breaking this component
     * @param player The player breaking the component
     */
    public void onBreakStart(PlayerEntity player) {

    }

    /**
     * Called when this component is exploded
     */
    public void onExploded() {
        onBreak();
        state.removeComponent(this, true);
    }

    /**
     * Called when this component is broken
     */
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

    // Properties
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

    public float getBlastResistance(Entity source) {
        return 0.0F;
    }

    public BlockSoundGroup getSoundGroup() {
        return Block.DEFAULT_SOUND_GROUP;
    }
    
    // Drop List
    public ObjectArrayList<ItemStack> getDropList() {
        return null;
    }

    // NBT
    public void writeNbt(NbtCompound nbt) {

    }

    public void readNbt(NbtCompound nbt) {

    }

    // Collision and Bounds checking
    public abstract ObjectArrayList<Box> getBoundingBoxes();
    
    public void getCollisionBoxes(ObjectArrayList<Box> boxes) {

    }

    @Nullable
    public MultipartHitResult raycast(Vec3d startPos, Vec3d endPos) {
        ObjectArrayList<Box> boxes = new ObjectArrayList<>();
        getCollisionBoxes(boxes);
        if(boxes.isEmpty()){
            return null;
        }
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
    public boolean render(Tessellator tessellator, BlockRenderManager blockRenderManager, int renderLayer) {
        return false;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " { x=" + x + ", y=" + y + ", z=" + z + ", world=" + world + "}";
    }
}
