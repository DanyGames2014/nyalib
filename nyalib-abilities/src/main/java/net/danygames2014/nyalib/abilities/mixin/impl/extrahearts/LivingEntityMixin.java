package net.danygames2014.nyalib.abilities.mixin.impl.extrahearts;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.danygames2014.nyalib.abilities.ability.AbilityManager;
import net.danygames2014.nyalib.abilities.ability.impl.Abilities;
import net.danygames2014.nyalib.entity.maxhealth.mixininterface.NyaLibMaxHealthLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements NyaLibMaxHealthLivingEntity {
    @Shadow
    public int health;

    @Unique
    public int healthBonus;
    
    @Unique
    public LivingEntity self;
    
    public LivingEntityMixin(World world) {
        super(world);
    }

    /**
     * Stores a reference to itself casted to LivingEntity so we don't have to cast it every time
     */
    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void storeCastedReference(World world, CallbackInfo ci) {
        this.self = (LivingEntity) (Object) this;
    }

    /**
     * Updates the health bonus granted by the Extra Hearts ability and makes sure the player's health doesn't exceed their maximum health
     */
    @Unique
    private void updateHealthBonus() {
        this.healthBonus = AbilityManager.getInstance().get(self, Abilities.EXTRA_HEALTH);

        int maxHealth = this.getMaxHealth();
        if (this.health > maxHealth) {
            this.health = maxHealth;
        }
    }
    
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;tick()V"))
    public void updateHealthBonusOnTick(CallbackInfo ci) {
        if ((this.age & 7) == 0) { // Every 8 ticks
            updateHealthBonus();
        }
    }
    
    /**
     * When the player is healed, updates the health bonus
     */
    @Inject(method = "heal", at = @At(value = "HEAD"))
    public void updateMaxHealthOnHeal(int amount, CallbackInfo ci) {
        updateHealthBonus();
    }

    /**
     * When the player is damaged, updates the health bonus and caps the health to the max health
     */
    @Inject(method = "damage", at = @At(value = "HEAD"))
    public void updateMaxHealthOnDamage(Entity damageSource, int amount, CallbackInfoReturnable<Boolean> cir) {
        updateHealthBonus();
    }
    
    /**
     * Makes heal use the max health as by default it is hardcoded to 20
     */
    @ModifyExpressionValue(method = "heal", at = @At(value = "CONSTANT", args = "intValue=20"))
    public int makeHealUseMaxHealth(int original) {
        return this.getMaxHealth();
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public int getMaxHealth() {
        return NyaLibMaxHealthLivingEntity.super.getMaxHealth() + healthBonus;
    }
}
