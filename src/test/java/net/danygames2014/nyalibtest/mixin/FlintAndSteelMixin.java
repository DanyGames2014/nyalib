package net.danygames2014.nyalibtest.mixin;

import net.danygames2014.nyalibtest.NyaLibTest;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlintAndSteel.class)
public class FlintAndSteelMixin {
    @Inject(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlock(IIII)Z"))
    public void explodeMe(ItemStack stack, PlayerEntity player, World world, int x, int y, int z, int side, CallbackInfoReturnable<Boolean> cir) {
        if (player.isInFluid(NyaLibTest.fuelFluid)) {
            world.createExplosion(null, x, y, z, 2.0F, true);
        }
    }
}
