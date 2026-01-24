package net.danygames2014.nyalibtest;

import net.danygames2014.nyalib.block.*;
import net.danygames2014.nyalib.event.AfterFluidRegistryEvent;
import net.danygames2014.nyalib.event.FluidRegistryEvent;
import net.danygames2014.nyalib.event.MultipartComponentRegistryEvent;
import net.danygames2014.nyalib.event.NetworkTypeRegistryEvent;
import net.danygames2014.nyalib.fluid.Fluid;
import net.danygames2014.nyalib.fluid.FluidBuilder;
import net.danygames2014.nyalib.fluid.FluidRegistry;
import net.danygames2014.nyalib.network.NetworkType;
import net.danygames2014.nyalibtest.capability.block.ItemHandlerBlockCapabilityTesterBlock;
import net.danygames2014.nyalibtest.capability.item.YoinkerItem;
import net.danygames2014.nyalibtest.energy.EnergyConsumerBlock;
import net.danygames2014.nyalibtest.energy.EnergySourceBlock;
import net.danygames2014.nyalibtest.energy.MultimeterItem;
import net.danygames2014.nyalibtest.energy.WireBlock;
import net.danygames2014.nyalibtest.energy.entity.EnergyConsumerBlockEntity;
import net.danygames2014.nyalibtest.energy.entity.EnergySourceBlockEntity;
import net.danygames2014.nyalibtest.fluid.FluidTankBlock;
import net.danygames2014.nyalibtest.fluid.InfiniteWaterBlock;
import net.danygames2014.nyalibtest.fluid.ManagedFluidTankBlock;
import net.danygames2014.nyalibtest.fluid.SimpleFluidTankBlock;
import net.danygames2014.nyalibtest.fluid.entity.FluidTankBlockEntity;
import net.danygames2014.nyalibtest.fluid.entity.InfiniteWaterBlockEntity;
import net.danygames2014.nyalibtest.fluid.entity.ManagedFluidTankBlockEntity;
import net.danygames2014.nyalibtest.fluid.entity.SimpleFluidTankBlockEntity;
import net.danygames2014.nyalibtest.fluid.item.FluidCellItem;
import net.danygames2014.nyalibtest.fluid.item.FluidPipetteItem;
import net.danygames2014.nyalibtest.fluid.item.ManagedFluidPipetteItem;
import net.danygames2014.nyalibtest.item.ManagedInventoryBlock;
import net.danygames2014.nyalibtest.item.ManagedInventoryBlockEntity;
import net.danygames2014.nyalibtest.item.ManagedItemBag;
import net.danygames2014.nyalibtest.item.SideHopperBlock;
import net.danygames2014.nyalibtest.item.entity.SideHopperBlockEntity;
import net.danygames2014.nyalibtest.multipart.CoverMultipartComponent;
import net.danygames2014.nyalibtest.multipart.CoverMultipartItem;
import net.danygames2014.nyalibtest.multipart.MultipartItem;
import net.danygames2014.nyalibtest.multipart.TestMultipartComponent;
import net.danygames2014.nyalibtest.network.BasicNetworkType;
import net.danygames2014.nyalibtest.network.CableBlock;
import net.danygames2014.nyalibtest.network.EastWestCableBlock;
import net.danygames2014.nyalibtest.network.NetworkEdgeBlock;
import net.danygames2014.nyalibtest.screen.FluidTankScreen;
import net.danygames2014.nyalibtest.screen.ManagedFluidTankScreen;
import net.danygames2014.nyalibtest.screen.ManagedInventoryScreen;
import net.danygames2014.nyalibtest.screen.SimpleFluidTankScreen;
import net.danygames2014.nyalibtest.simpleenergy.SimpleEnergyReceiverBlock;
import net.danygames2014.nyalibtest.simpleenergy.SimpleInfiniteEnergyBlock;
import net.danygames2014.nyalibtest.simpleenergy.entity.InfiniteSimpleEnergyBlockEntity;
import net.danygames2014.nyalibtest.simpleenergy.entity.SimpleEnergyReceiverBlockEntity;
import net.danygames2014.nyalibtest.sound.ServerSoundBlock;
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
    public static Block simpleFluidTankBlock;
    public static Block managedFluidTankBlock;
    public static Block infiniteWaterBlock;
    public static Item fluidPippeteItem;
    public static Item managedfluidPipetteItem;
    public static Item emptyCellItem;
    public static Item waterCellItem;
    public static Item lavaCellItem;
    public static Item milkCellItem;
    public static Item fuelCellItem;
    public static Item glowstoneCellItem;
    public static Item gravelCellItem;

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
    public static Item managedItemBag;
    public static Block managedInventoryBlock;
    
    public static Item multipartItem;
    public static Item stoneCoverMultipartItem;
    public static Item diamondBlockCoverMultipartItem;

    public static Fluid gravelFluid;
    public static Fluid fuelFluid;
    public static Fluid glowstoneFluid;

    public static NetworkType basicNetworkType;

    @EventListener
    public void registerBlock(BlockRegistryEvent event) {
        // Simple Energy
        simpleInfiniteEnergyBlock = new SimpleInfiniteEnergyBlock(NAMESPACE.id("simple_infinite_energy")).setTranslationKey(NAMESPACE, "simple_infinite_energy");
        simpleEnergyReceiverBlock = new SimpleEnergyReceiverBlock(NAMESPACE.id("simple_energy_receiver")).setTranslationKey(NAMESPACE, "simple_energy_receiver");

        // Item
        sideHopper = new SideHopperBlock(NAMESPACE.id("side_hopper")).setTranslationKey(NAMESPACE, "side_hopper");
        managedInventoryBlock = new ManagedInventoryBlock(NAMESPACE.id("managed_inventory"), Material.METAL).setTranslationKey(NAMESPACE, "managed_inventory");

        // Block Networks
        networkNodeBlock = new CableBlock(NAMESPACE.id("network_node_block")).setTranslationKey(NAMESPACE, "network_node_block");
        eastWestNetworkNodeBlock = new EastWestCableBlock(NAMESPACE.id("east_west_network_node")).setTranslationKey(NAMESPACE, "east_west_network_node");
        networkEdgeBlock = new NetworkEdgeBlock(NAMESPACE.id("network_edge")).setTranslationKey(NAMESPACE, "network_edge");

        // Fluid
        fluidTankBlock = new FluidTankBlock(NAMESPACE.id("fluid_tank")).setTranslationKey(NAMESPACE, "fluid_tank");
        simpleFluidTankBlock = new SimpleFluidTankBlock(NAMESPACE.id("simple_fluid_tank")).setTranslationKey(NAMESPACE, "simple_fluid_tank");
        managedFluidTankBlock = new ManagedFluidTankBlock(NAMESPACE.id("managed_fluid_tank")).setTranslationKey(NAMESPACE, "managed_fluid_tank");
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
        event.register(gravelFluid = new FluidBuilder(NAMESPACE.id("gravel"), Block.GRAVEL, Block.GRAVEL)
                .color(0xFF212121)
                .build()
        );

        event.register(fuelFluid = new FluidBuilder(NAMESPACE.id("fuel"), NAMESPACE.id("block/fuel_still"), NAMESPACE.id("block/fuel_flowing"))
                .overlayTexture(NAMESPACE.id("block/fuel_overlay"))
                .color(0xFFFFE524)
                .movementSpeedMultiplier(1.1D)
                .build()
        );

        event.register(glowstoneFluid = new FluidBuilder(NAMESPACE.id("glowstone"), Namespace.MINECRAFT.id("block/glowstone"), Namespace.MINECRAFT.id("block/glowstone"))
                .color(0xFFCCAA00)
                .colorMultiplier(0x00FF00)
                .lightLevel(15)
                .build()
        );
    }
    
    @EventListener
    public void afterRegisterFluids(AfterFluidRegistryEvent event) {
        for (Fluid fl : FluidRegistry.getRegistry().values()) {
            System.out.println(fl.getIdentifier() + " -> " + fl.getMaterial().mapColor.id);
        }
    }

    @EventListener
    public void registerItems(ItemRegistryEvent event) {
        multimeter = new MultimeterItem(NAMESPACE.id("multimeter")).setTranslationKey(NAMESPACE, "multimeter");
        
        itemYoinker = new YoinkerItem(NAMESPACE.id("item_yoinker")).setTranslationKey(NAMESPACE, "item_yoinker");
        managedItemBag = new ManagedItemBag(NAMESPACE.id("managed_item_bag")).setTranslationKey(NAMESPACE, "managed_item_bag");
        
        fluidPippeteItem = new FluidPipetteItem(NAMESPACE.id("fluid_pipette")).setTranslationKey(NAMESPACE, "fluid_pipette");
        managedfluidPipetteItem = new ManagedFluidPipetteItem(NAMESPACE.id("managed_fluid_pipette")).setTranslationKey(NAMESPACE, "managed_fluid_pipette");
        emptyCellItem = new FluidCellItem(NAMESPACE.id("fluid_cell"), null).setTranslationKey(NAMESPACE, "fluid_cell");
        
        for (var fluid : FluidRegistry.getRegistry().values()) {
            String id = fluid.getIdentifier().namespace + "_" + fluid.getIdentifier().path + "_cell";
            new FluidCellItem(NAMESPACE.id(id), fluid).setTranslationKey(NAMESPACE, id);
        }
        
        multipartItem = new MultipartItem(NAMESPACE.id("multipart_item")).setTranslationKey(NAMESPACE, "multipart_item");
        stoneCoverMultipartItem = new CoverMultipartItem(NAMESPACE.id("stone_cover"), Block.STONE).setTranslationKey(NAMESPACE, "stone_cover");
        diamondBlockCoverMultipartItem = new CoverMultipartItem(NAMESPACE.id("diamond_cover"), Block.DIAMOND_BLOCK).setTranslationKey(NAMESPACE, "diamond_cover");
    }

    @EventListener
    public void registerBlockEntites(BlockEntityRegisterEvent event) {
        event.register(InfiniteSimpleEnergyBlockEntity.class, "simple_infinite_energy");
        event.register(SimpleEnergyReceiverBlockEntity.class, "simple_energy_receiver");
        event.register(SideHopperBlockEntity.class, "side_hopper");
        event.register(FluidTankBlockEntity.class, "fluid_tank");
        event.register(InfiniteWaterBlockEntity.class, "infinite_water_block");
        event.register(EnergySourceBlockEntity.class, "energy_source");
        event.register(EnergyConsumerBlockEntity.class, "energy_consumer");
        event.register(SimpleFluidTankBlockEntity.class, "simple_fluid_tank");
        event.register(ManagedFluidTankBlockEntity.class, "managed_fluid_tank");
        event.register(ManagedInventoryBlockEntity.class, "managed_inventory");
    }

    @EventListener
    public void registerNetworkTypes(NetworkTypeRegistryEvent event) {
        event.register(basicNetworkType = new BasicNetworkType(NAMESPACE.id("basic")));
    }
    
    @EventListener
    public void registerMultipartComponents(MultipartComponentRegistryEvent event) {
        event.register(NAMESPACE.id("test"), TestMultipartComponent.class, TestMultipartComponent::new);
        event.register(NAMESPACE.id("cover"), CoverMultipartComponent.class, CoverMultipartComponent::new);
    }

    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerScreenHandlers(GuiHandlerRegistryEvent event) {
        event.register(NAMESPACE.id("fluid_tank"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openFluidTank, FluidTankBlockEntity::new));
        event.register(NAMESPACE.id("simple_fluid_tank"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openSimpleFluidTank, SimpleFluidTankBlockEntity::new));
        event.register(NAMESPACE.id("managed_fluid_tank"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openManagedFluidTank, ManagedFluidTankBlockEntity::new));
        event.register(NAMESPACE.id("managed_inventory"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openManagedInventory, ManagedFluidTankBlockEntity::new));
    }

    @Environment(EnvType.CLIENT)
    private Screen openManagedInventory(PlayerEntity player, Inventory inventory) {
        return new ManagedInventoryScreen(player, (ManagedInventoryBlockEntity) inventory);
    }

    @Environment(EnvType.CLIENT)
    private Screen openFluidTank(PlayerEntity player, Inventory inventory) {
        return new FluidTankScreen(player, (FluidTankBlockEntity) inventory);
    }

    @Environment(EnvType.CLIENT)
    private Screen openSimpleFluidTank(PlayerEntity player, Inventory inventory) {
        return new SimpleFluidTankScreen(player, (SimpleFluidTankBlockEntity) inventory);
    }

    @Environment(EnvType.CLIENT)
    private Screen openManagedFluidTank(PlayerEntity player, Inventory inventory) {
        return new ManagedFluidTankScreen(player, (ManagedFluidTankBlockEntity) inventory);
    }
}
