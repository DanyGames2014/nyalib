package net.danygames2014.nyalib.network;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.modificationstation.stationapi.api.util.Identifier;

@SuppressWarnings("unused")
public class NetworkTypeRegistry {
    private final Object2ObjectOpenHashMap<Identifier, NetworkType> registry;
    private static NetworkTypeRegistry INSTANCE;

    public static NetworkTypeRegistry getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NetworkTypeRegistry();
        }
        return INSTANCE;
    }

    public NetworkTypeRegistry() {
        this.registry = new Object2ObjectOpenHashMap<>();
    }

    public static void register(Identifier identifier, NetworkType networkType) {
        if (getInstance().registry.containsKey(identifier)) {
            return;
        }

        getInstance().registry.put(identifier, networkType);
    }

    public static NetworkType get(Identifier identifier) {
        return getInstance().registry.getOrDefault(identifier, null);
    }

    public static Object2ObjectOpenHashMap<Identifier, NetworkType> getRegistry() {
        return getInstance().registry;
    }
}
