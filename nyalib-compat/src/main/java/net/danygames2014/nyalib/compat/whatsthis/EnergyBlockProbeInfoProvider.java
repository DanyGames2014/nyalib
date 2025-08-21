package net.danygames2014.nyalib.compat.whatsthis;

import net.danygames2014.nyalib.NyaLibCompat;
import net.danygames2014.nyalib.capability.CapabilityHelper;
import net.danygames2014.nyalib.capability.block.energyhandler.EnergyStorageBlockCapability;
import net.danygames2014.nyalib.energy.EnergyConductor;
import net.danygames2014.nyalib.network.Network;
import net.danygames2014.nyalib.network.NetworkManager;
import net.danygames2014.nyalib.network.NetworkType;
import net.danygames2014.nyalib.network.energy.EnergyNetwork;
import net.danygames2014.whatsthis.api.IProbeHitData;
import net.danygames2014.whatsthis.api.IProbeInfo;
import net.danygames2014.whatsthis.api.IProbeInfoProvider;
import net.danygames2014.whatsthis.api.ProbeMode;
import net.danygames2014.whatsthis.config.Config;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;

public class EnergyBlockProbeInfoProvider implements IProbeInfoProvider {
    int borderColor;
    int filledColor;
    int alternateFilledColor;

    public EnergyBlockProbeInfoProvider() {
        borderColor = Config.parseColor(Config.PROBE_CONFIG.rfbarBorderColor);
        filledColor = Config.parseColor(Config.PROBE_CONFIG.rfbarFilledColor);
        alternateFilledColor = Config.parseColor(Config.PROBE_CONFIG.rfbarAlternateFilledColor);
    }

    @Override
    public String getID() {
        return NyaLibCompat.NAMESPACE.id("nyalib_block_energy").toString();
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState state, IProbeHitData data) {
        BlockPos pos = data.getPos();

        EnergyStorageBlockCapability energyStorage = CapabilityHelper.getCapability(world, pos.x, pos.y, pos.z, EnergyStorageBlockCapability.class);
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

        if (world.getBlockState(pos).getBlock() instanceof EnergyConductor) {
            Network net = NetworkManager.getAt(world.dimension, pos.x, pos.y, pos.z, NetworkType.ENERGY.getIdentifier());
            if (net instanceof EnergyNetwork energyNet) {
                EnergyNetwork.EnergyFlowEntry flowEntry = energyNet.getFlowEntry(pos.x, pos.y, pos.z);
                if (flowEntry != null) {
                    probeInfo.progress(
                            flowEntry.energyFlow,
                            flowEntry.conductor.getBreakdownPower(world, flowEntry.componentEntry),
                            probeInfo.defaultProgressStyle().suffix(" EU/t")
                                    .borderColor(borderColor)
                                    .filledColor(filledColor)
                                    .alternateFilledColor(alternateFilledColor)
                    );
                }
            }
        }
    }
}
