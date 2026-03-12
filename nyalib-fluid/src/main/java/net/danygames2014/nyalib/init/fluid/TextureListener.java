package net.danygames2014.nyalib.init.fluid;

import net.danygames2014.nyalib.block.FluidBlockManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;

@Environment(EnvType.CLIENT)
public class TextureListener {
    @EventListener
    public void registerTextures(TextureRegisterEvent event) {
        FluidBlockManager.registerTextures(event);
    }
}
