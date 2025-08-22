package net.danygames2014.nyalib.compat.whatsthis;

import net.danygames2014.nyalib.NyaLibCompat;
import net.danygames2014.nyalib.capability.CapabilityHelper;
import net.danygames2014.nyalib.capability.block.fluidhandler.FluidHandlerBlockCapability;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.danygames2014.whatsthis.api.IProbeHitData;
import net.danygames2014.whatsthis.api.IProbeInfo;
import net.danygames2014.whatsthis.api.IProbeInfoProvider;
import net.danygames2014.whatsthis.api.ProbeMode;
import net.danygames2014.whatsthis.config.Config;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;

public class FluidBlockProbeInfoProvider implements IProbeInfoProvider {
    int borderColor;
    int filledColor;
    int alternateFilledColor;

    public FluidBlockProbeInfoProvider() {
        borderColor = Config.parseColor(Config.PROBE_CONFIG.tankbarBorderColor);
        filledColor = Config.parseColor(Config.PROBE_CONFIG.tankbarFilledColor);
        alternateFilledColor = Config.parseColor(Config.PROBE_CONFIG.tankbarAlternateFilledColor);
    }

    @Override
    public String getID() {
        return NyaLibCompat.NAMESPACE.id("nyalib_block_fluid").toString();
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState state, IProbeHitData data) {
        BlockPos pos = data.getPos();

        FluidHandlerBlockCapability fluidHandler = CapabilityHelper.getCapability(world, pos.x, pos.y, pos.z, FluidHandlerBlockCapability.class);
        if (fluidHandler != null) {
            IProbeInfo vertical = probeInfo.vertical();
            
            for (int i = 0; i < fluidHandler.getFluidSlots(data.getSideHit()); i++) {
                FluidStack fluidStack = fluidHandler.getFluid(i, data.getSideHit());
                int capacity = fluidHandler.getFluidCapacity(i, data.getSideHit());

                if (fluidStack != null) {
                    vertical.progress(
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
                    vertical.progress(
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
