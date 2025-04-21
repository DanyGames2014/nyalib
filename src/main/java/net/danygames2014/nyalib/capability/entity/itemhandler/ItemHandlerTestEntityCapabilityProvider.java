package net.danygames2014.nyalib.capability.entity.itemhandler;

import net.danygames2014.nyalib.capability.entity.EntityCapabilityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

public class ItemHandlerTestEntityCapabilityProvider extends EntityCapabilityProvider<ItemHandlerEntityCapability> {
    @Override
    public @Nullable ItemHandlerEntityCapability getCapability(Entity entity) {
        if(entity instanceof PlayerEntity player){
            return new ItemHandlerTestEntityCapability(player);
        }
        return null;
    }
}
