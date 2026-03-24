package net.danygames2014.nyalib.compat.whatsthis.providers;

import net.danygames2014.nyalib.NyaLibCompat;
import net.danygames2014.nyalib.capability.CapabilityHelper;
import net.danygames2014.nyalib.capability.block.fluidhandler.FluidHandlerBlockCapability;
import net.danygames2014.nyalib.compat.whatsthis.elements.ElementTankGauge;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.danygames2014.nyalib.fluid.FluidTankInfoProvider;
import net.danygames2014.whatsthis.api.IProbeHitData;
import net.danygames2014.whatsthis.api.IProbeInfo;
import net.danygames2014.whatsthis.api.IProbeInfoProvider;
import net.danygames2014.whatsthis.api.ProbeMode;
import net.danygames2014.whatsthis.config.Config;
import net.minecraft.block.entity.BlockEntity;
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
            BlockEntity blockEntity = world.getBlockEntity(pos.x, pos.y, pos.z);
            FluidTankInfoProvider tankInfoProvider = blockEntity instanceof FluidTankInfoProvider ? (FluidTankInfoProvider) blockEntity : null;
            
            IProbeInfo vertical = probeInfo.vertical();
            
            for (int slot = 0; slot < fluidHandler.getFluidSlots(data.getSideHit()); slot++) {
                FluidStack fluidStack = fluidHandler.getFluid(slot, data.getSideHit());
                int capacity = fluidHandler.getFluidCapacity(slot, data.getSideHit());
                
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
