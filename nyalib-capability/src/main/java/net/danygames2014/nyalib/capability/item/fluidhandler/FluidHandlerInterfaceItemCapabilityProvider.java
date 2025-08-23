package net.danygames2014.nyalib.capability.item.fluidhandler;

import net.danygames2014.nyalib.capability.item.ItemCapabilityProvider;
import net.danygames2014.nyalib.fluid.FluidHandlerItem;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class FluidHandlerInterfaceItemCapabilityProvider extends ItemCapabilityProvider<FluidHandlerItemCapability> {
    @Override
    public @Nullable FluidHandlerItemCapability getCapability(ItemStack stack) {
        if (stack.getItem() instanceof FluidHandlerItem fluidHandler) {
            return new FluidHandlerInterfaceItemCapability(fluidHandler, stack);
        }
        
        return null;
    }
}
