package net.danygames2014.nyalib.capability.block;

import net.danygames2014.nyalib.NyaLib;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("rawtypes")
public class BlockCapabilityProviderRegistry {
    private final HashMap<Identifier, ArrayList<BlockCapabilityProvider>> registry;
    private static BlockCapabilityProviderRegistry INSTANCE;

    public static BlockCapabilityProviderRegistry getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BlockCapabilityProviderRegistry();
        }
        return INSTANCE;
    }

    public BlockCapabilityProviderRegistry() {
        registry = new HashMap<>();
    }

    public static void register(Identifier identifier, BlockCapabilityProvider provider) {
        HashMap<Identifier, ArrayList<BlockCapabilityProvider>> r = getInstance().registry;

        if (!r.containsKey(identifier)) {
            r.put(identifier, new ArrayList<>());
        }

        r.get(identifier).add(provider);
        NyaLib.LOGGER.info("Provider {} registered for capability {}.", provider.getClass().getName(), identifier);
    }

    /**
     * Get the given capability of this block
     *
     * @param world      The world the block is in
     * @param x          x-coordinate of the block
     * @param y          y-coordinate of the block
     * @param z          z-coordinate of the block
     * @param identifier The identifier of the wanted capability
     * @return The requested capability if it was found. <code>null</code> if it was not.
     */
    public static @Nullable BlockCapability getCapability(World world, int x, int y, int z, Identifier identifier) {
        for (BlockCapabilityProvider provider : getProviders(identifier)) {
            BlockCapability blockCapability = provider.getCapability(world, x, y, z);

            if (blockCapability != null) {
                return blockCapability;
            }
        }
        return null;
    }

    public static ArrayList<BlockCapabilityProvider> getProviders(Identifier identifier) {
        return getInstance().registry.getOrDefault(identifier, new ArrayList<>());
    }
}
