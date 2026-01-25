package net.danygames2014.nyalib.mixin.multipart;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.danygames2014.nyalib.mixininterface.MultipartWorldRenderer;
import net.danygames2014.nyalib.multipart.MultipartHitResult;
import net.danygames2014.nyalib.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin implements MultipartWorldRenderer {
    @Shadow private World world;

    @Shadow protected abstract void renderOutline(Box box);

    @Override
    public void renderMultipartOutline(PlayerEntity player, float tickDelta){
        if(Minecraft.INSTANCE.getMultipartCrosshairTarget() != null){
            MultipartHitResult multipartHitResult = Minecraft.INSTANCE.getMultipartCrosshairTarget();
            if(multipartHitResult != null){
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
                GL11.glLineWidth(2.0F);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDepthMask(false);

                float zFightExpansion = 0.002F;

                ObjectArrayList<Box> boxes = multipartHitResult.component.getBoundingBoxes();
                Vec3d playerPos = PlayerUtil.getRenderPosition(player, tickDelta);
                for(Box box : boxes) {
                    renderOutline(box.expand(zFightExpansion, zFightExpansion, zFightExpansion).offset(-playerPos.getX(), -playerPos.getY(), -playerPos.getZ()));
                }

                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_BLEND);
            }
        }
    }
}
