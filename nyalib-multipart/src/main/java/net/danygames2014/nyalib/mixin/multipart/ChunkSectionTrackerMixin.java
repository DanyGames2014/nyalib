package net.danygames2014.nyalib.mixin.multipart;

import net.modificationstation.stationapi.impl.server.network.ChunkSectionTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkSectionTracker.class)
public class ChunkSectionTrackerMixin {
    @Inject(method = "sendQueue", at = @At(value = "TAIL"))
    public void onBlockChange(CallbackInfo ci) {
        // TODO: send the multipart update packets here
    }
}
