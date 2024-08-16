package net.danygames2014.nyalib.network;

import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;

public interface NetworkComponent {
    Identifier getNetworkTypeIdentifier();

    default ArrayList<Identifier> getNetworkTypeIdentifiers(){
        ArrayList<Identifier> identifiers = new ArrayList<>();
        identifiers.add(getNetworkTypeIdentifier());
        return identifiers;
    }

    void update(int x, int y, int z, Network network, World world);

    void onAddedToNet(int x, int y, int z, Network network, World world);

    void onRemovedFromNet(int x, int y, int z, Network network, World world);
}
