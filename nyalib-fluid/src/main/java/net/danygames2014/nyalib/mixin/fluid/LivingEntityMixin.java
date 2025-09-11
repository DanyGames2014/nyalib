package net.danygames2014.nyalib.mixin.fluid;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.danygames2014.nyalib.block.FlowingFluidBlock;
import net.danygames2014.nyalib.block.StillFluidBlock;
import net.danygames2014.nyalib.fluid.Fluid;
import net.minecraft.block.Block;
import net.minecraft.block.LiquidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
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
    Fluid fluid;
    
    // Breathing (or lack thereof)
    @WrapOperation(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isInFluid(Lnet/minecraft/block/material/Material;)Z"))
    public boolean checkIfInFluid(LivingEntity livingEntity, Material material, Operation<Boolean> original) {
        double eyeY = this.y + (double)this.getEyeHeight();
        
        int blockX = MathHelper.floor(this.x);
        int blockY = MathHelper.floor((float)MathHelper.floor(eyeY));
        int blockZ = MathHelper.floor(this.z);

        BlockState state = world.getBlockState(blockX, blockY, blockZ);

        if (state.getBlock() instanceof StillFluidBlock stillFluidBlock) {
            fluid = stillFluidBlock.fluid;
            return isInFluid(stillFluidBlock.fluid.getMaterial());
        } else if (state.getBlock() instanceof FlowingFluidBlock flowingFluidBlock) {
            fluid = flowingFluidBlock.fluid;
            return isInFluid(flowingFluidBlock.fluid.getMaterial());
        }
        
        fluid = null;
        return original.call(livingEntity, material);
    }
    
    @WrapOperation(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;canBreatheInWater()Z"))
    public boolean canBreatheInFluid(LivingEntity livingEntity, Operation<Boolean> original) {
        if (fluid != null) {
            return !fluid.willDrown(livingEntity);
        }
        
        return original.call(livingEntity);
    }
    
    // Movement Speed Multiplier
    @Unique
    public double movementMultiplier = 1.0D;

    @Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;move(DDD)V", ordinal = 0))
    public void gatherFluidMovementMultiplier(float moveX, float moveZ, CallbackInfo ci) {
        Block block = this.world.getBlockState(MathHelper.floor(this.x), MathHelper.floor(this.boundingBox.minY), MathHelper.floor(this.z)).getBlock();

        if (block instanceof StillFluidBlock stillFluidBlock) {
            movementMultiplier = stillFluidBlock.fluid.getMovementSpeedMultiplier((LivingEntity) (Object) this);
            return;
        } else if (block instanceof FlowingFluidBlock flowingFluidBlock) {
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
