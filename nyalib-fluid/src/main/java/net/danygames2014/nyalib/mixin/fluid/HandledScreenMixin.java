package net.danygames2014.nyalib.mixin.fluid;

import com.llamalad7.mixinextras.sugar.Local;
import net.danygames2014.nyalib.fluid.FluidSlot;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.modificationstation.stationapi.api.util.Formatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin extends Screen {
    @Shadow
    public ScreenHandler container;

    @Shadow
    public abstract void init();

    @Shadow
    protected int backgroundHeight;

    @Shadow
    protected int backgroundWidth;

    @Shadow
    private static ItemRenderer itemRenderer;

    @Unique
    FluidSlot hoveredSlot = null;
    
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glEnable(I)V", ordinal = 0))
    public void renderFluidSlots(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        hoveredSlot = null;
        
        for (FluidSlot fluidSlot : container.getFluidSlots()) {
            boolean hovered = isPointOverFluidSlot(fluidSlot, mouseX, mouseY);

            if (fluidSlot.getStack() != null) {
                ItemStack renderedItem = new ItemStack(fluidSlot.getStack().fluid.getBucketFluid());
                itemRenderer.renderGuiItem(this.textRenderer, this.minecraft.textureManager, renderedItem, fluidSlot.x, fluidSlot.y);
                itemRenderer.renderGuiItemDecoration(this.textRenderer, this.minecraft.textureManager, renderedItem, fluidSlot.x, fluidSlot.y);
            }

            if (hoveredSlot == null && hovered) {
                hoveredSlot = fluidSlot;
                
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                this.fillGradient(hoveredSlot.x, hoveredSlot.y, hoveredSlot.x + 16, hoveredSlot.y + 16, 0x80ffffff, 0x80ffffff);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }
    }
    
    @Inject(method = "render",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawForeground()V"))
    public void renderTooltip(int mouseX, int mouseY, float delta, CallbackInfo ci, @Local(ordinal = 2) int screenX, @Local(ordinal = 3) int screenY) {
        if (hoveredSlot != null) {
            String[] tooltip;
            
            if (!hoveredSlot.hasStack()) {
                tooltip = new String[1];
                tooltip[0] = "Empty";
            } else {
                tooltip = new String[2];
                tooltip[0] = hoveredSlot.getStack().fluid.getTranslatedName();
                tooltip[1] = Formatting.GRAY.toString() + hoveredSlot.getStack().amount + "/" + hoveredSlot.getMaxFluidAmount() + "mB";
            }
            
            // Calculate the tooltip length according to the longest line
            int textWidth = -1;
            for (String line : tooltip) {
                int lineWidth = this.textRenderer.getWidth(line);
                if (lineWidth > textWidth) {
                    textWidth = lineWidth;
                }
            }
            
            int textHeight = tooltip.length * 10;

            int x = (mouseX - screenX + 12);
            int y = (mouseY - screenY - 12);
            this.fillGradient(x - 3, y - 3, x + textWidth + 3, y + textHeight + 3, -1073741824, -1073741824);
            
            for (int i = 0; i < tooltip.length; i++) {
                String line = tooltip[i];
                
                if (line != null && !line.isEmpty()) {
                    this.textRenderer.drawWithShadow(line, x, y + (i * 12), -1);
                }
            }
        }
    }

    @Unique
    public boolean isPointOverFluidSlot(FluidSlot fluidSlot, int x, int y) {
        int centerX = (this.width - this.backgroundWidth) / 2;
        int centerY = (this.height - this.backgroundHeight) / 2;
        x -= centerX;
        y -= centerY;
        return x >= fluidSlot.x - 1 && x < fluidSlot.x + 16 + 1 && y >= fluidSlot.y - 1 && y < fluidSlot.y + 16 + 1;
    }
    
    @Unique
    public FluidSlot getFluidSlotAt(int x, int y) {
        for (FluidSlot fluidSlot : container.getFluidSlots()) {
            if (isPointOverFluidSlot(fluidSlot, x, y)) {
                return fluidSlot;
            }
        }
        
        return null;
    }
    
    @Inject(method = "mouseClicked", at = @At("TAIL"))
    public void mouseClickedFluid(int mouseX, int mouseY, int button, CallbackInfo ci){
        if (button == 0 || button == 1) {
            FluidSlot fluidSlot = this.getFluidSlotAt(mouseX, mouseY);
            int x = (this.width - this.backgroundWidth) / 2;
            int y = (this.height - this.backgroundHeight) / 2;
            boolean outside = mouseX < x || mouseY < y || mouseX >= x + this.backgroundWidth || mouseY >= y + this.backgroundHeight;
            
            int slotId = -1;
            
            if (fluidSlot != null) {
                slotId = fluidSlot.id;
            }

            if (outside) {
                slotId = -999;
            }

            if (slotId != -1) {
                boolean shift = slotId != -999 && (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT));
                this.minecraft.interactionManager.clickFluidSlot(this.container.syncId, slotId, button, shift, this.minecraft.player);
            }
        }
    }
}
