package net.danygames2014.nyalib.init;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.capability.block.BlockCapabilityProvider;
import net.danygames2014.nyalib.capability.block.itemhandler.ItemHandlerBlockCapability;
import net.danygames2014.nyalib.capability.block.itemhandler.ItemHandlerInterfaceBlockCapability;
import net.danygames2014.nyalib.capability.block.itemhandler.ItemHandlerInventoryBlockCapability;
import net.danygames2014.nyalib.event.BlockCapabilityClassRegisterEvent;
import net.danygames2014.nyalib.event.BlockCapabilityProviderRegisterEvent;
import net.danygames2014.nyalib.item.ItemHandler;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.inventory.Inventory;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BlockCapabilityListener {
    
    @EventListener
    public void registerBlockCapabilityClass(BlockCapabilityClassRegisterEvent event) {
        event.register(NyaLib.NAMESPACE.id("item_handler"), ItemHandlerBlockCapability.class);
    }
    
    @EventListener(priority = ListenerPriority.HIGHEST)
    public void registerInventoryBlockCapability(BlockCapabilityProviderRegisterEvent event) {
        event.register(NyaLib.NAMESPACE.id("item_handler"), new BlockCapabilityProvider<ItemHandlerBlockCapability>() {
            @Override
            public @Nullable ItemHandlerBlockCapability getCapability(World world, int x, int y, int z) {
                if (world.getBlockEntity(x,y,z) instanceof Inventory inventory) {
                    return new ItemHandlerInventoryBlockCapability(inventory);
                }
                return null;
            }
        });
        
    }

    @EventListener(priority = ListenerPriority.LOWEST)
    public void registerInterfaceBlockCapability(BlockCapabilityProviderRegisterEvent event) {
        event.register(NyaLib.NAMESPACE.id("item_handler"), new BlockCapabilityProvider<ItemHandlerBlockCapability>() {
            @Override
            public @Nullable ItemHandlerBlockCapability getCapability(World world, int x, int y, int z) {
                if (world.getBlockEntity(x, y, z) instanceof ItemHandler itemHandler) {
                    return new ItemHandlerInterfaceBlockCapability(itemHandler);
                }
                return null;
            }
        });
    }
}
