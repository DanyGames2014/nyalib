package net.danygames2014.nyalib.util;

import net.minecraft.block.MapColor;

public class MapColorUtil {
    public static MapColor addMapColor(int color) {
        MapColor[] newArray = new MapColor[MapColor.COLORS.length + 1];
        System.arraycopy(MapColor.COLORS, 0, newArray, 0, MapColor.COLORS.length);
        MapColor.COLORS = newArray;

        return new MapColor(MapColor.COLORS.length - 1, color);
    }
}
