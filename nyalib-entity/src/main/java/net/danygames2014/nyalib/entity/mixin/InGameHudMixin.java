package net.danygames2014.nyalib.entity.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin extends DrawContext {
    @Shadow
    private Minecraft minecraft;

    @Unique
    private final int[] heartHeights = new int[10];

    @Unique
    private final int[] colors = new int[] {0xFF0000, 0x00FFFF, 0x0000FF, 0x00FF00};

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(IIIIII)V", ordinal = 6))
    public void nyalibGetHeartHeights(float tickDelta, boolean screenOpen, int mouseX, int mouseY, CallbackInfo ci, @Local(name = "var16") int index, @Local(name = "var17") int height) {
        heartHeights[index] = height;
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ClientPlayerEntity;isInFluid(Lnet/minecraft/block/material/Material;)Z", shift = At.Shift.BEFORE))
    public void nyalibRenderHearts(float tickDelta, boolean screenOpen, int mouseX, int mouseY, CallbackInfo ci, @Local(name = "var6") int scaledWidth) {
        int heartsIndex = (int)Math.floor((double) minecraft.player.health / 20);
        int remainder = minecraft.player.health % 20;

        if(heartsIndex <= 0) {
            return;
        }

        int heartColor = colors[heartsIndex % colors.length];
        int prevHeartColor = 0;
        if(heartsIndex > 1) {
            prevHeartColor = colors[(heartsIndex - 1) % colors.length];
        }

        for(int i = 0; i < 10; ++i) {
            int x = scaledWidth / 2 - 91 + i * 8;

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.textureManager.getTextureId("/assets/nyalib-entity/stationapi/textures/gui/heart.png"));

            if(heartsIndex > 1) {
                GL11.glColor3f((float) ((prevHeartColor >> 16) & 0xFF) / 255.0f, (float) ((prevHeartColor >> 8) & 0xFF) / 255.0f, (float) (prevHeartColor & 0xFF) / 255.0f);
                this.drawTexture(x, heartHeights[i], 0, 0, 9, 9);
            }

            int healthForFull = (i * 2) + 2;
            int healthForHalf = (i * 2) + 1;

            if (remainder >= healthForHalf) {
                GL11.glColor3f((float) ((heartColor >> 16) & 0xFF) / 255.0f, (float) ((heartColor >> 8) & 0xFF) / 255.0f, (float) (heartColor & 0xFF) / 255.0f);

                if (remainder >= healthForFull) {
                    this.drawTexture(x, heartHeights[i], 0, 0, 9, 9);
                } else {
                    this.drawTexture(x, heartHeights[i], 9, 0, 9, 9);
                }
            }
        }
        GL11.glColor3f(1f, 1f, 1f);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.textureManager.getTextureId("/gui/icons.png"));
    }
}
