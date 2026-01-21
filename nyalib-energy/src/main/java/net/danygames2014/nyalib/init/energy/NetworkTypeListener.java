package net.danygames2014.nyalib.init.energy;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.event.NetworkTypeRegistryEvent;
import net.danygames2014.nyalib.network.NetworkType;
import net.danygames2014.nyalib.network.energy.EnergyNetwork;
import net.danygames2014.nyalib.network.energy.EnergyNetworkType;
import net.mine_diver.unsafeevents.listener.EventListener;

public class NetworkTypeListener {
    public static NetworkType energyNetworkType;
    
    @EventListener
    public void registerNetworkTypes(NetworkTypeRegistryEvent event){
        event.register(NetworkType.ENERGY = energyNetworkType = new EnergyNetworkType(NyaLib.NAMESPACE.id("energy"), EnergyNetwork::new));
    }
}
