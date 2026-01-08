package net.danygames2014.nyalibtest.screen;

import net.danygames2014.nyalibtest.fluid.entity.ManagedFluidTankBlockEntity;
import net.danygames2014.nyalibtest.screen.handler.ManagedFluidTankScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.opengl.GL11;

public class ManagedFluidTankScreen extends HandledScreen {
    public ManagedFluidTankBlockEntity fluidTank;
    
    public ManagedFluidTankScreen(PlayerEntity player, ManagedFluidTankBlockEntity fluidTank) {
        super(new ManagedFluidTankScreenHandler(player, fluidTank));
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
