package net.danygames2014.nyalibtest.network;

import net.danygames2014.nyalib.network.Network;
import net.danygames2014.nyalib.network.NetworkType;
import net.modificationstation.stationapi.api.util.Identifier;

public class EnergyNetworkType extends NetworkType {
    public EnergyNetworkType(Identifier identifier, Class<? extends Network> networkClass) {
        super(identifier, networkClass);
    }
}
