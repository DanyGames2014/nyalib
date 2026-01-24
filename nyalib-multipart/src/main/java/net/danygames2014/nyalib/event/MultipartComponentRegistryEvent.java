package net.danygames2014.nyalib.event;

import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.danygames2014.nyalib.multipart.MultipartComponentFactory;
import net.danygames2014.nyalib.multipart.MultipartComponentRegistry;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.util.Identifier;

public class MultipartComponentRegistryEvent extends Event {
    public MultipartComponentRegistry registry;
    
    public MultipartComponentRegistryEvent() {
        registry = MultipartComponentRegistry.getInstance();
    }

    public void register(Identifier identifier, Class<? extends MultipartComponent> componentClass, MultipartComponentFactory componentFactory) {
        MultipartComponentRegistry.register(identifier, componentClass, componentFactory);
    }
}
