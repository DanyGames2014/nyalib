package net.danygames2014.nyalib.util;

public class ColorUtil {
    /**
     * Calculates Delta E 2000 between two colors
     *
     * @param color1 The first color (e.g. 0x007D00)
     * @param color2 The second color (e.g. 0xFF5500)
     */
    public static double getDeltaE2000(int color1, int color2) {
        double[] lab1 = intToLab(color1 & 0xFFFFFF);
        double[] lab2 = intToLab(color2 & 0xFFFFFF);

        double L1 = lab1[0], a1 = lab1[1], b1 = lab1[2];
        double L2 = lab2[0], a2 = lab2[1], b2 = lab2[2];

        double avgL = (L1 + L2) / 2.0;
        double C1 = Math.sqrt(a1 * a1 + b1 * b1);
        double C2 = Math.sqrt(a2 * a2 + b2 * b2);
        double avgC = (C1 + C2) / 2.0;

        double G = 0.5 * (1 - Math.sqrt(Math.pow(avgC, 7) / (Math.pow(avgC, 7) + Math.pow(25, 7))));
        double a1p = (1 + G) * a1;
        double a2p = (1 + G) * a2;

        double C1p = Math.sqrt(a1p * a1p + b1 * b1);
        double C2p = Math.sqrt(a2p * a2p + b2 * b2);
        double avgCp = (C1p + C2p) / 2.0;

        double h1p = Math.toDegrees(Math.atan2(b1, a1p));
        if (h1p < 0) h1p += 360;
        double h2p = Math.toDegrees(Math.atan2(b2, a2p));
        if (h2p < 0) h2p += 360;

        double avgHp = Math.abs(h1p - h2p) > 180 ? (h1p + h2p + 360) / 2.0 : (h1p + h2p) / 2.0;
        double T = 1 - 0.17 * Math.cos(Math.toRadians(avgHp - 30)) + 0.24 * Math.cos(Math.toRadians(2 * avgHp)) +
                0.32 * Math.cos(Math.toRadians(3 * avgHp + 6)) - 0.20 * Math.cos(Math.toRadians(4 * avgHp - 63));

        double diffHp = h2p - h1p;
        if (Math.abs(diffHp) > 180) {
            if (h2p <= h1p) diffHp += 360;
            else diffHp -= 360;
        }

        double deltaLp = L2 - L1;
        double deltaCp = C2p - C1p;
        double deltaHp = 2 * Math.sqrt(C1p * C2p) * Math.sin(Math.toRadians(diffHp / 2.0));

        double Sl = 1 + (0.015 * Math.pow(avgL - 50, 2)) / Math.sqrt(20 + Math.pow(avgL - 50, 2));
        double Sc = 1 + 0.045 * avgCp;
        double Sh = 1 + 0.015 * avgCp * T;

        double deltaRo = 30 * Math.exp(-Math.pow((avgHp - 275) / 25, 2));
        double Rc = 2 * Math.sqrt(Math.pow(avgCp, 7) / (Math.pow(avgCp, 7) + Math.pow(25, 7)));
        double Rt = -Math.sin(Math.toRadians(2 * deltaRo)) * Rc;

        return Math.sqrt(Math.pow(deltaLp / Sl, 2) + Math.pow(deltaCp / Sc, 2) + Math.pow(deltaHp / Sh, 2) + Rt * (deltaCp / Sc) * (deltaHp / Sh));
    }
    
    /**
     * Calculates Delta E between two colors
     *
     * @param color1 The first color (e.g. 0x007D00)
     * @param color2 The second color (e.g. 0xFF5500)
     */
    public static double getDeltaE1976(int color1, int color2) {
        double[] lab1 = intToLab(color1);
        double[] lab2 = intToLab(color2);

        return Math.sqrt(
                Math.pow(lab2[0] - lab1[0], 2) +
                        Math.pow(lab2[1] - lab1[1], 2) +
                        Math.pow(lab2[2] - lab1[2], 2)
        );
    }

    /**
     * Converts an color represented by an integer to the LAB color space
     * 
     * @param color The color to convert
     * @return The same color in LAB color space
     */
    public static double[] intToLab(int color) {
        // 1. Extract RGB using bitmasking
        int rInt = (color >> 16) & 0xFF;
        int gInt = (color >> 8) & 0xFF;
        int bInt = color & 0xFF;

        // 2. Normalize to 0.0 - 1.0
        double r = rInt / 255.0;
        double g = gInt / 255.0;
        double b = bInt / 255.0;

        // 3. Linearize (sRGB Inverse Gamma)
        r = (r > 0.04045) ? Math.pow((r + 0.055) / 1.055, 2.4) : r / 12.92;
        g = (g > 0.04045) ? Math.pow((g + 0.055) / 1.055, 2.4) : g / 12.92;
        b = (b > 0.04045) ? Math.pow((b + 0.055) / 1.055, 2.4) : b / 12.92;

        // 4. Convert to XYZ (D65 Illuminant)
        double x = (r * 0.4124 + g * 0.3576 + b * 0.1805) / 0.95047;
        double y = (r * 0.2126 + g * 0.7152 + b * 0.0722);
        double z = (r * 0.0193 + g * 0.1192 + b * 0.9505) / 1.08883;

        // 5. Convert XYZ to LAB
        x = (x > 0.008856) ? Math.pow(x, 1.0 / 3.0) : (7.787 * x) + (16.0 / 116.0);
        y = (y > 0.008856) ? Math.pow(y, 1.0 / 3.0) : (7.787 * y) + (16.0 / 116.0);
        z = (z > 0.008856) ? Math.pow(z, 1.0 / 3.0) : (7.787 * z) + (16.0 / 116.0);

        return new double[]{
                (116.0 * y) - 16.0,  // L
                500.0 * (x - y),      // a
                200.0 * (y - z)       // b
        };
    }
}
