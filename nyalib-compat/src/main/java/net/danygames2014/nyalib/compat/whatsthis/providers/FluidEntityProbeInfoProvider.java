package net.danygames2014.nyalib.compat.whatsthis.providers;

import net.danygames2014.nyalib.NyaLibCompat;
import net.danygames2014.nyalib.capability.CapabilityHelper;
import net.danygames2014.nyalib.capability.entity.fluidhandler.FluidHandlerEntityCapability;
import net.danygames2014.nyalib.compat.whatsthis.elements.ElementTankGauge;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.danygames2014.nyalib.fluid.FluidTankInfoProvider;
import net.danygames2014.whatsthis.api.IProbeHitEntityData;
import net.danygames2014.whatsthis.api.IProbeInfo;
import net.danygames2014.whatsthis.api.IProbeInfoEntityProvider;
import net.danygames2014.whatsthis.api.ProbeMode;
import net.danygames2014.whatsthis.config.Config;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

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
            FluidTankInfoProvider tankInfoProvider = entity instanceof FluidTankInfoProvider ? (FluidTankInfoProvider) entity : null;
            
            IProbeInfo vertical = probeInfo.vertical();
            
            for (int slot = 0; slot < fluidHandler.getFluidSlots(); slot++) {
                FluidStack fluidStack = fluidHandler.getFluid(slot);
                int capacity = fluidHandler.getFluidCapacity(slot);

                String tankName = tankInfoProvider != null ? tankInfoProvider.getFluidTankName(slot) : "Tank";
                String tankUnits = tankInfoProvider != null ? tankInfoProvider.getFluidTankUnits(slot) : "mB";

                if (fluidStack != null) {
                    vertical.element(
                            new ElementTankGauge(
                                    tankName,
                                    fluidStack.fluid.getTranslatedName(),
                                    fluidStack.amount,
                                    capacity,
                                    tankUnits,
                                    fluidStack.fluid.getColor(),
                                    mode == ProbeMode.EXTENDED
                            )
                    );
                } else {
                    vertical.element(
                            new ElementTankGauge(
                                    tankName,
                                    "",
                                    0,
                                    capacity,
                                    tankUnits,
                                    0xFF777777,
                                    mode == ProbeMode.EXTENDED
                            )
                    );
                }
            }
        }
    }
}
