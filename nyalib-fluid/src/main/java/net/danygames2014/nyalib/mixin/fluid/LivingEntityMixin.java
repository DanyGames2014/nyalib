package net.danygames2014.nyalib.mixin.fluid;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.danygames2014.nyalib.block.FlowingFluidBlock;
import net.danygames2014.nyalib.block.StillFluidBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(World world) {
        super(world);
    }

    @Unique
    public double movementMultiplier = 1.0D;

    @Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;move(DDD)V", ordinal = 0))
    public void gatherFluidMovementMultiplier(float moveX, float moveZ, CallbackInfo ci) {
        Block block = this.world.getBlockState(MathHelper.floor(this.x), MathHelper.floor(this.boundingBox.minY), MathHelper.floor(this.z)).getBlock();

        if (block instanceof StillFluidBlock stillFluidBlock) {
            movementMultiplier = stillFluidBlock.fluid.getMovementSpeedMultiplier((LivingEntity) (Object) this);
            return;
        }

        if (block instanceof FlowingFluidBlock flowingFluidBlock) {
            movementMultiplier = flowingFluidBlock.fluid.getMovementSpeedMultiplier((LivingEntity) (Object) this);
            return;
        }

        movementMultiplier = 1.0D;
    }

    @ModifyExpressionValue(method = "travel", at = @At(value = "CONSTANT", args = "doubleValue=0.800000011920929"))
    public double aVoid(double original) {
        if (movementMultiplier != 1.0D) {
            return movementMultiplier;   
        }
        
        return original;
    }
}
