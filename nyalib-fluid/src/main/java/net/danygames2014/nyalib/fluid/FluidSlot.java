package net.danygames2014.nyalib.fluid;

import net.danygames2014.nyalib.fluid.block.FluidHandler;
import net.danygames2014.nyalib.util.fluid.FluidStackUtil;
import net.danygames2014.nyalib.util.fluid.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;

public class FluidSlot {
    public final int index;
    private final FluidHandler handler;
    public int id;
    public int x;
    public int y;
    public int width;
    public int height;
    
    public boolean enabled = true;

    public FluidSlot(FluidHandler handler, int index, int x, int y, int width, int height) {
        this.handler = handler;
        this.index = index;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public FluidSlot(FluidHandler handler, int index, int x, int y) {
        this(handler, index, x, y, 16, 16);
    }

    // Manipulating the FluidStack itself
    public FluidStack getStack() {
        return handler.getFluid(index, null);
    }

    public boolean hasStack() {
        return getStack() != null;
    }

    public void setStack(FluidStack fluidStack) {
        handler.setFluid(index, fluidStack, null);
    }

    public FluidStack takeStack(int amount) {
        return handler.extractFluid(index, amount, null);
    }

    // Fluid Amount
    public int getFluidAmount() {
        return handler.getFluid(index, null).amount;
    }

    public void setFluidAmount(int amount) {
        handler.getFluid(index, null).amount = Math.max(0, Math.min(getMaxFluidAmount(), amount));
    }

    public int getMaxFluidAmount() {
        return handler.getFluidCapacity(index, null);
    }

    public FluidHandler getHandler() {
        return handler;
    }

    // Slot Rules
    public boolean canInsert(FluidStack fluidStack) {
        return true;
    }

    // Comparable
    public boolean equals(FluidHandler otherHandler, int otherIndex) {
        return otherHandler == this.handler && otherIndex == this.index;
    }
    
    // Rendering
    public boolean renderAmount(HandledScreen screen) {
        return true;
    }
    
    public void render(Minecraft minecraft, TextRenderer textRenderer, ItemRenderer itemRenderer, HandledScreen screen, int mouseX, int mouseY, float delta, FluidSlot slot, int slotX, int slotY) {
        if (slot.getStack() != null) {
            ItemStack renderedItem = new ItemStack(slot.getStack().fluid.getBucketFluid());
            itemRenderer.renderGuiItem(textRenderer, minecraft.textureManager, renderedItem, slotX, slotY);
            itemRenderer.renderGuiItemDecoration(textRenderer, minecraft.textureManager, renderedItem, slotX, slotY);

            if (slot.renderAmount(screen)) {
                String formattedAmount = FluidStackUtil.formatAmount(slot.getStack());
                int formattedAmountWidth = (int) (Minecraft.INSTANCE.textRenderer.getWidth(formattedAmount) * 0.53);
                RenderHelper.drawSmallText(slotX + width - formattedAmountWidth, slotY + height - 5, formattedAmount, 0xFFFFFF);
            }
        }
    }
}
