package net.danygames2014.nyalib.mixin.network;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.danygames2014.nyalib.network.NetworkComponent;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("RedundantIfStatement")
@Mixin(FlattenedChunk.class)
public class FlattenedChunkMixin {
    @WrapWithCondition(method = "setBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onBreak(Lnet/minecraft/world/World;III)V"))
    public boolean dontCallOnBreak(Block block, World world, int x, int y, int z, @Local(ordinal = 0, argsOnly = true) BlockState state) {
        if (block instanceof NetworkComponent && state.getBlock() instanceof NetworkComponent) {
            return false;
        }
        return true;
    }
}
