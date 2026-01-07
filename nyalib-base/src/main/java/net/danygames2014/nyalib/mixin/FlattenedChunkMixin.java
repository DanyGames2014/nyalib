package net.danygames2014.nyalib.mixin;

import net.danygames2014.nyalib.block.BlockEntityInit;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FlattenedChunk.class)
public abstract class FlattenedChunkMixin extends Chunk {
    @Shadow
    public abstract BlockState getBlockState(int x, int y, int z);

    public FlattenedChunkMixin(World world, int x, int z) {
        super(world, x, z);
    }

    @Inject(method = "setBlockEntity", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", shift = At.Shift.AFTER))
    public void initBlockEntity(int relX, int relY, int relZ, BlockEntity blockEntity, CallbackInfo ci) {
        if (blockEntity instanceof BlockEntityInit blockEntityInit) {
            blockEntityInit.init(this.getBlockState(relX, relY, relZ));
        }
    }
}
