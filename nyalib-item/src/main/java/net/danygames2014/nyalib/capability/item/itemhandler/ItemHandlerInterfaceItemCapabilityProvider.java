package net.danygames2014.nyalib.capability.item.itemhandler;

import net.danygames2014.nyalib.capability.item.ItemCapabilityProvider;
import net.danygames2014.nyalib.item.ItemHandlerItem;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemHandlerInterfaceItemCapabilityProvider extends ItemCapabilityProvider<ItemHandlerItemCapability> {
    @Override
    public @Nullable ItemHandlerItemCapability getCapability(ItemStack stack) {
        if (stack.getItem() instanceof ItemHandlerItem itemHandler) {
            return new ItemHandlerInterfaceItemCapability(itemHandler, stack);
        }
        
        return null;
    }
}
