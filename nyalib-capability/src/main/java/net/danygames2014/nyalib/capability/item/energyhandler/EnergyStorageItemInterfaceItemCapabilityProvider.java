package net.danygames2014.nyalib.capability.item.energyhandler;

import net.danygames2014.nyalib.capability.item.ItemCapabilityProvider;
import net.danygames2014.nyalib.energy.EnergyStorageItem;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class EnergyStorageItemInterfaceItemCapabilityProvider extends ItemCapabilityProvider<EnergyStorageItemCapability> {
    @Override
    public @Nullable EnergyStorageItemCapability getCapability(ItemStack stack) {
        if (stack.getItem() instanceof EnergyStorageItem energyStorageItem) {
            return new EnergyStorageItemInterfaceItemCapability(stack, energyStorageItem);
        }
        return null;
    }
}
