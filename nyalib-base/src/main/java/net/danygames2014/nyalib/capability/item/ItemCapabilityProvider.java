package net.danygames2014.nyalib.capability.item;

import net.danygames2014.nyalib.capability.entity.EntityCapability;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class ItemCapabilityProvider<T extends ItemCapability> {
    /**
     * Get the capability supplied by this provider
     *
     * @param stack The {@link ItemStack} to get capability for
     * @return The {@link EntityCapability} this provider can provide. <code>null</code> if it cannot provide any
     */
    public abstract @Nullable T getCapability(ItemStack stack);
}
