package net.danygames2014.nyalib.abilities.mixin.impl.extrahearts;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.danygames2014.nyalib.abilities.ability.AbilityManager;
import net.danygames2014.nyalib.abilities.ability.impl.Abilities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin extends LivingEntity {
    public PlayerEntityMixin(World world) {
        super(world);
    }
    
    @ModifyExpressionValue(method = "tickMovement", at = @At(value = "CONSTANT", args = "intValue=20", ordinal = 0))
    public int healToMaxHealth(int original) {
        return original + AbilityManager.getInstance().get((PlayerEntity) (Object) this, Abilities.EXTRA_HEARTS);
    }
}
