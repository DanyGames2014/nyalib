package net.danygames2014.nyalib.mixin.blocknetworks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.danygames2014.nyalib.network.NetworkComponent;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FlattenedChunk.class)
public class FlattenedChunkMixin {
    // This exists because StationAPI still calls the onBlockPlaced method even when setBlockState is used to update the current state's properties
    // That call results in onPlaced getting called on the Network Component, invoking the creation of a new network
    @ModifyExpressionValue(method = "setBlockState", at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isRemote:Z"))
    public boolean aaa(boolean original, @Local(argsOnly = true) BlockState state, @Local(ordinal = 1) BlockState oldState) {
        // If the block is a network component AND the old state is the same as the block
        Block block = state.getBlock();
        if (block instanceof NetworkComponent && oldState.isOf(block)) {
            // Supress the onBlockPlaced method call
            return true;
        }
        return original;
    }
}
