package net.danygames2014.nyalib.network;

import net.modificationstation.stationapi.api.util.Identifier;

public class NetworkType {
    // Base Network Types
    public static NetworkType ENERGY;
    
    Identifier identifier;
    NetworkFactory networkFactory;

    public NetworkType(Identifier identifier, NetworkFactory networkFactory) {
        this.identifier = identifier;
        this.networkFactory = networkFactory;
    }

    public NetworkType(Identifier identifier) {
        this(identifier, Network::new);
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public NetworkFactory getNetworkFactory() {
        return networkFactory;
    }
}
