package net.danygames2014.nyalib.util.fluid;

import net.danygames2014.nyalib.fluid.FluidStack;

public class FluidStackUtil {
    public static String formatAmount(FluidStack stack) {
        if (stack == null) {
            return "";
        }

        int amount = stack.amount;

        if (amount >= 1000000000) {
            // >= 1000 kB
            return Math.floor((amount / 1000000000D) * 10) / 10 + "MB";
        } else if (amount >= 10000000) {
            // >= 10000 B
            return (int) Math.floor(amount / 1000000D) + "kB";
        } else if (amount >= 1000) {
            // >= 1000 mB
            return (int) Math.floor(amount / 1000D) + "B";
        } else {
            return amount + "mB";
        }
    }
}
