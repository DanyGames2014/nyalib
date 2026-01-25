package net.danygames2014.nyalib.mixininterface;

import net.minecraft.entity.player.PlayerEntity;
import net.modificationstation.stationapi.api.util.Util;

public interface MultipartWorldRenderer {
    default void renderMultipartOutline(PlayerEntity player, float tickDelta){
        Util.assertImpl();
    }
}
