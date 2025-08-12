package net.danygames2014.nyalib.capability;

import net.danygames2014.nyalib.capability.block.BlockCapability;
import net.danygames2014.nyalib.capability.block.BlockCapabilityRegistry;
import net.danygames2014.nyalib.capability.entity.EntityCapability;
import net.danygames2014.nyalib.capability.entity.EntityCapabilityRegistry;
import net.danygames2014.nyalib.capability.item.ItemCapability;
import net.danygames2014.nyalib.capability.item.ItemCapabilityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class CapabilityHelper {
    // Block Capabilities
    public static <T extends BlockCapability> @Nullable T getCapability(World world, int x, int y, int z, Identifier identifier) {
        return BlockCapabilityRegistry.getCapability(world, x, y, z, identifier);
    }

    public static <T extends BlockCapability> @Nullable T getCapability(World world, int x, int y, int z, Class<T> capabilityClass) {
        return BlockCapabilityRegistry.getCapability(world, x, y, z, capabilityClass);
    }

    // Entity Capabilities
    public static <T extends EntityCapability> @Nullable T getCapability(Entity entity, Identifier identifier) {
        return EntityCapabilityRegistry.getCapability(entity, identifier);
    }

    public static <T extends EntityCapability> @Nullable T getCapability(Entity entity, Class<T> capabilityClass) {
        return EntityCapabilityRegistry.getCapability(entity, capabilityClass);
    }

    // Item capabilities
    public static <T extends ItemCapability> @Nullable T getCapability(ItemStack stack, Identifier identifier) {
        return ItemCapabilityRegistry.getCapability(stack, identifier);
    }

    public static <T extends ItemCapability> @Nullable T getCapability(ItemStack stack, Class<T> capabilityClass) {
        return ItemCapabilityRegistry.getCapability(stack, capabilityClass);
    }
}
