package net.danygames2014.nyalib.multipart;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.modificationstation.stationapi.api.util.Identifier;

public class MultipartComponentRegistry {
    public final Object2ObjectOpenHashMap<Identifier, Entry> registry;
    public final Object2ObjectOpenHashMap<Class<? extends MultipartComponent>, Identifier> componentClassToIdentifier = new Object2ObjectOpenHashMap<>();
    private static MultipartComponentRegistry INSTANCE;

    public static MultipartComponentRegistry getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MultipartComponentRegistry();
        }
        return INSTANCE;
    }

    public MultipartComponentRegistry() {
        this.registry = new Object2ObjectOpenHashMap<>();
    }

    public static void register(Identifier identifier, Class<? extends MultipartComponent> componentClass, MultipartComponentFactory componentFactory) {
        if (getInstance().registry.containsKey(identifier)) {
            return;
        }

        getInstance().registry.put(identifier, new Entry(componentFactory, componentClass));
        getInstance().componentClassToIdentifier.put(componentClass, identifier);
    }

    public static Entry get(Identifier identifier) {
        return getInstance().registry.getOrDefault(identifier, null);
    }
    
    public static Identifier getIdentifier(Class<? extends MultipartComponent> componentClass) {
        return getInstance().componentClassToIdentifier.getOrDefault(componentClass, null);
    }

    public static Object2ObjectOpenHashMap<Identifier, Entry> getRegistry() {
        return getInstance().registry;
    }
    
    public static class Entry {
        public MultipartComponentFactory factory;
        public Class<? extends MultipartComponent> componentClass;

        public Entry(MultipartComponentFactory factory, Class<? extends MultipartComponent> componentClass) {
            this.factory = factory;
            this.componentClass = componentClass;
        }
    }
}
