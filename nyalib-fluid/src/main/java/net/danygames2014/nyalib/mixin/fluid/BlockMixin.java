package net.danygames2014.nyalib.mixin.fluid;

import net.danygames2014.nyalib.event.FluidRegistryEvent;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.StationAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/stat/Stats;initializeItemStats()V"))
    private static void callFluidRegisterEvent(CallbackInfo ci){
        StationAPI.EVENT_BUS.post(new FluidRegistryEvent());    
    }
}
