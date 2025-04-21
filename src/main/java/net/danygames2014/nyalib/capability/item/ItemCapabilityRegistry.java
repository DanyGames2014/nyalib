package net.danygames2014.nyalib.capability.item;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.util.ClassUtil;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings({"rawtypes", "StringConcatenationArgumentToLogCall", "DuplicatedCode", "LoggingSimilarMessage", "unchecked"})
public class ItemCapabilityRegistry {
    private static ItemCapabilityRegistry INSTANCE;

    private final HashMap<Identifier, CapabilityRegistryEntry> providerRegistry;
    private final HashMap<Class, CapabilityRegistryEntry> registryEntryClassLookup;

    public static ItemCapabilityRegistry getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ItemCapabilityRegistry();
        }

        return INSTANCE;
    }

    public ItemCapabilityRegistry() {
        providerRegistry = new HashMap<>();
        registryEntryClassLookup = new HashMap<>();
    }

    public static void registerCapabilityClass(Identifier identifier, Class<? extends ItemCapability> capabilityClass) {
        HashMap<Identifier, CapabilityRegistryEntry> r = getInstance().providerRegistry;
        HashMap<Class, CapabilityRegistryEntry> lookup = getInstance().registryEntryClassLookup;

        if (r.containsKey(identifier)) {
            NyaLib.LOGGER.warn("Attempted to register a item capability class for " + identifier + " but a class has already been registered before.");
            NyaLib.LOGGER.warn("Already registered class:" + r.get(identifier).capabilityClass.getName());
            NyaLib.LOGGER.warn("Class being registered:" + r.get(identifier).capabilityClass.getName());
            return;
        }

        r.put(identifier, new CapabilityRegistryEntry(identifier, new ArrayList<>(), capabilityClass));
        lookup.put(capabilityClass, r.get(identifier));
        NyaLib.LOGGER.info("Registered item capability class " + capabilityClass.getName() + " for " + identifier);
    }

    public static void register(Identifier identifier, ItemCapabilityProvider provider) {
        HashMap<Identifier, CapabilityRegistryEntry> r = getInstance().providerRegistry;
        HashMap<Class, CapabilityRegistryEntry> lookup = getInstance().registryEntryClassLookup;

        // Check if capability class has been registered
        if (!r.containsKey(identifier)) {
            // Get the T argument of the superclass of the provider
            Class<?> capabilityClass = ClassUtil.getGenericSuperclass(provider, ItemCapability.class);

            // capabilityClass instanceof ItemCapability
            if (ItemCapability.class.isAssignableFrom(capabilityClass)) {
                // If the capabilityClass is instance of ItemCapability, we accept it and register it
                // Altho this does 100% guarantee its correct, it's the best we got
                r.put(identifier, new CapabilityRegistryEntry(identifier, new ArrayList<>(), (Class<? extends ItemCapability>) capabilityClass));
                lookup.put(capabilityClass, r.get(identifier));
                NyaLib.LOGGER.warn("Provider for " + identifier + " is being registered, but no capability class has been registered. Using the " + capabilityClass + " class.");
            } else {
                // The automatic lookup somehow didn't provide a class that extends ItemCapability. 
                // Honestly, we give up at this point, this whole endeavour is correcting improper registration anyway.
                NyaLib.LOGGER.error("Fallback lookup for item capability class for " + identifier + " failed");
                return;
            }
        }

        r.get(identifier).providers.add(provider);
        NyaLib.LOGGER.info("Provider " + provider.getClass().getName() + " registered for capability " + identifier);
    }

    /**
     * Get the given capability of this stack
     *
     * @param stack      The stack to get capability of
     * @param identifier The identifier of the wanted capability
     * @return The requested capability if it was found. <code>null</code> if it was not.
     */
    public static <T extends ItemCapability> @Nullable T getCapability(ItemStack stack, Identifier identifier) {
        CapabilityRegistryEntry registryEntry = getCapabilityRegistryEntry(identifier);

        if (registryEntry == null) {
            NyaLib.LOGGER.warn("Non-existent item capability requested: " + identifier);
            return null;
        }

        for (ItemCapabilityProvider provider : registryEntry.providers) {
            ItemCapability ItemCapability = provider.getCapability(stack);

            // If its null, try another provider
            if (ItemCapability == null) {
                continue;
            }

            // If the capability class is not assignable from the registred capability class
            // ItemCapability instanceof registryEntry.capabilityClass
            if (!registryEntry.capabilityClass.isAssignableFrom(ItemCapability.getClass())) {
                NyaLib.LOGGER.warn("Provider " + ItemCapability.getClass().getName() + " does not implement " + getCapabilityRegistryEntry(identifier));
                continue;
            }

            return (T) ItemCapability;
        }

        return null;
    }

    /**
     * Get the given capability of this stack
     *
     * @param stack           The stack to get capability of
     * @param capabilityClass The capability class
     * @return The requested capability if it was found. <code>null</code> if it was not.
     */
    public static <T extends ItemCapability> @Nullable T getCapability(ItemStack stack, Class<T> capabilityClass) {
        CapabilityRegistryEntry registryEntry = getCapabilityRegistryEntry(capabilityClass);

        if (registryEntry == null) {
            NyaLib.LOGGER.warn("Non-existent ItemStack capability requested: " + capabilityClass.getName());
            return null;
        }

        for (ItemCapabilityProvider provider : registryEntry.providers) {
            ItemCapability ItemCapability = provider.getCapability(stack);

            // If its null, try another provider
            if (ItemCapability == null) {
                continue;
            }

            // Check if the capability class matches the requested capability class
            // ItemCapability instance of capabilityClass
            if (!capabilityClass.isAssignableFrom(ItemCapability.getClass())) {
                NyaLib.LOGGER.warn("Provider " + ItemCapability.getClass().getName() + " does not implement " + capabilityClass);
                continue;
            }

            return capabilityClass.cast(ItemCapability);
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
        public ArrayList<ItemCapabilityProvider> providers;
        public Class<? extends ItemCapability> capabilityClass;


        public CapabilityRegistryEntry(Identifier identifier, ArrayList<ItemCapabilityProvider> providers, Class<? extends ItemCapability> capabilityClass) {
            this.providers = providers;
            this.capabilityClass = capabilityClass;
        }
    }
}
