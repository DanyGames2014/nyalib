package net.danygames2014.nyalib.mixin.multipart;

import com.llamalad7.mixinextras.sugar.Local;
import net.danygames2014.nyalib.multipart.MultipartState;
import net.minecraft.world.World;
import net.minecraft.world.chunk.light.LightUpdate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = LightUpdate.class, priority = 1100)
public class LightUpdateMixin {
    @ModifyVariable(
            method = "updateLight(Lnet/minecraft/world/World;)V",
            at = @At(
                    value = "STORE",
                    ordinal = 2
            ),
            index = 20
    )
    private int getMultipartLuminance(int original, @Local World world, @Local(index = 10) int x, @Local(index = 15) int y, @Local(index = 11) int z) {
        MultipartState state = world.getMultipartState(x,y,z);
        if (state != null) {
            return Math.max(original, state.getLightLevel());
        }
        return original;
    }
}
