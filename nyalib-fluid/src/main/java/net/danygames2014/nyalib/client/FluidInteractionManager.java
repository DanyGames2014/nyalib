package net.danygames2014.nyalib.client;

import net.danygames2014.nyalib.fluid.FluidStack;
import net.minecraft.entity.player.PlayerEntity;
import net.modificationstation.stationapi.api.util.Util;

public interface FluidInteractionManager {
    default FluidStack clickFluidSlot(int syncId, int slotId, int button, boolean shift, PlayerEntity player) {
        return Util.assertImpl();
    }
}
