package net.danygames2014.nyalib.mixin.fluid;

import net.danygames2014.nyalib.block.FlowingFluidBlock;
import net.danygames2014.nyalib.block.FluidBlockTextureHolder;
import net.danygames2014.nyalib.block.StillFluidBlock;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.util.math.MathHelper;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {
    @Shadow
    private Minecraft minecraft;

    @Inject(method = "renderScreenOverlays", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glEnable(I)V", ordinal = 0, shift = At.Shift.BEFORE))
    public void renderFluidOverlay(float tickDelta, CallbackInfo ci) {
        int x = MathHelper.floor(this.minecraft.player.x);
        int y = MathHelper.floor(this.minecraft.player.y);
        int z = MathHelper.floor(this.minecraft.player.z);
        Block block = this.minecraft.world.getBlockState(x, y, z).getBlock();

        StillFluidBlock stillBlock = null;
        if (block instanceof StillFluidBlock stillFluidBlock) {
            stillBlock = stillFluidBlock;
        } else if (block instanceof FlowingFluidBlock flowingFluidBlock) {
            if (flowingFluidBlock.fluid.getStillBlock() instanceof StillFluidBlock stillFluidBlock) {
                stillBlock = stillFluidBlock;
            }
        }

        if (stillBlock == null) {
            return;
        }

        if (!this.minecraft.player.isInFluid(stillBlock.material)) {
            return;
        }

        if (stillBlock.textureHolder.overlaySpriteId != -1) {
            renderWrappedOverlay(tickDelta, stillBlock.textureHolder);
        }
    }

    @Unique
    int atlasTextureId = -1;

    @Unique
    private void renderStaticOverlay(float tickDelta, int textureId) {
        Atlas.Sprite tex = Atlases.getTerrain().getTexture(textureId);

        Tessellator tessellator = Tessellator.INSTANCE;
        float brightness = this.minecraft.player.getBrightnessAtEyes(tickDelta);
        if (atlasTextureId == -1) {
            atlasTextureId = StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getGlId();
        }
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, atlasTextureId);
        GL11.glColor4f(brightness, brightness, brightness, 0.5F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glPushMatrix();
        tessellator.startQuads();
        tessellator.vertex(-1.0f, -1.0f, -0.5f, tex.getEndU(), tex.getEndV());
        tessellator.vertex(1.0f, -1.0f, -0.5f, tex.getStartU(), tex.getEndV());
        tessellator.vertex(1.0f, 1.0f, -0.5f, tex.getStartU(), tex.getStartV());
        tessellator.vertex(-1.0f, 1.0f, -0.5f, tex.getEndU(), tex.getStartV());
        tessellator.draw();
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Unique
    public void renderWrappedOverlay(float tickDelta, FluidBlockTextureHolder textureHolder) {
        Tessellator tessellator = Tessellator.INSTANCE;

        float textureWidth = textureHolder.overlayTextureWidth;
        float textureHeight = textureHolder.overlayTextureHeight;
        float windowSize = 64;
        
        float yaw = -this.minecraft.player.yaw / (textureWidth * 16);
        float pitch = this.minecraft.player.pitch / (textureHeight * 16);

        float windowU = windowSize / textureWidth; 
        float windowV = windowSize / textureHeight;

        float uMin = 0.0f + yaw;
        float uMax = windowU + yaw;
        float vMin = 0.0f + pitch;
        float vMax = windowV + pitch;

        float brightness = this.minecraft.player.getBrightnessAtEyes(tickDelta);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureHolder.getOverlayTextureId());
        GL11.glColor4f(brightness, brightness, brightness, 0.5F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glPushMatrix();
        tessellator.startQuads();
        tessellator.vertex(-1.0f, -1.0f, -0.5f, uMax, vMax);
        tessellator.vertex( 1.0f, -1.0f, -0.5f, uMin, vMax);
        tessellator.vertex( 1.0f,  1.0f, -0.5f, uMin, vMin);
        tessellator.vertex(-1.0f,  1.0f, -0.5f, uMax, vMin);
        tessellator.draw();
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
    }
}
