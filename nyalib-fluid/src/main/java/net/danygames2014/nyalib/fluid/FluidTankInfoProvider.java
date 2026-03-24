package net.danygames2014.nyalib.fluid;

public interface FluidTankInfoProvider {
    String getFluidTankName(int slot);
    
    default String getFluidTankUnits(int slot) {
        return "mB";
    }
}
