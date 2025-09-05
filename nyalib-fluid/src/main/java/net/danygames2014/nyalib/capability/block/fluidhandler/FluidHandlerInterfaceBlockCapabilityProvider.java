package net.danygames2014.nyalib.capability.block.fluidhandler;

import net.danygames2014.nyalib.capability.block.BlockCapabilityProvider;
import net.danygames2014.nyalib.fluid.FluidHandler;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FluidHandlerInterfaceBlockCapabilityProvider extends BlockCapabilityProvider<FluidHandlerBlockCapability> {
    @Override
    public @Nullable FluidHandlerBlockCapability getCapability(World world, int x, int y, int z) {
        if (world.getBlockEntity(x, y, z) instanceof FluidHandler fluidHandler) {
            return new FluidHandlerInterfaceBlockCapability(fluidHandler);
        }
        return null;
    }
}
