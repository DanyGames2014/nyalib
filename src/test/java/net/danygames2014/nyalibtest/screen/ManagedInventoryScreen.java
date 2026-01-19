package net.danygames2014.nyalibtest.screen;

import net.danygames2014.nyalibtest.item.ManagedInventoryBlockEntity;
import net.danygames2014.nyalibtest.screen.handler.ManagedInventoryScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.opengl.GL11;

public class ManagedInventoryScreen extends HandledScreen {
    public ManagedInventoryBlockEntity blockEntity;
    public PlayerEntity player;
    
    public ManagedInventoryScreen(PlayerEntity player, ManagedInventoryBlockEntity blockEntity) {
        super(new ManagedInventoryScreenHandler(player, blockEntity));
        this.blockEntity = blockEntity;
        this.player = player;
        this.backgroundHeight = 222;
        this.backgroundWidth = 176;
    }

    @Override
    protected void drawForeground() {
        textRenderer.draw("Managed Inventory", 8, 6, 4210752);
        textRenderer.draw("Inventory", 8, this.backgroundHeight - 96 + 2, 4210752);
    }

    protected void drawBackground(float tickDelta) {
        int bgTextureId = minecraft.textureManager.getTextureId("/assets/nyalibtest/stationapi/textures/gui/builder.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(bgTextureId);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(x, y, 0, 0, backgroundWidth, backgroundHeight);

        if (blockEntity.getStack(0) != null) {
            drawTexture(x - 76, y, 176, 0, 80, 151);
        }
    }
}
