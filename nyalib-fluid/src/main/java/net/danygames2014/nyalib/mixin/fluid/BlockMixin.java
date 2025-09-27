package net.danygames2014.nyalib.mixin.fluid;

import net.danygames2014.nyalib.block.FluidBlockManager;
import net.danygames2014.nyalib.event.AfterFluidRegistryEvent;
import net.danygames2014.nyalib.event.FluidRegistryEvent;
import net.minecraft.block.Block;
import net.minecraft.stat.Stat;
import net.modificationstation.stationapi.api.StationAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/item/Item;ITEMS:[Lnet/minecraft/item/Item;", ordinal = 0))
    private static void callFluidRegisterEvent(CallbackInfo ci){
        StationAPI.EVENT_BUS.post(new FluidRegistryEvent());   
        FluidBlockManager.registerBlocks();
        StationAPI.EVENT_BUS.post(new AfterFluidRegistryEvent());
    }
}
