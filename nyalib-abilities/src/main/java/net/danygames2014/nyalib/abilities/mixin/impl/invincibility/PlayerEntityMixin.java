package net.danygames2014.nyalib.abilities.mixin.impl.invincibility;

import net.danygames2014.nyalib.abilities.ability.AbilityManager;
import net.danygames2014.nyalib.abilities.ability.impl.Abilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "damage", at = @At(value = "HEAD"), cancellable = true)
    public void negateDamage(Entity damageSource, int amount, CallbackInfoReturnable<Boolean> cir) {
        if(AbilityManager.getInstance().get((PlayerEntity) (Object) this, Abilities.INVINCIBILITY)) {
            cir.setReturnValue(false);
        };
    }

    @Inject(method = "applyDamage", at = @At(value = "HEAD"), cancellable = true)
    public void negateApplyDamage(int amount, CallbackInfo ci) {
        if(AbilityManager.getInstance().get((PlayerEntity) (Object) this, Abilities.INVINCIBILITY)) {
            ci.cancel();
        };
    }
}
