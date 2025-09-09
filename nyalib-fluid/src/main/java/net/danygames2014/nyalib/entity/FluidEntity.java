package net.danygames2014.nyalib.entity;

import net.danygames2014.nyalib.fluid.Fluid;
import net.modificationstation.stationapi.api.util.Util;

public interface FluidEntity {
    default boolean isInFluid(Fluid fluid) {
        return Util.assertImpl();
    }
}
