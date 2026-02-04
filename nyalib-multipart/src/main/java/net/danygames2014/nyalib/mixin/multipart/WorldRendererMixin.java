package net.danygames2014.nyalib.mixin.multipart;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.danygames2014.nyalib.block.voxelshape.BoxToLinesConverter;
import net.danygames2014.nyalib.block.voxelshape.Line;
import net.danygames2014.nyalib.item.multipart.CustomMultipartOutlineRenderer;
import net.danygames2014.nyalib.mixininterface.MultipartWorldRenderer;
import net.danygames2014.nyalib.multipart.MultipartHitResult;
import net.danygames2014.nyalib.util.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin implements MultipartWorldRenderer {

    @Shadow public float miningProgress;

    @Shadow private TextureManager textureManager;

    @Shadow private World world;

    @Shadow private BlockRenderManager blockRenderManager;

    @SuppressWarnings("ExtractMethodRecommender")
    @Override
    public void renderMultipartOutline(PlayerEntity player, float tickDelta){
        if(Minecraft.INSTANCE.getMultipartCrosshairTarget() != null){
            MultipartHitResult multipartHitResult = Minecraft.INSTANCE.getMultipartCrosshairTarget();
            boolean renderedCustom = false;

            if(player.getHand() != null && player.getHand().getItem() instanceof CustomMultipartOutlineRenderer outlineRenderer){
                renderedCustom = outlineRenderer.renderOutline(player, multipartHitResult, tickDelta);
            }
            if(multipartHitResult != null && !renderedCustom){
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
                GL11.glLineWidth(2.0F);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDepthMask(false);

                float zFightExpansion = 0.002F;

                ObjectArrayList<Box> boxes = multipartHitResult.component.getBoundingBoxes();
                for (int i = 0; i < boxes.size(); i++) {
                    Box box = boxes.get(i);
                    boxes.set(i, box.expand(zFightExpansion, zFightExpansion, zFightExpansion));
                }
                List<Line> lines = BoxToLinesConverter.convertBoxesToLines(boxes.toArray(new Box[0]), new Vec3d(0.5, 0.5, 0.5));

                Vec3d playerPos = PlayerUtil.getRenderPosition(player, tickDelta);

                Vec3d offset = new Vec3d(0, 0, 0);

                Tessellator tessellator = Tessellator.INSTANCE;
                tessellator.start(1);
                for (Line line : lines) {
                    tessellator.vertex(
                            -playerPos.getX() + line.getStart().x + offset.x,
                            -playerPos.getY() + line.getStart().y + offset.y,
                            -playerPos.getZ() + line.getStart().z + offset.z
                    );
                    tessellator.vertex(
                            -playerPos.getX() + line.getEnd().x + offset.x,
                            -playerPos.getY() + line.getEnd().y + offset.y,
                            -playerPos.getZ() + line.getEnd().z + offset.z
                    );
                }
                tessellator.draw();

                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_BLEND);
            }
        }
    }

    public void renderMultipartMiningProgress(PlayerEntity player, float tickDelta) {
        if(Minecraft.INSTANCE.getMultipartCrosshairTarget() != null) {
            Tessellator tessellator = Tessellator.INSTANCE;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(770, 1);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, (MathHelper.sin((float)System.currentTimeMillis() / 100.0F) * 0.2F + 0.4F) * 0.5F);

            if (this.miningProgress > 0) {
                GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR);
                int texture = this.textureManager.getTextureId("/terrain.png");
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
                GL11.glPushMatrix();
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glPolygonOffset(-3.0F, -3.0F);
                GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);

                Vec3d playerPos = PlayerUtil.getRenderPosition(player, tickDelta);

                GL11.glEnable(GL11.GL_ALPHA_TEST);
                tessellator.startQuads();
                tessellator.setOffset(-playerPos.getX(), -playerPos.getY(), -playerPos.getZ());
                tessellator.disableColor();

                MultipartHitResult hitResult = Minecraft.INSTANCE.getMultipartCrosshairTarget();
                for(Box box : hitResult.component.getBoundingBoxes()){

                    Block.STONE.setBoundingBox((float) box.minX - hitResult.blockX, (float) box.minY - hitResult.blockY, (float) box.minZ - hitResult.blockZ, (float) box.maxX - hitResult.blockX, (float) box.maxY - hitResult.blockY, (float) box.maxZ - hitResult.blockZ);
                    this.blockRenderManager.renderWithTexture(Block.STONE, hitResult.blockX, hitResult.blockY, hitResult.blockZ, 240 + (int)(this.miningProgress * 10.0F));
                }
                Block.STONE.setBoundingBox(0f,0f, 0f, 1f, 1f, 1f);


                tessellator.draw();
                tessellator.setOffset(0.0F, 0.0F, 0.0F);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glPolygonOffset(0.0F, 0.0F);
                GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glDepthMask(true);
                GL11.glPopMatrix();
            }

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
        }
    }
}
