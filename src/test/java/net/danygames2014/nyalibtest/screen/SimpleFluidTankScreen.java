package net.danygames2014.nyalibtest.screen;

import net.danygames2014.nyalibtest.fluid.entity.SimpleFluidTankBlockEntity;
import net.danygames2014.nyalibtest.screen.handler.SimpleFluidTankScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.opengl.GL11;

public class SimpleFluidTankScreen extends HandledScreen {
    public SimpleFluidTankBlockEntity fluidTank;
    
    public ButtonWidget button;
    
    public SimpleFluidTankScreen(PlayerEntity player, SimpleFluidTankBlockEntity fluidTank) {
        super(new SimpleFluidTankScreenHandler(player, fluidTank));
        this.fluidTank = fluidTank;
        this.backgroundWidth = 165;
    }

    @Override
    public void init() {
        super.init();

        int startX = (width / 2) - (backgroundWidth / 2);
        int startY = (height / 2) - (backgroundHeight / 2);

        button = new ButtonWidget(9001, startX + 85, startY + 30, 32, 32, "AHA!");
        this.buttons.add(button);
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
