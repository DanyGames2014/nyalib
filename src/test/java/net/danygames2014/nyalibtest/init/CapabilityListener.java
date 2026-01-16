package net.danygames2014.nyalibtest.init;

import net.danygames2014.nyalib.event.BlockCapabilityClassRegisterEvent;
import net.danygames2014.nyalib.event.BlockCapabilityProviderRegisterEvent;
import net.danygames2014.nyalibtest.NyaLibTest;
import net.danygames2014.nyalibtest.capability.meow.DefaultMeowBlockCapabilityProvider;
import net.danygames2014.nyalibtest.capability.meow.MeowBlockCapability;
import net.mine_diver.unsafeevents.listener.EventListener;

public class CapabilityListener {
    @EventListener
    public void registerBlockCapabilityClass(BlockCapabilityClassRegisterEvent event) {
        event.register(NyaLibTest.NAMESPACE.id("meow"), MeowBlockCapability.class);
    }
    
    @EventListener
    public void registerBlockCapabilityProvider(BlockCapabilityProviderRegisterEvent event) {
        event.register(NyaLibTest.NAMESPACE.id("meow"), new DefaultMeowBlockCapabilityProvider());
    }
}
