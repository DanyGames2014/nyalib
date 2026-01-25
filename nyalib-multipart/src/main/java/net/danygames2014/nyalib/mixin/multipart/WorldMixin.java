package net.danygames2014.nyalib.mixin.multipart;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.danygames2014.nyalib.multipart.MultipartHitResult;
import net.danygames2014.nyalib.multipart.MultipartState;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "rawtypes"})
@Mixin(World.class)
public class WorldMixin {
    @Shadow
    private ArrayList tempCollisionBoxes;

    @Unique
    private final ObjectArrayList<Box> tempMultipartCollisionBoxes = new ObjectArrayList<>();

    @Unique
    private MultipartHitResult lastMultiblockRaycastHitResult = null;

    @Inject(method = "getEntityCollisions", at = @At(value = "HEAD"))
    public void clearMultipartCollisionBoxes(Entity entity, Box entityBox, CallbackInfoReturnable<List> cir) {
        tempMultipartCollisionBoxes.clear();
    }
    
    @Inject(method = "getEntityCollisions", at = @At(value = "FIELD", target = "Lnet/minecraft/block/Block;BLOCKS:[Lnet/minecraft/block/Block;", opcode = Opcodes.GETSTATIC))
    public void handleMultipartCollision(Entity entity, Box entityBox, CallbackInfoReturnable<List<?>> cir, @Local(ordinal = 6) int var9, @Local(ordinal = 7) int var10, @Local(ordinal = 8) int var11) {
        MultipartState state = entity.world.getMultipartState(var9, var11, var10);
        
        if (state != null) {
            state.getCollisionBoxes(tempMultipartCollisionBoxes);
            
            for (Box box : tempMultipartCollisionBoxes) {
                if (entityBox.intersects(box)) {
                    tempCollisionBoxes.add(box);
                }
            }
        }
    }

    @WrapOperation(
            method = "raycast(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;ZZ)Lnet/minecraft/util/hit/HitResult;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockId(III)I")
    )
    public int raycastMultipart(World instance, int x, int y, int z, Operation<Integer> original, Vec3d start, Vec3d end, boolean bl, boolean bl2){
        int blockId = original.call(instance, x, y, z);
        int blockMeta = instance.getBlockMeta(x, y, z);

        Block block = Block.BLOCKS[blockId];

        HitResult blockHitResult = null;

        if ((!bl2 || block == null || block.getCollisionShape(instance, x, y, z) != null) && blockId > 0 && block != null && block.hasCollision(blockMeta, bl)) {
            blockHitResult = block.raycast(instance, x, y, z, start, end);
        }

        MultipartState state = instance.getMultipartState(x, y, z);
        if(state == null || state.components == null){
            return blockId;
        }
        MultipartHitResult[] hitResults = new MultipartHitResult[state.components.size()];
        for(int i = 0; i < state.components.size(); i++){
            hitResults[i] = state.components.get(i).raycast(start, end);
        }

        double minimumLengthSquared = Double.POSITIVE_INFINITY;
        int minimumIndex = 0;
        for(int i = 0; i < hitResults.length; i++) {
            MultipartHitResult hitResult = hitResults[i];
            if(hitResult == null){
                continue;
            }

            double lengthSquared = hitResult.pos.squaredDistanceTo(start);

            if(lengthSquared < minimumLengthSquared) {
                minimumLengthSquared = lengthSquared;
                minimumIndex = i;
            }
        }

        double lengthBlock = Double.POSITIVE_INFINITY;
        if(blockHitResult != null){
            lengthBlock = blockHitResult.pos.squaredDistanceTo(start);
        }

        if(lengthBlock >= minimumLengthSquared) {
            MultipartHitResult.lastHit = hitResults[minimumIndex];
            lastMultiblockRaycastHitResult = hitResults[minimumIndex];
        } else {
            MultipartHitResult.lastHit = null;
            lastMultiblockRaycastHitResult = null;
        }

        if(hitResults[minimumIndex] == null || lengthBlock < minimumLengthSquared){
            return blockId;
        }
        return 0;
    }

    @Inject(
            method = "raycast(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;ZZ)Lnet/minecraft/util/hit/HitResult;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockId(III)I"),
            cancellable = true
    )
    void returnMultipart(Vec3d end, Vec3d bl, boolean bl2, boolean par4, CallbackInfoReturnable<HitResult> cir){
        if(lastMultiblockRaycastHitResult != null) {
            lastMultiblockRaycastHitResult = null;
            cir.setReturnValue(null);
        }
    }

    @Inject(
            method = "raycast(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;ZZ)Lnet/minecraft/util/hit/HitResult;",
            at = @At(value = "HEAD")
    )
    void resetLastHitMultipart(Vec3d end, Vec3d bl, boolean bl2, boolean par4, CallbackInfoReturnable<HitResult> cir){
        MultipartHitResult.lastHit = null;
    }
}
