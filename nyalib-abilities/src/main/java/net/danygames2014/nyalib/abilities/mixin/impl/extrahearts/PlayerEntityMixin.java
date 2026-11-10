package net.danygames2014.nyalib.abilities.mixin.impl.extrahearts;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {
    public PlayerEntityMixin(World world) {
        super(world);
    }

    /**
     * Despite literally having a max health variable, this is once again hardcoded to 20 ._.
     */
    @ModifyExpressionValue(method = "tickMovement", at = @At(value = "CONSTANT", args = "intValue=20", ordinal = 0))
    public int healToMaxHealth(int original) {
        return getMaxHealth();
    }
}
