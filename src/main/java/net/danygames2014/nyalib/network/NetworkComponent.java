package net.danygames2014.nyalib.network;

import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;

public interface NetworkComponent {
    Identifier getNetworkTypeIdentifier();

    default ArrayList<Identifier> getNetworkTypeIdentifiers(){
        ArrayList<Identifier> identifiers = new ArrayList<>();
        identifiers.add(getNetworkTypeIdentifier());
        return identifiers;
    }
}
