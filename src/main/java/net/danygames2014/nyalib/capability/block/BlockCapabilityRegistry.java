package net.danygames2014.nyalib.capability.block;

import net.danygames2014.nyalib.NyaLib;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings({"rawtypes", "StringConcatenationArgumentToLogCall"})
public class BlockCapabilityRegistry {
    private static BlockCapabilityRegistry INSTANCE;

    private final HashMap<Identifier, CapabilityRegistryEntry> providerRegistry;

    public static BlockCapabilityRegistry getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BlockCapabilityRegistry();
        }

        return INSTANCE;
    }

    public BlockCapabilityRegistry() {
        providerRegistry = new HashMap<>();
    }

    public static void registerCapabilityClass(Identifier identifier, Class<? extends BlockCapability> capabilityClass) {
        HashMap<Identifier, CapabilityRegistryEntry> r = getInstance().providerRegistry;

        if (r.containsKey(identifier)) {
            NyaLib.LOGGER.warn("Attempted to register a capability class for " + identifier + " but a class has already been registered before.");
            NyaLib.LOGGER.warn("Already registered class:" + r.get(identifier).capabilityClass.getName());
            NyaLib.LOGGER.warn("Class being registered:" + r.get(identifier).capabilityClass.getName());
            return;
        }

        r.put(identifier, new CapabilityRegistryEntry(new ArrayList<>(), capabilityClass));
        NyaLib.LOGGER.info("Register block capability class " + capabilityClass.getName() + " for " + identifier);
    }

    public static void register(Identifier identifier, BlockCapabilityProvider provider) {
        HashMap<Identifier, CapabilityRegistryEntry> r = getInstance().providerRegistry;

        if (!r.containsKey(identifier)) {
            NyaLib.LOGGER.warn("Provider for " + identifier + " is being registered, but no capability class has been registered. Using default BlockCapability class.");
            r.put(identifier, new CapabilityRegistryEntry(new ArrayList<>(), BlockCapability.class));
        }

        r.get(identifier).providers.add(provider);
        NyaLib.LOGGER.info("Provider " + provider.getClass().getName() + " registered for capability " + identifier);
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

            // If its null, try another provider
            if (blockCapability == null) {
                continue;
            }
            
            // If the capability class is not assignable from the registred capability class
            if (!blockCapability.getClass().isAssignableFrom(provider.getClass())) {
                NyaLib.LOGGER.warn("Provider " + blockCapability.getClass().getName() + " does not implement " + provider.getClass().getName());
                continue;
            }
            
            return blockCapability;
        }
        return null;
    }

    public static ArrayList<BlockCapabilityProvider> getProviders(Identifier identifier) {
        if (getInstance().providerRegistry.containsKey(identifier)) {
            return getInstance().providerRegistry.get(identifier).providers;
        }
        return new ArrayList<>();
    }

    public static class CapabilityRegistryEntry {
        public ArrayList<BlockCapabilityProvider> providers;
        public Class<? extends BlockCapability> capabilityClass;


        public CapabilityRegistryEntry(ArrayList<BlockCapabilityProvider> providers, Class<? extends BlockCapability> capabilityClass) {
            this.providers = providers;
            this.capabilityClass = capabilityClass;
        }
    }
}
