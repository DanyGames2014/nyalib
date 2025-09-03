package net.danygames2014.nyalibtest.block.screen;

import net.danygames2014.nyalibtest.block.fluid.entity.FluidTankBlockEntity;
import net.danygames2014.nyalibtest.block.screen.handler.FluidTankScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.opengl.GL11;

public class FluidTankScreen extends HandledScreen {
    public FluidTankBlockEntity fluidTank;
    
    public FluidTankScreen(PlayerEntity player, FluidTankBlockEntity fluidTank) {
        super(new FluidTankScreenHandler(player, fluidTank));
        this.fluidTank = fluidTank;
        this.backgroundWidth = 165;
    }

    @Override
    protected void drawForeground() {
        textRenderer.draw("Macerator", 50, 6, 4210752);
        textRenderer.draw("Inventory", 8, this.backgroundHeight - 96 + 2, 4210752);
    }

    @Override
    protected void drawBackground(float tickDelta) {
        int bgTextureId = minecraft.textureManager.getTextureId("/gui/furnace.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(bgTextureId);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(x, y, 0, 0, backgroundWidth, backgroundHeight);
    }
}
