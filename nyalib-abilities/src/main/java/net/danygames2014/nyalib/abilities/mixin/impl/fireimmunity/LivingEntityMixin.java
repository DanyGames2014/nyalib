package net.danygames2014.nyalib.abilities.mixin.impl.fireimmunity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.danygames2014.nyalib.abilities.ability.AbilityManager;
import net.danygames2014.nyalib.abilities.ability.impl.Abilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {
    @Unique
    public boolean nyalib$fireImmune = false;
    
    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void fetchFireImmunity(CallbackInfo ci) {
        nyalib$fireImmune = AbilityManager.getInstance().get((LivingEntity) (Object) this, Abilities.FIRE_IMMUNITY);
        if (nyalib$fireImmune) {
            fireTicks = 0;
        }
    }

    @Override
    public boolean baseTickFireImmune(Entity instance, Operation<Boolean> original) {
        return nyalib$fireImmune || original.call(instance);
    }

    @Override
    public boolean setOnFireFireImmune(Entity instance, Operation<Boolean> original) {
        return nyalib$fireImmune || original.call(instance);
    }

    @Override
    public boolean burnFireImmune(Entity instance, Operation<Boolean> original) {
        return nyalib$fireImmune || original.call(instance);
    }

    @Override
    public boolean moveIsInFireOrLava(World instance, Box box, Operation<Boolean> original) {
        return !nyalib$fireImmune && original.call(instance, box);
    }
}
