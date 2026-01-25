package net.danygames2014.nyalib.mixin.multipart;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.danygames2014.nyalib.multipart.MultipartHitResult;
import net.danygames2014.nyalib.multipart.MultipartState;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ArrowEntity.class)
public abstract class ArrowEntityMixin extends Entity {
    @Shadow private int blockId;
    @Shadow private int blockMeta;
    @Unique
    private MultipartComponent component;


    public ArrowEntityMixin(World world) {
        super(world);
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockId(III)I", ordinal = 0))
    int checkMultipartBoxesNoBlock(World instance, int x, int y, int z, Operation<Integer> original){
        int blockId = original.call(instance, x, y, z);
        if(blockId > 0){
            return blockId;
        }

        MultipartState state = world.getMultipartState(x, y, z);
        if(state == null){
            return blockId;
        }

        ObjectArrayList<Box>[] boxLists = new ObjectArrayList[state.components.size()];
        for(int i = 0; i < boxLists.length; i++) {
            boxLists[i] = new ObjectArrayList<>();
            state.components.get(i).getCollisionBoxes(boxLists[i]);
        }

        Vec3d arrowPos = Vec3d.createCached(this.x, this.y, this.z);

        for(int i = 0; i < boxLists.length; i++) {
            for(Box box : boxLists[i]) {
                if(box.contains(arrowPos)){
                    component = state.components.get(i);
                    break;
                }
            }
        }

        return blockId;
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getCollisionShape(Lnet/minecraft/world/World;III)Lnet/minecraft/util/math/Box;"))
    Box checkMultipartBoxes(Block instance, World world, int x, int y, int z, Operation<Box> original){
        Box blockCollisionBox = original.call(instance, world, x, y, z);
        Vec3d arrowPos = Vec3d.createCached(this.x, this.y, this.z);

        if(blockCollisionBox != null && blockCollisionBox.contains(arrowPos)) {
            return blockCollisionBox;
        }

        MultipartState state = world.getMultipartState(x, y, z);
        if(state == null){
            return blockCollisionBox;
        }

        ObjectArrayList<Box>[] boxLists = new ObjectArrayList[state.components.size()];
        for(int i = 0; i < boxLists.length; i++) {
            boxLists[i] = new ObjectArrayList<>();
            state.components.get(i).getCollisionBoxes(boxLists[i]);
        }

        for(int i = 0; i < boxLists.length; i++) {
            for(Box box : boxLists[i]) {
                if(box.contains(arrowPos)){
                    component = state.components.get(i);
                    return box;
                }
            }
        }
        return blockCollisionBox;
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockId(III)I", ordinal = 1))
    int overwriteBlockId(World instance, int x, int y, int z, Operation<Integer> original){
        if(component != null){
            return blockId;
        }
        return original.call(instance, x, y, z);
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockMeta(III)I", ordinal = 0))
    int overwriteBlockMeta(World instance, int x, int y, int z, Operation<Integer> original){
        if(component != null){
            return blockMeta;
        }
        return original.call(instance, x, y, z);
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;raycast(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;ZZ)Lnet/minecraft/util/hit/HitResult;"))
    HitResult setHitResult(World instance, Vec3d start, Vec3d end, boolean bl, boolean bl2, Operation<HitResult> original){
        HitResult hitResult = original.call(instance, start, end, bl, bl2);
        if(hitResult == null && MultipartHitResult.lastHit != null){
            return new HitResult(MultipartHitResult.lastHit.blockX, MultipartHitResult.lastHit.blockY, MultipartHitResult.lastHit.blockZ, MultipartHitResult.lastHit.face.ordinal(), MultipartHitResult.lastHit.pos);
        }
        return hitResult;
    }
}
