package net.danygames2014.nyalibtest;

import net.danygames2014.nyalib.event.NetworkTypeRegistryEvent;
import net.danygames2014.nyalib.network.NetworkType;
import net.danygames2014.nyalibtest.block.CableBlock;
import net.danygames2014.nyalibtest.block.EnergyReceiverBlock;
import net.danygames2014.nyalibtest.block.InfiniteEnergyBlock;
import net.danygames2014.nyalibtest.block.SideHopperBlock;
import net.danygames2014.nyalibtest.blockentity.EnergyReceiverBlockEntity;
import net.danygames2014.nyalibtest.blockentity.InfiniteEnergyBlockEntity;
import net.danygames2014.nyalibtest.blockentity.SideHopperBlockEntity;
import net.danygames2014.nyalibtest.network.BasicNetworkType;
import net.danygames2014.nyalibtest.network.EnergyNetwork;
import net.danygames2014.nyalibtest.network.EnergyNetworkType;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.event.block.entity.BlockEntityRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;

@SuppressWarnings("unused")
public class NyaLibTest {
    @Entrypoint.Namespace
    public static final Namespace NAMESPACE = Null.get();

    public static Block infiniteEnergyBlock;
    public static Block energyReceiverBlock;
    public static Block sideHopper;
    public static Block cableBlock;

    public static NetworkType basicNetworkType;
    public static NetworkType energyNetworkType;

    @EventListener
    public void registerBlock(BlockRegistryEvent event){
        infiniteEnergyBlock = new InfiniteEnergyBlock(NAMESPACE.id("infinite_energy")).setTranslationKey(NAMESPACE, "infinite_energy");
        energyReceiverBlock = new EnergyReceiverBlock(NAMESPACE.id("energy_receiver")).setTranslationKey(NAMESPACE, "energy_receiver");
        sideHopper = new SideHopperBlock(NAMESPACE.id("side_hopper")).setTranslationKey(NAMESPACE, "side_hopper");
        cableBlock = new CableBlock(NAMESPACE.id("cable_block")).setTranslationKey(NAMESPACE, "cable_block");
    }

    @EventListener
    public void registerBlockEntites(BlockEntityRegisterEvent event){
        event.register(InfiniteEnergyBlockEntity.class, "infinite_energy");
        event.register(EnergyReceiverBlockEntity.class, "energy_receiver");
        event.register(SideHopperBlockEntity.class, "side_hopper");
    }

    @EventListener
    public void registerNetworkTypes(NetworkTypeRegistryEvent event){
        event.register(basicNetworkType = new BasicNetworkType(NAMESPACE.id("basic")));
        event.register(energyNetworkType = new EnergyNetworkType(NAMESPACE.id("energy"), EnergyNetwork.class));
    }
}
