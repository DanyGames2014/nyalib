package net.danygames2014.nyalib.init;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.capability.item.energyhandler.EnergyStorageItemCapability;
import net.danygames2014.nyalib.capability.item.energyhandler.EnergyStorageItemInterfaceItemCapabilityProvider;
import net.danygames2014.nyalib.capability.item.itemhandler.ItemHandlerItemCapability;
import net.danygames2014.nyalib.event.ItemCapabilityClassRegisterEvent;
import net.danygames2014.nyalib.event.ItemCapabilityProviderRegisterEvent;
import net.mine_diver.unsafeevents.listener.EventListener;

public class ItemCapabilityListener {
    @EventListener
    public void registerItemCapabilityClass(ItemCapabilityClassRegisterEvent event) {
        event.register(NyaLib.NAMESPACE.id("item_handler"), ItemHandlerItemCapability.class);
        event.register(NyaLib.NAMESPACE.id("energy_storage"), EnergyStorageItemCapability.class);
    }

    @EventListener
    public void registerItemCapability(ItemCapabilityProviderRegisterEvent event) {
        event.register(NyaLib.NAMESPACE.id("energy_storage"), new EnergyStorageItemInterfaceItemCapabilityProvider());
    }
}
