package net.danygames2014.nyalib.compat.whatsthis.elements;

import net.danygames2014.nyalib.compat.whatsthis.styles.ProgressStyleTank;
import net.danygames2014.nyalib.init.ProbeInfoProviderListener;
import net.danygames2014.whatsthis.api.IElement;
import net.danygames2014.whatsthis.apiimpl.client.ElementProgressRender;
import net.danygames2014.whatsthis.apiimpl.client.ElementTextRender;
import net.danygames2014.whatsthis.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.I18n;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static net.danygames2014.nyalib.compat.whatsthis.NyaLibRenderHelper.drawSmallText;

public class ElementTankGauge implements IElement {
    private final String tankName;
    private final String fluidName;
    private final int amount;
    private final int capacity;
    private final String suffix;
    private final int color1;
    private final int color2;
    private final boolean sneaking;

    public ElementTankGauge(String tankName, String fluidName, int amount, int capacity, String suffix, int color1, boolean sneaking) {
        this.tankName = tankName;
        this.fluidName = fluidName;
        this.amount = amount;
        this.capacity = capacity;
        this.suffix = suffix;
        this.color1 = color1;
        this.color2 = new Color(this.color1).darker().hashCode();
        this.sneaking = sneaking;
    }

    public ElementTankGauge(DataInputStream stream) throws IOException {
        this.tankName = stream.readUTF();
        this.fluidName = stream.readUTF();
        this.amount = stream.readInt();
        this.capacity = stream.readInt();
        this.suffix = stream.readUTF();
        this.color1 = stream.readInt();
        this.color2 = new Color(this.color1).darker().hashCode();
        this.sneaking = stream.readBoolean();
    }

    @Override
    public void toBytes(DataOutputStream stream) throws IOException {
        stream.writeUTF(tankName);
        stream.writeUTF(fluidName);
        stream.writeInt(amount);
        stream.writeInt(capacity);
        stream.writeUTF(suffix);
        stream.writeInt(color1);
        stream.writeBoolean(sneaking);
    }

    @Override
    public void render(int x, int y) {
        if (capacity > 0) {
            ElementProgressRender.render(new ProgressStyleTank().filledColor(color1).alternateFilledColor(color2), amount, capacity, x, y, 100, sneaking ? 12 : 8);
        } else {
            ElementProgressRender.render(new ProgressStyleTank(), amount, capacity, x, y, 100, sneaking ? 12 : 8);
        }

        if (sneaking) {
            for (int i = 1; i < 10; i++) {
                RenderHelper.drawVerticalLine(x + i * 10, y + 1, y + (i == 5 ? 11 : 6), 0xff767676);
            }

            ElementTextRender.render((capacity > 0) ? amount + "/" + capacity + " " + suffix : I18n.getTranslation("compat.nyalib.whatsthis.empty_tank"), x + 3, y + 2);
            drawSmallText(x + 99 - Minecraft.INSTANCE.textRenderer.getWidth(fluidName) / 2, y + 13, fluidName, color1);
        }

        drawSmallText(sneaking ? x + 1 : x + 2, sneaking ? y + 13 : y + 2, tankName, 0xffffffff);
        RenderHelper.drawVerticalLine(x + 99, y, y + (sneaking ? 12 : 8), 0xff969696);
    }

    @Override
    public int getWidth() {
        return 100;
    }

    @Override
    public int getHeight() {
        return (sneaking) ? 18 : 8;
    }

    @Override
    public int getID() {
        return ProbeInfoProviderListener.ELEMENT_TANK_GAUGE;
    }
}
