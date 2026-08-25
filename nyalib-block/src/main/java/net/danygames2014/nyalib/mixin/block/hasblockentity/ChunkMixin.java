package net.danygames2014.nyalib.mixin.block.hasblockentity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.danygames2014.nyalib.block.HasBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(Chunk.class)
public abstract class ChunkMixin {
    @Shadow
    public World world;

    @Shadow
    @Final
    public int x;

    @Shadow
    @Final
    public int z;

    @Shadow
    public Map blockEntities;

    @Shadow
    public abstract int getBlockId(int x, int y, int z);

    @WrapOperation(method = "getBlockEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;getBlockId(III)I"))
    public int foolGetBlockEntity(Chunk chunk, int x, int y, int z, Operation<Integer> original, @Local BlockPos var4, @Local LocalRef<BlockEntity> var5) {
        Block block = Block.BLOCKS[chunk.getBlockId(x, y, z)];
        
        if (block instanceof HasBlockEntity) {
            block.onPlaced(this.world, this.x * 16 + x, y, this.z * 16 + z);
            var5.set((BlockEntity) this.blockEntities.get(var4));
            return 0;
        }
        
        return original.call(chunk, x, y, z);
    }
    
    @WrapOperation(method = "setBlockEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;getBlockId(III)I", ordinal = 1))
    public int foolSetBlockEntity(Chunk chunk, int localX, int y, int localZ, Operation<Integer> original) {
        Block block = Block.BLOCKS[this.getBlockId(localX, y, localZ)];
        
        if (block instanceof HasBlockEntity) {
            return Block.FURNACE.id;
        }
        
        return original.call(chunk, localX, y, localZ);
    }
}
