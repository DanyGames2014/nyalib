package net.danygames2014.nyalib.capability.entity.itemhandler;

import net.danygames2014.nyalib.capability.entity.EntityCapabilityProvider;
import net.danygames2014.nyalib.item.entity.ItemHandlerEntity;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class ItemHandlerEntityInterfaceEntityCapabilityProvider extends EntityCapabilityProvider<ItemHandlerEntityCapability> {
    @Override
    public @Nullable ItemHandlerEntityCapability getCapability(Entity entity) {
        if (entity instanceof ItemHandlerEntity itemHandlerEntity) {
            return new ItemHandlerEntityInterfaceEntityCapability(itemHandlerEntity);
        }
        
        return null;
    }
}
