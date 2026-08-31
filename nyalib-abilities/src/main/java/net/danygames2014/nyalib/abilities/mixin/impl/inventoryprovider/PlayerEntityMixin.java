package net.danygames2014.nyalib.abilities.mixin.impl.inventoryprovider;

import net.danygames2014.nyalib.abilities.ability.impl.inventoryprovider.NyaLibInventoryAbilityProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin extends LivingEntity {
    @Unique
    NyaLibInventoryAbilityProvider inventoryProvider;

    public PlayerEntityMixin(World world) {
        super(world);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void tickInventoryProvider(CallbackInfo ci) {
        if (!world.isRemote) {
            if (inventoryProvider == null) {
                inventoryProvider = new NyaLibInventoryAbilityProvider((PlayerEntity) (Object) this);
            }

            inventoryProvider.tick();
        }
    }
}
