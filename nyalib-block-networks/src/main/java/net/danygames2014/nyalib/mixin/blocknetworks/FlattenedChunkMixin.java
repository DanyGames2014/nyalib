package net.danygames2014.nyalib.mixin.blocknetworks;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.danygames2014.nyalib.network.NetworkComponent;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = FlattenedChunk.class)
public class FlattenedChunkMixin {
    // This exists because StationAPI still calls the onBlockPlaced method even when setBlockState is used to update the current state's properties
    // That call results in onPlaced getting called on the Network Component, invoking the creation of a new network
    @WrapWithCondition(method = "setBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onBlockPlaced(Lnet/minecraft/world/World;IIILnet/modificationstation/stationapi/api/block/BlockState;)V"), require = 0)
    public boolean aaa(Block block, World world, int x, int y, int z, BlockState oldState) {
        // If the block is a network component AND the old state is the same as the block
        //noinspection RedundantIfStatement
        if (block instanceof NetworkComponent && oldState.isOf(block)) {
            // Supress the onBlockPlaced method call
            return false;
        }
        return true;
    }
}
