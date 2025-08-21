package net.danygames2014.nyalib.compat.whatsthis;

import net.danygames2014.nyalib.NyaLibCompat;
import net.danygames2014.nyalib.capability.CapabilityHelper;
import net.danygames2014.nyalib.capability.block.fluidhandler.FluidHandlerBlockCapability;
import net.danygames2014.nyalib.capability.entity.fluidhandler.FluidHandlerEntityCapability;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.danygames2014.whatsthis.api.*;
import net.danygames2014.whatsthis.config.Config;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;

public class FluidEntityProbeInfoProvider implements IProbeInfoEntityProvider {
    int borderColor;
    int filledColor;
    int alternateFilledColor;

    public FluidEntityProbeInfoProvider() {
        borderColor = Config.parseColor(Config.PROBE_CONFIG.tankbarBorderColor);
        filledColor = Config.parseColor(Config.PROBE_CONFIG.tankbarFilledColor);
        alternateFilledColor = Config.parseColor(Config.PROBE_CONFIG.tankbarAlternateFilledColor);
    }

    @Override
    public String getID() {
        return NyaLibCompat.NAMESPACE.id("nyalib_entity_fluid").toString();
    }

    @Override
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, Entity entity, IProbeHitEntityData data) {
        FluidHandlerEntityCapability fluidHandler = CapabilityHelper.getCapability(entity, FluidHandlerEntityCapability.class);
        if (fluidHandler != null) {
            for (int i = 0; i < fluidHandler.getFluidSlots(); i++) {
                FluidStack fluidStack = fluidHandler.getFluid(i);
                int capacity = fluidHandler.getFluidCapacity(i);

                if (fluidStack != null) {
                    probeInfo.progress(
                            fluidStack.amount,
                            capacity,
                            probeInfo.defaultProgressStyle()
                                    .prefix(fluidStack.fluid.getTranslatedName() + " ")
                                    .suffix(" mB")
                                    .borderColor(borderColor)
                                    .filledColor(filledColor)
                                    .alternateFilledColor(alternateFilledColor)
                    );
                } else {
                    probeInfo.progress(
                            0,
                            capacity,
                            probeInfo.defaultProgressStyle()
                                    .prefix("Empty ")
                                    .suffix(" mB")
                                    .borderColor(borderColor)
                                    .filledColor(filledColor)
                                    .alternateFilledColor(alternateFilledColor)
                    );
                }
            }
        }
    }
}
