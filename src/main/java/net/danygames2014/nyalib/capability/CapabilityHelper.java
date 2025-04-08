package net.danygames2014.nyalib.capability;

import net.danygames2014.nyalib.capability.block.BlockCapability;
import net.danygames2014.nyalib.capability.block.BlockCapabilityRegistry;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

public class CapabilityHelper {
    public static BlockCapability getBlockCapability(World world, int x, int y, int z, Identifier identifier) {
        return BlockCapabilityRegistry.getCapability(world, x, y, z, identifier);
    }
}
