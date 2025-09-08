package net.danygames2014.nyalib.mixin.fluid;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.danygames2014.nyalib.block.StillFluidBlock;
import net.minecraft.block.StillLiquidBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StillLiquidBlock.class)
public class StillLiquidBlockMixin {
    @WrapOperation(method = "convertToFlowing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockWithoutNotifyingNeighbors(IIIII)Z"))
    public boolean injectCorrectFLowingBlockId(World world, int x, int y, int z, int blockId, int meta, Operation<Boolean> original){
        if (world.getBlockState(x, y, z).getBlock() instanceof StillFluidBlock stillFluidBlock) {
            return original.call(world, x, y, z, stillFluidBlock.fluid.getFlowingBlock().id, meta);
        }

        return original.call(world, x, y, z, blockId, meta);
    }
}
