package net.danygames2014.nyalib.mixin.fluid;

import net.danygames2014.nyalib.fluid.FluidSlot;
import net.glasslauncher.mods.alwaysmoreitems.gui.Tooltip;
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

import java.util.List;

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

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glEnable(I)V", ordinal = 0))
    public void renderFluidSlots(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        for (FluidSlot fluidSlot : container.getFluidSlots()) {
            boolean hovered = isPointOverFluidSlot(fluidSlot, mouseX, mouseY);

            if (fluidSlot.getStack() != null) {
                ItemStack renderedItem = new ItemStack(fluidSlot.getStack().fluid.getBucketFluid());
                itemRenderer.renderGuiItem(this.textRenderer, this.minecraft.textureManager, renderedItem, fluidSlot.x, fluidSlot.y);
                itemRenderer.renderGuiItemDecoration(this.textRenderer, this.minecraft.textureManager, renderedItem, fluidSlot.x, fluidSlot.y);
            }

            if (hovered) {
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                this.fillGradient(fluidSlot.x, fluidSlot.y, fluidSlot.x + 16, fluidSlot.y + 16, 0x80ffffff, 0x80ffffff);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);

                if (!fluidSlot.hasStack()) {
                    Tooltip.INSTANCE.setTooltip(List.of("Empty"), mouseX, mouseY);
                } else if (fluidSlot.hasStack()) {
                    Tooltip.INSTANCE.setTooltip(List.of(fluidSlot.getStack().fluid.getTranslatedName(), Formatting.GRAY + String.valueOf(fluidSlot.getStack().amount)), mouseX, mouseY);
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
