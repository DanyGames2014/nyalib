package net.danygames2014.nyalib.abilities.mixin.impl.fly;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.danygames2014.nyalib.abilities.ability.AbilityManager;
import net.danygames2014.nyalib.abilities.ability.impl.Abilities;
import net.danygames2014.nyalib.abilities.mixininterface.NyaLibFlyingPlayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin implements NyaLibFlyingPlayer {
    @Unique
    public boolean nyalib$flying = false;

    @Unique
    public float jumpMovementFactor = 0.02F;

    public PlayerEntityMixin(World world) {
        super(world);
    }

    @Override
    public boolean nyalib$canFly() {
        return AbilityManager.getInstance().get((PlayerEntity) (Object) this, Abilities.FLIGHT);
    }

    @Override
    public boolean nyalib$isFlying() {
        return nyalib$flying;
    }

    @Override
    public void nyalib$setFlying(boolean flying) {
        this.nyalib$flying = flying;
    }

    @WrapOperation(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;travel(FF)V"))
    public void fly(PlayerEntity instance, float x, float z, Operation<Void> original) {
        PlayerEntity playerEntity = (PlayerEntity) (Object) this;
        
        if (nyalib$flying && !nyalib$canFly()) {
            this.nyalib$setFlying(false);
        }
        
        if (nyalib$flying && this.vehicle == null && AbilityManager.getInstance().get(playerEntity, Abilities.FLIGHT)) {
            double d3 = this.velocityY;
            float f = this.jumpMovementFactor;
            this.jumpMovementFactor = AbilityManager.getInstance().get(playerEntity, Abilities.FLIGHT_SPEED); //* (float)(this.isSprinting() ? 2 : 1); TODO: Use CTRL to speed up flying
            original.call(instance, x, z);
            this.velocityY = d3 * 0.6;
            this.jumpMovementFactor = f;
            this.fallDistance = 0.0F;
            this.setFlag(7, false);
        } else {
            original.call(instance, x, z);
        }
    }

    @Override
    public float modifySpeedInAir(float original) {
        if (jumpMovementFactor != 0.02F) {
            return jumpMovementFactor;
        }

        return super.modifySpeedInAir(original);
    }
    
    @WrapWithCondition(method = "onLanding", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;onLanding(F)V"))
    public boolean cancelFallDamage(LivingEntity instance, float fallDistance) {
        return !this.nyalib$canFly();
    }
}
