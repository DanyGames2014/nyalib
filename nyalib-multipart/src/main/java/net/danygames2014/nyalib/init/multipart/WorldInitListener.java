package net.danygames2014.nyalib.init.multipart;

import net.danygames2014.nyalib.multipart.MultipartDynamicRenderDispatcher;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.world.WorldEvent;

public class WorldInitListener {
    @EventListener()
    public void onWorldInit(WorldEvent.Init event) {
        MultipartDynamicRenderDispatcher.INSTANCE.renderers.clear();
    }
}
