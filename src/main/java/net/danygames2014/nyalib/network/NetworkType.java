package net.danygames2014.nyalib.network;

import net.modificationstation.stationapi.api.util.Identifier;

public class NetworkType {
    // Base Network Types
    public static NetworkType ENERGY;
    
    Identifier identifier;
    Class<? extends Network> networkClass;

    public NetworkType(Identifier identifier, Class<? extends Network> networkClass) {
        this.identifier = identifier;
        this.networkClass = networkClass;
    }

    public NetworkType(Identifier identifier) {
        this(identifier, Network.class);
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public Class<? extends Network> getNetworkClass() {
        return networkClass;
    }
}
