package net.danygames2014.nyalibtest.screen.slot;

import net.danygames2014.nyalib.fluid.FluidSlot;
import net.danygames2014.nyalib.fluid.block.FluidHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.item.ItemRenderer;

public class CustomFluidSlotWithRendering extends FluidSlot {
    public CustomFluidSlotWithRendering(FluidHandler handler, int index, int x, int y) {
        super(handler, index, x, y);
    }

    @Override
    public void render(Minecraft minecraft, TextRenderer textRenderer, ItemRenderer itemRenderer, HandledScreen screen, int mouseX, int mouseY, float delta, FluidSlot slot, int slotX, int slotY) {
        super.render(minecraft, textRenderer, itemRenderer, screen, mouseX, mouseY, delta, slot, slotX, slotY);
        textRenderer.draw("PABLO", slotX, slotY, 0xFF0066, true);
    }
}
