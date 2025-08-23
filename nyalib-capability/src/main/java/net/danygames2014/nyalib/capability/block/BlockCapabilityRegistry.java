package net.danygames2014.nyalib.capability.block;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.util.ClassUtil;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings({"rawtypes", "StringConcatenationArgumentToLogCall", "DuplicatedCode", "unchecked", "LoggingSimilarMessage"})
public class BlockCapabilityRegistry {
    private static BlockCapabilityRegistry INSTANCE;

    private final HashMap<Identifier, CapabilityRegistryEntry> providerRegistry;
    private final HashMap<Class, CapabilityRegistryEntry> registryEntryClassLookup;

    public static BlockCapabilityRegistry getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BlockCapabilityRegistry();
        }

        return INSTANCE;
    }

    public BlockCapabilityRegistry() {
        providerRegistry = new HashMap<>();
        registryEntryClassLookup = new HashMap<>();
    }

    public static void registerCapabilityClass(Identifier identifier, Class<? extends BlockCapability> capabilityClass) {
        HashMap<Identifier, CapabilityRegistryEntry> r = getInstance().providerRegistry;
        HashMap<Class, CapabilityRegistryEntry> lookup = getInstance().registryEntryClassLookup;

        if (r.containsKey(identifier)) {
            NyaLib.LOGGER.warn("Attempted to register a block capability class for " + identifier + " but a class has already been registered before.");
            NyaLib.LOGGER.warn("Already registered class:" + r.get(identifier).capabilityClass.getName());
            NyaLib.LOGGER.warn("Class being registered:" + r.get(identifier).capabilityClass.getName());
            return;
        }

        r.put(identifier, new CapabilityRegistryEntry(identifier, new ArrayList<>(), capabilityClass));
        lookup.put(capabilityClass, r.get(identifier));
        NyaLib.LOGGER.info("Registered block capability class " + capabilityClass.getName() + " for " + identifier);
    }

    public static void register(Identifier identifier, BlockCapabilityProvider provider) {
        HashMap<Identifier, CapabilityRegistryEntry> r = getInstance().providerRegistry;
        HashMap<Class, CapabilityRegistryEntry> lookup = getInstance().registryEntryClassLookup;

        // Check if capability class has been registered
        if (!r.containsKey(identifier)) {
            // Get the T argument of the superclass of the provider
            Class<?> capabilityClass = ClassUtil.getGenericSuperclass(provider, BlockCapability.class);

            // capabilityClass instanceof BlockCapability
            if (BlockCapability.class.isAssignableFrom(capabilityClass)) {
                // If the capabilityClass is instance of BlockCapability, we accept it and register it
                // Altho this does 100% guarantee its correct, it's the best we got
                r.put(identifier, new CapabilityRegistryEntry(identifier, new ArrayList<>(), (Class<? extends BlockCapability>) capabilityClass));
                lookup.put(capabilityClass, r.get(identifier));
                NyaLib.LOGGER.warn("Provider for " + identifier + " is being registered, but no capability class has been registered. Using the " + capabilityClass + " class.");
            } else {
                // The automatic lookup somehow didn't provide a class that extends BlockCapability. 
                // Honestly, we give up at this point, this whole endeavour is correcting improper registration anyway.
                NyaLib.LOGGER.error("Fallback lookup for block capability class for " + identifier + " failed");
                return;
            }
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
    public static <T extends BlockCapability> @Nullable T getCapability(World world, int x, int y, int z, Identifier identifier) {
        CapabilityRegistryEntry registryEntry = getCapabilityRegistryEntry(identifier);

        if (registryEntry == null) {
            NyaLib.LOGGER.warn("Non-existent block capability requested: " + identifier);
            return null;
        }

        for (BlockCapabilityProvider provider : registryEntry.providers) {
            BlockCapability blockCapability = provider.getCapability(world, x, y, z);

            // If its null, try another provider
            if (blockCapability == null) {
                continue;
            }

//                System.err.println(registryEntry.capabilityClass.getSimpleName() + " assignable from " + blockCapability.getClass().getSimpleName() + ": " + registryEntry.capabilityClass.isAssignableFrom(blockCapability.getClass()));
//                System.err.println(blockCapability.getClass().getSimpleName() + " assignable from " + registryEntry.capabilityClass.getSimpleName() + ": " + blockCapability.getClass().isAssignableFrom(registryEntry.capabilityClass));
//
//                System.err.println(registryEntry.capabilityClass.getSimpleName() + " assignable from " + BlockCapability.class.getSimpleName() + ": " + registryEntry.capabilityClass.isAssignableFrom(BlockCapability.class));
//                System.err.println(BlockCapability.class.getSimpleName() + " assignable from " + registryEntry.capabilityClass.getSimpleName() + ": " + BlockCapability.class.isAssignableFrom(registryEntry.capabilityClass));

            // If the capability class is not assignable from the registred capability class
            // blockCapability instanceof registryEntry.capabilityClass
            if (!registryEntry.capabilityClass.isAssignableFrom(blockCapability.getClass())) {
                NyaLib.LOGGER.warn("Provider " + blockCapability.getClass().getName() + " does not implement " + getCapabilityRegistryEntry(identifier));
                continue;
            }

            return (T) blockCapability;
        }

        return null;
    }

    /**
     * Get the given capability of this block
     *
     * @param world           The world the block is in
     * @param x               x-coordinate of the block
     * @param y               y-coordinate of the block
     * @param z               z-coordinate of the block
     * @param capabilityClass The capability class
     * @return The requested capability if it was found. <code>null</code> if it was not.
     */
    public static <T extends BlockCapability> @Nullable T getCapability(World world, int x, int y, int z, Class<T> capabilityClass) {
        CapabilityRegistryEntry registryEntry = getCapabilityRegistryEntry(capabilityClass);

        if (registryEntry == null) {
            NyaLib.LOGGER.warn("Non-existent block capability requested: " + capabilityClass.getName());
            return null;
        }

        for (BlockCapabilityProvider provider : registryEntry.providers) {
            BlockCapability blockCapability = provider.getCapability(world, x, y, z);

            // If its null, try another provider
            if (blockCapability == null) {
                continue;
            }

            // Check if the capability class matches the requested capability class
            // blockCapability instance of capabilityClass
            if (!capabilityClass.isAssignableFrom(blockCapability.getClass())) {
                NyaLib.LOGGER.warn("Provider " + blockCapability.getClass().getName() + " does not implement " + capabilityClass);
                continue;
            }

            return capabilityClass.cast(blockCapability);
        }

        return null;
    }

    public static CapabilityRegistryEntry getCapabilityRegistryEntry(Identifier identifier) {
        return getInstance().providerRegistry.get(identifier);
    }

    public static CapabilityRegistryEntry getCapabilityRegistryEntry(Class capabilityClass) {
        return getInstance().registryEntryClassLookup.get(capabilityClass);
    }

    public static class CapabilityRegistryEntry {
        public Identifier identifier;
        public ArrayList<BlockCapabilityProvider> providers;
        public Class<? extends BlockCapability> capabilityClass;


        public CapabilityRegistryEntry(Identifier identifier, ArrayList<BlockCapabilityProvider> providers, Class<? extends BlockCapability> capabilityClass) {
            this.providers = providers;
            this.capabilityClass = capabilityClass;
        }
    }
}
