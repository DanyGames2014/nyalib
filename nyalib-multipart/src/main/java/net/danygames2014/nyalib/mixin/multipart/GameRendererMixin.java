package net.danygames2014.nyalib.mixin.multipart;

import com.llamalad7.mixinextras.sugar.Local;
import net.danygames2014.nyalib.multipart.MultipartHitResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "updateTargetedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;raycast(DF)Lnet/minecraft/util/hit/HitResult;"))
    void setMultiblockCrosshairTarget(float tickDelta, CallbackInfo ci){
        Minecraft.INSTANCE.setMultipartCrosshairTarget(MultipartHitResult.lastHit);
    }

    @Inject(method = "renderFrame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleManager;render(Lnet/minecraft/entity/Entity;F)V"))
    void renderMultiblockOutline(float tickDelta, long time, CallbackInfo ci, @Local WorldRenderer worldRenderer, @Local LivingEntity player){
        if(Minecraft.INSTANCE.crosshairTarget == null){
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            worldRenderer.renderMultipartMiningProgress((PlayerEntity) player, tickDelta);
            worldRenderer.renderMultipartOutline((PlayerEntity) player, tickDelta);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
        }
    }
}
