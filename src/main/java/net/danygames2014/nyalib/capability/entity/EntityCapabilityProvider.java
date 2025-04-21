package net.danygames2014.nyalib.capability.entity;

import net.danygames2014.nyalib.capability.block.BlockCapability;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;

public abstract class EntityCapabilityProvider<T extends EntityCapability> {
    /**
     * Get the capability supplied by this provider
     *
     * @param entity The entity to get capability for
     * @return The {@link EntityCapability} this provider can provide. <code>null</code> if it cannot provide any
     */
    public abstract @Nullable T getCapability(Entity entity);
}
