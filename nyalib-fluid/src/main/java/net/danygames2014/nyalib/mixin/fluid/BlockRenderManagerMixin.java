package net.danygames2014.nyalib.mixin.fluid;

import net.danygames2014.nyalib.block.FlowingFluidBlock;
import net.danygames2014.nyalib.block.StillFluidBlock;
import net.minecraft.block.Block;
import net.minecraft.block.LiquidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.SpriteAtlasTexture;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRenderManager.class)
public abstract class BlockRenderManagerMixin {
    @Shadow
    private BlockView blockView;

    @Shadow
    protected abstract float getFluidHeight(int x, int y, int z, Material material);

    @Shadow
    private boolean skipFaceCulling;

    @Shadow
    public abstract void renderBottomFace(Block block, double x, double y, double z, int texture);

//    @Inject(method = "renderFluid", at = @At(value = "HEAD"), cancellable = true)
//    public void pain(Block block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
//        if (block instanceof FlowingFluidBlock flowingFluidBlock || block instanceof StillFluidBlock stillFluidBlock) {
//            SpriteAtlasTexture atlas = StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE);
//            
//            Tessellator tessellator = Tessellator.INSTANCE;
//            int colorMultiplier = block.getColorMultiplier(this.blockView, x, y, z);
//            float r = (float)(colorMultiplier >> 16 & 255) / 255.0F;
//            float g = (float)(colorMultiplier >> 8 & 255) / 255.0F;
//            float b = (float)(colorMultiplier & 255) / 255.0F;
//            boolean topSideVisible = block.isSideVisible(this.blockView, x, y + 1, z, 1);
//            boolean bottomSideVisible = block.isSideVisible(this.blockView, x, y - 1, z, 0);
//            boolean[] sidesVisible = new boolean[]{block.isSideVisible(this.blockView, x, y, z - 1, 2), block.isSideVisible(this.blockView, x, y, z + 1, 3), block.isSideVisible(this.blockView, x - 1, y, z, 4), block.isSideVisible(this.blockView, x + 1, y, z, 5)};
//            if (!topSideVisible && !bottomSideVisible && !sidesVisible[0] && !sidesVisible[1] && !sidesVisible[2] && !sidesVisible[3]) {
//                cir.setReturnValue(false);
//            } else {
//                boolean anySideRendered = false;
//                Material material = block.material;
//                int blockMeta = this.blockView.getBlockMeta(x, y, z);
//                float height1 = this.getFluidHeight(x, y, z, material);
//                float height2 = this.getFluidHeight(x, y, z + 1, material);
//                float height3 = this.getFluidHeight(x + 1, y, z + 1, material);
//                float height4 = this.getFluidHeight(x + 1, y, z, material);
//                
//                if (this.skipFaceCulling || topSideVisible) {
//                    anySideRendered = true;
//                    
//                    int textureId = block.getTexture(1, blockMeta);
//                    float flowAngle = (float) LiquidBlock.getFlowingAngle(this.blockView, x, y, z, material);
//                    if (flowAngle > -999.0F) {
//                        textureId = block.getTexture(2, blockMeta);
//                    }
//                    int xTextureId = (textureId & 15) << 4;
//                    int yTextureId = textureId & 240;
//                    
//                    double textureU = ((double)xTextureId + (double)8.0F) / (double)atlas.getWidth();
//                    double textureV = ((double)yTextureId + (double)8.0F) / (double)atlas.getHeight();
//                    if (flowAngle < -999.0F) {
//                        flowAngle = 0.0F;
//                    } else {
//                        textureU = (float)(xTextureId + 16) / atlas.getWidth();
//                        textureV = (float)(yTextureId + 16) / atlas.getHeight();
//                    }
//
//                    float angleSine = MathHelper.sin(flowAngle) * 8.0F / atlas.getWidth();
//                    float angleCosine = MathHelper.cos(flowAngle) * 8.0F / atlas.getHeight();
//                    float luminance = block.getLuminance(this.blockView, x, y, z);
//                    tessellator.color(1.0F * luminance * r, 1.0F * luminance * g, 1.0F * luminance * b);
//                    tessellator.vertex(x, (float)y + height1, z, textureU - (double)angleCosine - (double)angleSine, textureV - (double)angleCosine + (double)angleSine);
//                    tessellator.vertex(x, (float)y + height2, z + 1, textureU - (double)angleCosine + (double)angleSine, textureV + (double)angleCosine + (double)angleSine);
//                    tessellator.vertex(x + 1, (float)y + height3, z + 1, textureU + (double)angleCosine + (double)angleSine, textureV + (double)angleCosine - (double)angleSine);
//                    tessellator.vertex(x + 1, (float)y + height4, z, textureU + (double)angleCosine - (double)angleSine, textureV - (double)angleCosine - (double)angleSine);
//                }
//
//                if (this.skipFaceCulling || bottomSideVisible) {
//                    float luminance = block.getLuminance(this.blockView, x, y - 1, z);
//                    tessellator.color(0.5F * luminance, 0.5F * luminance, 0.5F * luminance);
//                    this.renderBottomFace(block, x, y, z, block.getTexture(0));
//                    anySideRendered = true;
//                }
//
//                for(int face = 0; face < 4; ++face) {
//                    int sideX = x;
//                    int sideZ = z;
//                    if (face == 0) {
//                        sideZ = z - 1;
//                    }
//
//                    if (face == 1) {
//                        ++sideZ;
//                    }
//
//                    if (face == 2) {
//                        sideX = x - 1;
//                    }
//
//                    if (face == 3) {
//                        ++sideX;
//                    }
//
//                    int textureId = block.getTexture(face + 2, blockMeta);
//                    int xTextureId = (textureId & 15) << 4;
//                    int yTextureId = textureId & 240;
//                    
//                    if (this.skipFaceCulling || sidesVisible[face]) {
//                        float cornerHeight1;
//                        float x2;
//                        float z2;
//                        float cornerHeight2;
//                        float x1;
//                        float z1;
//                        if (face == 0) {
//                            cornerHeight1 = height1;
//                            cornerHeight2 = height4;
//                            x1 = (float)x;
//                            x2 = (float)(x + 1);
//                            z1 = (float)z;
//                            z2 = (float)z;
//                        } else if (face == 1) {
//                            cornerHeight1 = height3;
//                            cornerHeight2 = height2;
//                            x1 = (float)(x + 1);
//                            x2 = (float)x;
//                            z1 = (float)(z + 1);
//                            z2 = (float)(z + 1);
//                        } else if (face == 2) {
//                            cornerHeight1 = height2;
//                            cornerHeight2 = height1;
//                            x1 = (float)x;
//                            x2 = (float)x;
//                            z1 = (float)(z + 1);
//                            z2 = (float)z;
//                        } else {
//                            cornerHeight1 = height4;
//                            cornerHeight2 = height3;
//                            x1 = (float)(x + 1);
//                            x2 = (float)(x + 1);
//                            z1 = (float)z;
//                            z2 = (float)(z + 1);
//                        }
//
//                        anySideRendered = true;
//                        double var41 = (float)(xTextureId) / atlas.getWidth();
//                        double var43 = ((double)(xTextureId + 16) - 0.01) / (double)atlas.getWidth();
//                        double var45 = ((float)yTextureId + (1.0F - cornerHeight1) * 16.0F) / atlas.getHeight();
//                        double var47 = ((float)yTextureId + (1.0F - cornerHeight2) * 16.0F) / atlas.getHeight();
//                        double var49 = ((double)(yTextureId + 16) - 0.01) / (double)atlas.getHeight();
//                        
//                        float luminance = block.getLuminance(this.blockView, sideX, y, sideZ);
//                        if (face < 2) {
//                            luminance *= 0.8F;
//                        } else {
//                            luminance *= 0.6F;
//                        }
//
//                        tessellator.color(1.0F * luminance * r, 1.0F * luminance * g, 1.0F * luminance * b);
//                        tessellator.vertex(x1, (float)y + cornerHeight1, z1, var41, var45);
//                        tessellator.vertex(x2, (float)y + cornerHeight2, z2, var43, var47);
//                        tessellator.vertex(x2, y, z2, var43, var49);
//                        tessellator.vertex(x1, y, z1, var41, var49);
//                    }
//                }
//
//                block.minY = 0.0D;
//                block.maxY = 1.0D;
//                cir.setReturnValue(anySideRendered);
//            }
//        }
//    }
}
