package net.danygames2014.nyalib.mixin.multipart;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.danygames2014.nyalib.multipart.MultipartState;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Explosion.class)
public class ExplosionMixin {
    @Shadow private World world;
    @Shadow public Entity source;
    private ObjectOpenHashSet<MultipartComponent> damagedComponents = new ObjectOpenHashSet<>();

    @Inject(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockId(III)I", ordinal = 0))
    void addDamagedMultipartComponents(CallbackInfo ci, @Local(ordinal = 1) float remainingPower, @Local(ordinal = 4)int x, @Local(ordinal = 5)int y, @Local(ordinal = 6)int z, @Local(ordinal = 2) float var21){
        MultipartState state = world.getMultipartState(x, y, z);
        if(state != null && state.components != null){
            for(MultipartComponent component : state.components) {
                remainingPower -= (component.getBlastResistance(source) + 0.3F) * var21;

                if(remainingPower > 0){
                    damagedComponents.add(component);
                }
            }
        }
    }

    @Inject(method = "playExplosionSound", at = @At("TAIL"))
    void destroyDamagedMultipartComponents(boolean addParticles, CallbackInfo ci) {
        for(MultipartComponent component : damagedComponents){
            component.onExploded();
        }
    }
}
