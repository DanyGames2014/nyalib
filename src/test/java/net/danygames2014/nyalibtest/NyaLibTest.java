package net.danygames2014.nyalibtest;

import net.danygames2014.nyalibtest.block.InfiniteEnergyBlock;
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

    @EventListener
    public void registerBlock(BlockRegistryEvent event){
        infiniteEnergyBlock = new InfiniteEnergyBlock(NAMESPACE.id("infinite_energy_block"));
    }

    @EventListener
    public void registerBlockEntites(BlockEntityRegisterEvent event){
        event.register(InfiniteEnergyBlockEntity.class, "infinite_energy");
    }
}
