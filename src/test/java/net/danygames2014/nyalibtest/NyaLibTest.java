package net.danygames2014.nyalibtest;

import net.danygames2014.nyalibtest.block.EnergyReceiverBlock;
import net.danygames2014.nyalibtest.block.InfiniteEnergyBlock;
import net.danygames2014.nyalibtest.blockentity.EnergyReceiverBlockEntity;
import net.danygames2014.nyalibtest.blockentity.InfiniteEnergyBlockEntity;
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

    @EventListener
    public void registerBlock(BlockRegistryEvent event){
        infiniteEnergyBlock = new InfiniteEnergyBlock(NAMESPACE.id("infinite_energy")).setTranslationKey(NAMESPACE, "infinite_energy");
        energyReceiverBlock = new EnergyReceiverBlock(NAMESPACE.id("energy_receiver")).setTranslationKey(NAMESPACE, "energy_receiver");
    }

    @EventListener
    public void registerBlockEntites(BlockEntityRegisterEvent event){
        event.register(InfiniteEnergyBlockEntity.class, "infinite_energy");
        event.register(EnergyReceiverBlockEntity.class, "energy_receiver");
    }
}
