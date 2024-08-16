package net.danygames2014.nyalib.network;

import net.modificationstation.stationapi.api.util.Identifier;

import java.util.HashMap;

public class NetworkTypeRegistry {
    private final HashMap<Identifier, NetworkType> registry;
    private static NetworkTypeRegistry INSTANCE;

    public static NetworkTypeRegistry getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NetworkTypeRegistry();
        }
        return INSTANCE;
    }

    public NetworkTypeRegistry() {
        this.registry = new HashMap<>();
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

    public static HashMap<Identifier, NetworkType> getRegistry() {
        return getInstance().registry;
    }
}
