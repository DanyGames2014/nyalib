package net.danygames2014.nyalibtest;

import net.danygames2014.nyalib.block.FenceBlockTemplate;
import net.danygames2014.nyalib.block.FenceGateBlockTemplate;
import net.danygames2014.nyalib.block.SlabBlockTemplate;
import net.danygames2014.nyalib.block.StairsBlockTemplate;
import net.danygames2014.nyalib.event.NetworkTypeRegistryEvent;
import net.danygames2014.nyalib.network.NetworkType;
import net.danygames2014.nyalibtest.block.energy.EnergyConsumerBlock;
import net.danygames2014.nyalibtest.block.energy.EnergySourceBlock;
import net.danygames2014.nyalibtest.block.energy.WireBlock;
import net.danygames2014.nyalibtest.block.energy.entity.EnergyConsumerBlockEntity;
import net.danygames2014.nyalibtest.block.energy.entity.EnergySourceBlockEntity;
import net.danygames2014.nyalibtest.block.fluid.FluidTankBlock;
import net.danygames2014.nyalibtest.block.fluid.InfiniteWaterBlock;
import net.danygames2014.nyalibtest.block.item.SideHopperBlock;
import net.danygames2014.nyalibtest.block.network.CableBlock;
import net.danygames2014.nyalibtest.block.network.EastWestCableBlock;
import net.danygames2014.nyalibtest.block.network.NetworkEdgeBlock;
import net.danygames2014.nyalibtest.block.simpleenergy.SimpleEnergyReceiverBlock;
import net.danygames2014.nyalibtest.block.simpleenergy.SimpleInfiniteEnergyBlock;
import net.danygames2014.nyalibtest.block.fluid.entity.FluidTankBlockEntity;
import net.danygames2014.nyalibtest.block.fluid.entity.InfiniteWaterBlockEntity;
import net.danygames2014.nyalibtest.block.item.entity.SideHopperBlockEntity;
import net.danygames2014.nyalibtest.block.simpleenergy.entity.InfiniteSimpleEnergyBlockEntity;
import net.danygames2014.nyalibtest.block.simpleenergy.entity.SimpleEnergyReceiverBlockEntity;
import net.danygames2014.nyalibtest.block.sound.ServerSoundBlock;
import net.danygames2014.nyalibtest.network.BasicNetworkType;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.event.block.entity.BlockEntityRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;

@SuppressWarnings("unused")
public class NyaLibTest {
    @Entrypoint.Namespace
    public static final Namespace NAMESPACE = Null.get();

    public static Block simpleInfiniteEnergyBlock;
    public static Block simpleEnergyReceiverBlock;
    
    public static Block sideHopper;
    
    public static Block networkNodeBlock;
    public static Block eastWestNetworkNodeBlock;
    public static Block networkEdgeBlock;
    
    public static Block energyGeneratorBlock;
    public static Block energyConsumerBlock;
    public static Block energyWireBlock;
    
    public static Block fluidTankBlock;
    public static Block infiniteWaterBlock;
    
    public static Block spongeStairs;
    public static Block spongeSlab;
    public static Block spongeFence;
    public static Block spongeFenceGate;
    
    public static Block serverSoundBlock;

    public static NetworkType basicNetworkType;

    @EventListener
    public void registerBlock(BlockRegistryEvent event){
        // Simple Energy
        simpleInfiniteEnergyBlock = new SimpleInfiniteEnergyBlock(NAMESPACE.id("simple_infinite_energy")).setTranslationKey(NAMESPACE, "simple_infinite_energy");
        simpleEnergyReceiverBlock = new SimpleEnergyReceiverBlock(NAMESPACE.id("simple_energy_receiver")).setTranslationKey(NAMESPACE, "simple_energy_receiver");
        
        // Item
        sideHopper = new SideHopperBlock(NAMESPACE.id("side_hopper")).setTranslationKey(NAMESPACE, "side_hopper");
        
        // Block Networks
        networkNodeBlock = new CableBlock(NAMESPACE.id("network_node_block")).setTranslationKey(NAMESPACE, "network_node_block");
        eastWestNetworkNodeBlock = new EastWestCableBlock(NAMESPACE.id("east_west_network_node")).setTranslationKey(NAMESPACE, "east_west_network_node");
        networkEdgeBlock = new NetworkEdgeBlock(NAMESPACE.id("network_edge")).setTranslationKey(NAMESPACE, "network_edge");
        
        // Fluid
        fluidTankBlock = new FluidTankBlock(NAMESPACE.id("fluid_tank")).setTranslationKey(NAMESPACE, "fluid_tank");
        infiniteWaterBlock = new InfiniteWaterBlock(NAMESPACE.id("infinite_water_tank")).setTranslationKey(NAMESPACE, "infinite_water_tank");
        
        // Energy
        energyGeneratorBlock = new EnergySourceBlock(NAMESPACE.id("energy_source"), Material.METAL).setTranslationKey(NAMESPACE, "energy_source");
        energyConsumerBlock = new EnergyConsumerBlock(NAMESPACE.id("energy_consumer"), Material.METAL).setTranslationKey(NAMESPACE, "energy_consumer");
        energyWireBlock = new WireBlock(NAMESPACE.id("energy_wire")).setTranslationKey(NAMESPACE, "energy_wire");
        
        // Block Templates
        spongeStairs = new StairsBlockTemplate(NAMESPACE.id("sponge_stairs"), Block.SPONGE).setTranslationKey(NAMESPACE, "sponge_stairs");
        spongeSlab = new SlabBlockTemplate(NAMESPACE.id("sponge_slab"), Block.SPONGE).setTranslationKey(NAMESPACE, "sponge_slab");
        spongeFence = new FenceBlockTemplate(NAMESPACE.id("sponge_fence"), Block.SPONGE).setTranslationKey(NAMESPACE, "sponge_fence");
        spongeFenceGate = new FenceGateBlockTemplate(NAMESPACE.id("sponge_fence_gate"), Block.SPONGE).setTranslationKey(NAMESPACE, "sponge_fence_gate");
        
        // Sound
        serverSoundBlock = new ServerSoundBlock(NAMESPACE.id("server_sound_block"), Material.WOOL).setTranslationKey(NAMESPACE, "server_sound_block");
    }

    @EventListener
    public void registerBlockEntites(BlockEntityRegisterEvent event){
        event.register(InfiniteSimpleEnergyBlockEntity.class, "simple_infinite_energy");
        event.register(SimpleEnergyReceiverBlockEntity.class, "simple_energy_receiver");
        event.register(SideHopperBlockEntity.class, "side_hopper");
        event.register(FluidTankBlockEntity.class, "fluid_tank");
        event.register(InfiniteWaterBlockEntity.class, "infinite_water_block");
        event.register(EnergySourceBlockEntity.class, "energy_source");
        event.register(EnergyConsumerBlockEntity.class, "energy_consumer");
    }

    @EventListener
    public void registerNetworkTypes(NetworkTypeRegistryEvent event){
        event.register(basicNetworkType = new BasicNetworkType(NAMESPACE.id("basic")));
    }
}
