package net.danygames2014.nyalib.init.energy;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.capability.block.energyhandler.EnergyHandlerBlockCapability;
import net.danygames2014.nyalib.capability.block.energyhandler.EnergyHandlerInterfaceBlockCapabilityProvider;
import net.danygames2014.nyalib.capability.block.energyhandler.EnergyStorageBlockCapability;
import net.danygames2014.nyalib.capability.block.energyhandler.EnergyStorageInterfaceBlockCapabilityProvider;
import net.danygames2014.nyalib.event.BlockCapabilityClassRegisterEvent;
import net.danygames2014.nyalib.event.BlockCapabilityProviderRegisterEvent;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;

public class BlockCapabilityListener {
    @EventListener
    public void registerBlockCapabilityClass(BlockCapabilityClassRegisterEvent event) {
        event.register(NyaLib.NAMESPACE.id("energy_storage"), EnergyStorageBlockCapability.class);
        event.register(NyaLib.NAMESPACE.id("energy_handler"), EnergyHandlerBlockCapability.class);
    }

    @EventListener
    public void registerBlockCapabilityProvider(BlockCapabilityProviderRegisterEvent event) {
        event.register(NyaLib.NAMESPACE.id("energy_storage"), new EnergyStorageInterfaceBlockCapabilityProvider());
        event.register(NyaLib.NAMESPACE.id("energy_handler"), new EnergyHandlerInterfaceBlockCapabilityProvider());
    }

    @EventListener(priority = ListenerPriority.LOWEST)
    public void registerBlockCapabilityProviderLowestPriority(BlockCapabilityProviderRegisterEvent event) {
    }
}
