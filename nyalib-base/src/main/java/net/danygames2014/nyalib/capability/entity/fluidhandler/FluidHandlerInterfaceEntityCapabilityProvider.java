package net.danygames2014.nyalib.capability.entity.fluidhandler;

import net.danygames2014.nyalib.capability.entity.EntityCapabilityProvider;
import net.danygames2014.nyalib.fluid.FluidHandlerEntity;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class FluidHandlerInterfaceEntityCapabilityProvider extends EntityCapabilityProvider<FluidHandlerEntityCapability> {
    @Override
    public @Nullable FluidHandlerEntityCapability getCapability(Entity entity) {
        if (entity instanceof FluidHandlerEntity fluidHandler) {
            return new FluidHandlerInterfaceEntityCapability(fluidHandler);
        }
        
        return null;
    }
}
