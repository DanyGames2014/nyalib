package net.danygames2014.nyalibtest;

import net.danygames2014.nyalib.block.*;
import net.danygames2014.nyalib.event.FluidRegistryEvent;
import net.danygames2014.nyalib.event.NetworkTypeRegistryEvent;
import net.danygames2014.nyalib.fluid.Fluid;
import net.danygames2014.nyalib.network.NetworkType;
import net.danygames2014.nyalibtest.block.capability.block.ItemHandlerBlockCapabilityTesterBlock;
import net.danygames2014.nyalibtest.block.capability.item.YoinkerItem;
import net.danygames2014.nyalibtest.block.energy.EnergyConsumerBlock;
import net.danygames2014.nyalibtest.block.energy.MultimeterItem;
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
import net.danygames2014.nyalibtest.block.screen.FluidTankScreen;
import net.danygames2014.nyalibtest.block.simpleenergy.SimpleEnergyReceiverBlock;
import net.danygames2014.nyalibtest.block.simpleenergy.SimpleInfiniteEnergyBlock;
import net.danygames2014.nyalibtest.block.fluid.entity.FluidTankBlockEntity;
import net.danygames2014.nyalibtest.block.fluid.entity.InfiniteWaterBlockEntity;
import net.danygames2014.nyalibtest.block.item.entity.SideHopperBlockEntity;
import net.danygames2014.nyalibtest.block.simpleenergy.entity.InfiniteSimpleEnergyBlockEntity;
import net.danygames2014.nyalibtest.block.simpleenergy.entity.SimpleEnergyReceiverBlockEntity;
import net.danygames2014.nyalibtest.block.sound.ServerSoundBlock;
import net.danygames2014.nyalibtest.network.BasicNetworkType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.PressurePlateActivationRule;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.client.gui.screen.GuiHandler;
import net.modificationstation.stationapi.api.event.block.entity.BlockEntityRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.GuiHandlerRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;

@SuppressWarnings("unused")
public class NyaLibTest {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    public static Block simpleInfiniteEnergyBlock;
    public static Block simpleEnergyReceiverBlock;
    
    public static Block sideHopper;
    
    public static Block networkNodeBlock;
    public static Block eastWestNetworkNodeBlock;
    public static Block networkEdgeBlock;
    
    public static Block energyGeneratorBlock;
    public static Block energyConsumerBlock;
    public static Block energyWireBlock;
    public static Item multimeter;
    
    public static Block fluidTankBlock;
    public static Block infiniteWaterBlock;
    
    public static Block spongeStairs;
    public static Block spongeSlab;
    public static Block spongeFence;
    public static Block spongeFenceGate;
    
    public static Block tntStairs;
    public static Block tntSlab;
    public static Block tntFence;
    public static Block tntFenceGate;
    public static Block tntButton;
    public static Block tntWall;
    public static Block tntPane;
    public static Block glassPane;
    public static Block tntPressurePlate;
    public static Block obsidianPressurePlate;
    
    public static Block serverSoundBlock;
    
    public static Block itemHandlerBlockCapabilityTester;
    public static Item itemYoinker;
    
    public static Fluid gravelFluid;
    public static Fluid fuelFluid;

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
        
        tntStairs = new StairsBlockTemplate(NAMESPACE.id("tnt_stairs"), Block.TNT, Identifier.of("minecraft:block/tnt_side")).setTranslationKey(NAMESPACE, "tnt_stairs").setHardness(0.5F);
        tntSlab = new SlabBlockTemplate(NAMESPACE.id("tnt_slab"), Block.TNT, Identifier.of("minecraft:block/tnt_side")).setTranslationKey(NAMESPACE, "tnt_slab").setHardness(0.5F);
        tntFence = new FenceBlockTemplate(NAMESPACE.id("tnt_fence"), Block.TNT, Identifier.of("minecraft:block/tnt_side")).setTranslationKey(NAMESPACE, "tnt_fence").setHardness(0.5F);
        tntFenceGate = new FenceGateBlockTemplate(NAMESPACE.id("tnt_fence_gate"), Block.TNT, Identifier.of("minecraft:block/tnt_side")).setTranslationKey(NAMESPACE.id("tnt_fence_gate")).setHardness(0.5F);
        tntButton = new ButtonBlockTemplate(NAMESPACE.id("tnt_button"), Block.TNT, Identifier.of("minecraft:block/tnt_side")).setTranslationKey(NAMESPACE, "tnt_button").setHardness(0.5F);
        tntWall = new WallBlockTemplate(NAMESPACE.id("tnt_wall"), Block.TNT, Identifier.of("minecraft:block/tnt_side")).setTranslationKey(NAMESPACE, "tnt_wall").setHardness(0.5F);
        tntPane = new PaneBlockTemplate(NAMESPACE.id("tnt_pane"), Block.TNT, Identifier.of("minecraft:block/tnt_side")).setTranslationKey(NAMESPACE, "tnt_pane").setHardness(0.5F);
        glassPane = new PaneBlockTemplate(NAMESPACE.id("glass_pane"), Block.GLASS, Identifier.of("minecraft:block/glass")).setTranslationKey(NAMESPACE, "glass_pane").setHardness(0.5F);
        tntPressurePlate = new PressurePlateBlockTemplate(NAMESPACE.id("tnt_pressure_plate"), Block.TNT, PressurePlateActivationRule.EVERYTHING, Identifier.of("minecraft:block/tnt_side")).setTranslationKey(NAMESPACE, "tnt_pressure_plate").setHardness(0.2F);
        obsidianPressurePlate = new PressurePlateBlockTemplate(NAMESPACE.id("obsidian_pressure_plate"), Block.TNT, PressurePlateActivationRule.PLAYERS, Identifier.of("minecraft:block/obsidian")).setTranslationKey(NAMESPACE, "obsidian_pressure_plate").setHardness(0.2F);
        
        // Sound
        serverSoundBlock = new ServerSoundBlock(NAMESPACE.id("server_sound_block"), Material.WOOL).setTranslationKey(NAMESPACE, "server_sound_block");
        
        // Capability
        itemHandlerBlockCapabilityTester = new ItemHandlerBlockCapabilityTesterBlock(NAMESPACE.id("item_handler_block_capability")).setTranslationKey(NAMESPACE, "item_handler_block_capability");
    }
    
    @EventListener
    public void registerFluids(FluidRegistryEvent event) {
        event.register(gravelFluid = new Fluid(NAMESPACE.id("gravel"), Block.GRAVEL, Block.GRAVEL, 0xFF212121));
        event.register(fuelFluid = new Fluid(NAMESPACE.id("fuel"), NAMESPACE.id("block/fuel_still"), NAMESPACE.id("block/fuel_flowing"), NAMESPACE.id("block/fuel_overlay"), 0xFFFFE524));
        
        fuelFluid.setMovementSpeedMultiplier(1.00D);
    }
    
    @EventListener
    public void registerItems(ItemRegistryEvent event){
        multimeter = new MultimeterItem(NAMESPACE.id("multimeter")).setTranslationKey(NAMESPACE, "multimeter");
        itemYoinker = new YoinkerItem(NAMESPACE.id("item_yoinker")).setTranslationKey(NAMESPACE, "item_yoinker");
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

    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerScreenHandlers(GuiHandlerRegistryEvent event) {
        event.register(NAMESPACE.id("fluid_tank"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openFluidTank, FluidTankBlockEntity::new));
    }

    @Environment(EnvType.CLIENT)
    private Screen openFluidTank(PlayerEntity player, Inventory inventory) {
        return new FluidTankScreen(player, (FluidTankBlockEntity) inventory);
    }
}
