package net.danygames2014.nyalib.compat.whatsthis;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class NyaLibRenderHelper {
    public static int drawSmallText(int x, int y, String text, int color) {
        GL11.glPushMatrix();
        GL11.glScalef(0.5F, 0.5F, 1.0F);
        Minecraft.INSTANCE.textRenderer.drawWithShadow(text, x * 2, y * 2, color);
        GL11.glPopMatrix();
        return Minecraft.INSTANCE.textRenderer.getWidth(text) / 2;
    }
}
