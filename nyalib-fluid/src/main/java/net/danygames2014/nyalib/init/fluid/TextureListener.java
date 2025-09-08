package net.danygames2014.nyalib.init.fluid;

import net.danygames2014.nyalib.block.FluidBlockManager;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;

public class TextureListener {
    @EventListener
    public void registerTextures(TextureRegisterEvent event) {
        FluidBlockManager.registerTextures(event);
    }
}
