package net.danygames2014.nyalib.mixin.network;

import net.danygames2014.nyalib.network.Network;
import net.danygames2014.nyalib.network.NetworkManager;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionData;
import net.modificationstation.stationapi.api.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;

@Mixin(World.class)
public class WorldMixin {
    @Shadow @Final public Dimension dimension;

    @Inject(method = "tickEntities", at = @At(value = "TAIL"))
    public void tickNetworks(CallbackInfo ci) {
        HashMap<Identifier, ArrayList<Network>> networks = NetworkManager.getNetworks(this.dimension);
        if(networks != null){
            for (ArrayList<Network> networkTypes : networks.values()) {
                for (Network network : networkTypes) {
                    network.tick();
                }
            }
        }
    }
}
