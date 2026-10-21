package net.danygames2014.nyalib.abilities.mixin.impl.extrahearts;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.danygames2014.nyalib.abilities.ability.AbilityManager;
import net.danygames2014.nyalib.abilities.ability.impl.Abilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(World world) {
        super(world);
    }

    @ModifyExpressionValue(method = "heal", at = @At(value = "CONSTANT", args = "intValue=20"))
    public int makeHealUseMaxHealth(int original) {
        return original + AbilityManager.getInstance().get((LivingEntity) (Object) this, Abilities.EXTRA_HEARTS);
    }
    
    @WrapOperation(method = "heal", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;maxHealth:I", opcode = Opcodes.GETFIELD))
    public int extraHeartsHeal(LivingEntity livingEntity, Operation<Integer> original) {
        return original.call(livingEntity) + AbilityManager.getInstance().get(livingEntity, Abilities.EXTRA_HEARTS); 
    }
    
    @WrapOperation(method = "damage", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;maxHealth:I", opcode = Opcodes.GETFIELD))
    public int extraHeartsDamage(LivingEntity livingEntity, Operation<Integer> original) {
        return original.call(livingEntity) + AbilityManager.getInstance().get(livingEntity, Abilities.EXTRA_HEARTS);
    }
}
