package net.danygames2014.nyalib.network;

import net.danygames2014.nyalib.NyaLib;
import net.modificationstation.stationapi.api.util.Identifier;

public enum NetworkType {
    ENERGY(NyaLib.NAMESPACE.id("energy"));


    private final Identifier type;

    NetworkType(Identifier type) {
        this.type = type;
    }

    public Identifier getType() {
        return type;
    }
}
