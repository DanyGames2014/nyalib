package net.danygames2014.nyalib.capability.entity.itemhandler;

import net.danygames2014.nyalib.capability.entity.EntityCapabilityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import org.jetbrains.annotations.Nullable;

public class ItemHandlerVanillaEntityCapabilityProvider extends EntityCapabilityProvider<ItemHandlerEntityCapability> {
    @Override
    public @Nullable ItemHandlerEntityCapability getCapability(Entity entity) {
        if (entity instanceof PlayerEntity player) {
            return new ItemHandlerPlayerEntityCapability(player);
        }

        if (entity instanceof MinecartEntity minecart) {
            return new ItemHandlerMinecartEntityCapability(minecart);
        }
        
        return null;
    }
}
