package net.danygames2014.nyalib.compat.whatsthis.styles;

import net.danygames2014.whatsthis.apiimpl.styles.ProgressStyle;

public class ProgressStyleNyaLib extends ProgressStyle {
    @Override
    public int getBorderColor() {
        return 0xff969696;
    }

    @Override
    public int getWidth() {
        return 100;
    }

    @Override
    public int getBackgroundColor() {
        return 0x44969696;
    }
}
