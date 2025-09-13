package net.danygames2014.nyalib.compat.whatsthis.providers;

import net.danygames2014.nyalib.NyaLibCompat;
import net.danygames2014.nyalib.capability.CapabilityHelper;
import net.danygames2014.nyalib.capability.entity.energyhandler.EnergyStorageEntityCapability;
import net.danygames2014.whatsthis.api.*;
import net.danygames2014.whatsthis.config.Config;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class EnergyEntityProbeInfoProvider implements IProbeInfoEntityProvider {
    int borderColor;
    int filledColor;
    int alternateFilledColor;

    public EnergyEntityProbeInfoProvider() {
        borderColor = Config.parseColor(Config.PROBE_CONFIG.rfbarBorderColor);
        filledColor = Config.parseColor(Config.PROBE_CONFIG.rfbarFilledColor);
        alternateFilledColor = Config.parseColor(Config.PROBE_CONFIG.rfbarAlternateFilledColor);
    }

    @Override
    public String getID() {
        return NyaLibCompat.NAMESPACE.id("nyalib_entity_energy").toString();
    }

    @Override
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, Entity entity, IProbeHitEntityData data) {
        EnergyStorageEntityCapability energyStorage = CapabilityHelper.getCapability(entity, EnergyStorageEntityCapability.class);
        if (energyStorage != null) {
            probeInfo.progress(
                    energyStorage.getEnergyStored(),
                    energyStorage.getEnergyCapacity(),
                    probeInfo.defaultProgressStyle().suffix(" EU")
                            .borderColor(borderColor)
                            .filledColor(filledColor)
                            .alternateFilledColor(alternateFilledColor)
            );
        }
    }
}
