package net.danygames2014.nyalib.util;

import net.minecraft.block.MapColor;

public class MapColorUtil {
    public static MapColor getMapColor(int color) {
        return getMapColor(color, 30D);
    }
    
    public static MapColor getMapColor(int color, double maxDeltaE) {
        double closestDeltaE = Double.MAX_VALUE;
        MapColor closestColor = null;
        
        for (MapColor mapColor : MapColor.COLORS) {
            if (mapColor == null) {
                continue;
            }
            
            double deltaE = ColorUtil.getDeltaE2000(color, mapColor.color);
            if (deltaE < closestDeltaE) {
                closestDeltaE = deltaE;
                closestColor = mapColor;
            }
        }
        
        if (closestDeltaE > maxDeltaE && MapColor.COLORS.length < 64) {
            return addMapColor(color);
        } else {
            return closestColor;
        }
    }
    
    public static MapColor addMapColor(int color) {
        MapColor[] newArray = new MapColor[MapColor.COLORS.length + 1];
        System.arraycopy(MapColor.COLORS, 0, newArray, 0, MapColor.COLORS.length);
        MapColor.COLORS = newArray;

        return new MapColor(MapColor.COLORS.length - 1, color);
    }
}
