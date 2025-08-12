package net.danygames2014.nyalib.capability.entity;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.util.ClassUtil;
import net.minecraft.entity.Entity;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings({"rawtypes", "StringConcatenationArgumentToLogCall", "DuplicatedCode", "LoggingSimilarMessage", "unchecked"})
public class EntityCapabilityRegistry {
    private static EntityCapabilityRegistry INSTANCE;

    private final HashMap<Identifier, CapabilityRegistryEntry> providerRegistry;
    private final HashMap<Class, CapabilityRegistryEntry> registryEntryClassLookup;

    public static EntityCapabilityRegistry getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EntityCapabilityRegistry();
        }

        return INSTANCE;
    }

    public EntityCapabilityRegistry() {
        providerRegistry = new HashMap<>();
        registryEntryClassLookup = new HashMap<>();
    }

    public static void registerCapabilityClass(Identifier identifier, Class<? extends EntityCapability> capabilityClass) {
        HashMap<Identifier, CapabilityRegistryEntry> r = getInstance().providerRegistry;
        HashMap<Class, CapabilityRegistryEntry> lookup = getInstance().registryEntryClassLookup;

        if (r.containsKey(identifier)) {
            NyaLib.LOGGER.warn("Attempted to register a entity capability class for " + identifier + " but a class has already been registered before.");
            NyaLib.LOGGER.warn("Already registered class:" + r.get(identifier).capabilityClass.getName());
            NyaLib.LOGGER.warn("Class being registered:" + r.get(identifier).capabilityClass.getName());
            return;
        }

        r.put(identifier, new CapabilityRegistryEntry(identifier, new ArrayList<>(), capabilityClass));
        lookup.put(capabilityClass, r.get(identifier));
        NyaLib.LOGGER.info("Registered entity capability class " + capabilityClass.getName() + " for " + identifier);
    }

    public static void register(Identifier identifier, EntityCapabilityProvider provider) {
        HashMap<Identifier, CapabilityRegistryEntry> r = getInstance().providerRegistry;
        HashMap<Class, CapabilityRegistryEntry> lookup = getInstance().registryEntryClassLookup;

        // Check if capability class has been registered
        if (!r.containsKey(identifier)) {
            // Get the T argument of the superclass of the provider
            Class<?> capabilityClass = ClassUtil.getGenericSuperclass(provider, EntityCapability.class);

            // capabilityClass instanceof EntityCapability
            if (EntityCapability.class.isAssignableFrom(capabilityClass)) {
                // If the capabilityClass is instance of EntityCapability, we accept it and register it
                // Altho this does 100% guarantee its correct, it's the best we got
                r.put(identifier, new CapabilityRegistryEntry(identifier, new ArrayList<>(), (Class<? extends EntityCapability>) capabilityClass));
                lookup.put(capabilityClass, r.get(identifier));
                NyaLib.LOGGER.warn("Provider for " + identifier + " is being registered, but no capability class has been registered. Using the " + capabilityClass + " class.");
            } else {
                // The automatic lookup somehow didn't provide a class that extends EntityCapability. 
                // Honestly, we give up at this point, this whole endeavour is correcting improper registration anyway.
                NyaLib.LOGGER.error("Fallback lookup for entity capability class for " + identifier + " failed");
                return;
            }
        }

        r.get(identifier).providers.add(provider);
        NyaLib.LOGGER.info("Provider " + provider.getClass().getName() + " registered for capability " + identifier);
    }

    /**
     * Get the given capability of this entity
     *
     * @param entity     The entity
     * @param identifier The identifier of the wanted capability
     * @return The requested capability if it was found. <code>null</code> if it was not.
     */
    public static <T extends EntityCapability> @Nullable T getCapability(Entity entity, Identifier identifier) {
        CapabilityRegistryEntry registryEntry = getCapabilityRegistryEntry(identifier);

        if (registryEntry == null) {
            NyaLib.LOGGER.warn("Non-existent entity capability requested: " + identifier);
            return null;
        }

        for (EntityCapabilityProvider provider : registryEntry.providers) {
            EntityCapability EntityCapability = provider.getCapability(entity);

            // If its null, try another provider
            if (EntityCapability == null) {
                continue;
            }

            // If the capability class is not assignable from the registred capability class
            // EntityCapability instanceof registryEntry.capabilityClass
            if (!registryEntry.capabilityClass.isAssignableFrom(EntityCapability.getClass())) {
                NyaLib.LOGGER.warn("Provider " + EntityCapability.getClass().getName() + " does not implement " + getCapabilityRegistryEntry(identifier));
                continue;
            }

            return (T) EntityCapability;
        }

        return null;
    }

    /**
     * Get the given capability of this entity
     *
     * @param entity          The entity
     * @param capabilityClass The capability class
     * @return The requested capability if it was found. <code>null</code> if it was not.
     */
    public static <T extends EntityCapability> @Nullable T getCapability(Entity entity, Class<T> capabilityClass) {
        CapabilityRegistryEntry registryEntry = getCapabilityRegistryEntry(capabilityClass);

        if (registryEntry == null) {
            NyaLib.LOGGER.warn("Non-existent entity capability requested: " + capabilityClass.getName());
            return null;
        }

        for (EntityCapabilityProvider provider : registryEntry.providers) {
            EntityCapability EntityCapability = provider.getCapability(entity);

            // If its null, try another provider
            if (EntityCapability == null) {
                continue;
            }

            // Check if the capability class matches the requested capability class
            // EntityCapability instance of capabilityClass
            if (!capabilityClass.isAssignableFrom(EntityCapability.getClass())) {
                NyaLib.LOGGER.warn("Provider " + EntityCapability.getClass().getName() + " does not implement " + capabilityClass);
                continue;
            }

            return capabilityClass.cast(EntityCapability);
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
        public ArrayList<EntityCapabilityProvider> providers;
        public Class<? extends EntityCapability> capabilityClass;


        public CapabilityRegistryEntry(Identifier identifier, ArrayList<EntityCapabilityProvider> providers, Class<? extends EntityCapability> capabilityClass) {
            this.providers = providers;
            this.capabilityClass = capabilityClass;
        }
    }
}
