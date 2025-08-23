package net.danygames2014.nyalib.capability.block.energyhandler;

import net.danygames2014.nyalib.capability.block.BlockCapabilityProvider;
import net.danygames2014.nyalib.energy.EnergyConsumer;
import net.danygames2014.nyalib.energy.EnergyHandler;
import net.danygames2014.nyalib.energy.EnergySource;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EnergyHandlerInterfaceBlockCapabilityProvider extends BlockCapabilityProvider<EnergyHandlerBlockCapability> {
    @Override
    public @Nullable EnergyHandlerBlockCapability getCapability(World world, int x, int y, int z) {
        BlockEntity blockEntity = world.getBlockEntity(x, y, z);
        
        if (blockEntity instanceof EnergyHandler energyHandler) {
            EnergyConsumer energyConsumer = null;
            EnergySource energySource = null;
            
            if (blockEntity instanceof EnergySource source) {
                energySource = source;
            }
            
            if (blockEntity instanceof EnergyConsumer consumer) {
                energyConsumer = consumer;
            }
            
            return new EnergyHandlerInterfaceBlockCapability(energyHandler, energyConsumer, energySource);
        }
        
        return null;
    }
}
