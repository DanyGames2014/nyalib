package net.danygames2014.nyalib.mixin.fluid;

import net.danygames2014.nyalib.block.FlowingFluidBlock;
import net.danygames2014.nyalib.block.StillFluidBlock;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {
    @Shadow
    private Minecraft minecraft;

    @Shadow
    protected abstract void renderUnderwaterOverlay(float delta);

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

        int textureId = stillBlock.textureHolder.getStillTextureId();
        //textureId = Atlases.getTerrain().getTexture(Identifier.of("nyalibtest:block/fuel_still")).index;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        this.renderUnderwaterOverlay(tickDelta);


//        int atlasTextureId = this.minecraft.textureManager.getTextureId("/terrain.png");
//        GL11.glBindTexture(3553, atlasTextureId);


        //int waterTextureId = this.minecraft.textureManager.getTextureId("/misc/water.png");
        //GL11.glBindTexture(3553, waterTextureId);
//        this.renderUnderwaterOverlay(tickDelta);

//        if (this.minecraft.player.isInFluid(Material.WATER)) {
//
//        }
    }
}
