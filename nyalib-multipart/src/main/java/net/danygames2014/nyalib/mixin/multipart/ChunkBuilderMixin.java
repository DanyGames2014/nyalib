package net.danygames2014.nyalib.mixin.multipart;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.danygames2014.nyalib.multipart.MultipartState;
import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkBuilder.class)
public class ChunkBuilderMixin {
    @Shadow
    public World world;
    @Unique
    boolean cancelRender = false;
    
    @Unique
    int renderX = 0;
    @Unique
    int renderY = 0;
    @Unique
    int renderZ = 0;
    @Unique
    MultipartState state;
    
    @WrapOperation(method = "rebuild", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/BlockView;getBlockId(III)I"))
    public int trickTheRebuild(BlockView view, int x, int y, int z, Operation<Integer> original) {
        int result = original.call(view, x, y, z);
        cancelRender = false;
        renderX = x;
        renderY = y;
        renderZ = z;
        state = world.getMultipartState(x,y,z);
        
        if (state != null && result == 0) {
            result = 1;
            cancelRender = true;
        }
        
        return result;
    }
    
    @ModifyExpressionValue(method = "rebuild", at = @At(value = "CONSTANT", args = "intValue=0"))
    public int dontRenderBlockEntity(int original) {
        return cancelRender ? 99 : original;
    }
    
    @WrapOperation(method = "rebuild", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getRenderLayer()I"))
    public int dontRenderBlock(Block instance, Operation<Integer> original) {
        return cancelRender ? 99 : original.call(instance);
    }
    
    @Inject(method = "rebuild", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getRenderLayer()I"))
    public void renderMultipart(CallbackInfo ci, @Local BlockRenderManager blockRenderManager, @Local(ordinal = 7) int var11, @Local(ordinal = 9) LocalIntRef var13) {
        if (state != null) {
            var13.set(var13.get() | (state.render(Tessellator.INSTANCE, blockRenderManager, var11) ? 1 : 0));
        }
    }
}
