package net.danygames2014.nyalib.mixin.fluid;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.danygames2014.nyalib.block.FlowingFluidBlock;
import net.minecraft.block.FlowingLiquidBlock;
import net.minecraft.block.LiquidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FlowingLiquidBlock.class)
public class FlowingLiquidBlockMixin extends LiquidBlock {
    public FlowingLiquidBlockMixin(int i, Material material) {
        super(i, material);
    }

    @WrapOperation(method = "convertToSource", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockWithoutNotifyingNeighbors(IIIII)Z"))
    public boolean injectSourceBlockId(World world, int x, int y, int z, int blockId, int meta, Operation<Boolean> original) {
        if (world.getBlockState(x, y, z).getBlock() instanceof FlowingFluidBlock flowingFluidBlock) {
            return original.call(world, x, y, z, flowingFluidBlock.fluid.getStillBlock().id, meta);
        }

        return original.call(world, x, y, z, blockId, meta);
    }
}
