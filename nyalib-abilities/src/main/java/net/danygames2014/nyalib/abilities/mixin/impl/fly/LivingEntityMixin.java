package net.danygames2014.nyalib.abilities.mixin.impl.fly;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(World world) {
        super(world);
    }

    @ModifyExpressionValue(method = "travel", at = @At(value = "CONSTANT", args = "floatValue=0.02"))
    public float modifySpeedInAir(float original) {
        return original;
    }
    
    @WrapOperation(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isSubmergedInWater()Z"))
    public boolean cancelWaterMovement(LivingEntity instance, Operation<Boolean> original) {
        return original.call(instance);
    }
    
    @WrapOperation(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isTouchingLava()Z"))
    public boolean cancelLavaMovement(LivingEntity instance, Operation<Boolean> original) {
        return original.call(instance);
    }
}
