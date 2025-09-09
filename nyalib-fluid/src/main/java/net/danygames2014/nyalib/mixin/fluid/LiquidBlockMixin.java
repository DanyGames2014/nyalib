package net.danygames2014.nyalib.mixin.fluid;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.danygames2014.nyalib.block.FlowingFluidBlock;
import net.danygames2014.nyalib.fluid.Fluid;
import net.danygames2014.nyalib.fluid.FluidRegistry;
import net.minecraft.block.LiquidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LiquidBlock.class)
public class LiquidBlockMixin {
    @Unique
    private static Fluid fluid;
    
    @WrapOperation(method = "getFlowingAngle", at = @At(value = "FIELD", target = "Lnet/minecraft/block/material/Material;WATER:Lnet/minecraft/block/material/Material;", ordinal = 0))
    private static Material injectFluidMaterial(Operation<Material> original, @Local(argsOnly = true) BlockView view, @Local(ordinal = 0, argsOnly = true) int x, @Local(ordinal = 1, argsOnly = true) int y, @Local(ordinal = 2, argsOnly = true) int z) {
        fluid = FluidRegistry.get(view.getBlockId(x, y, z));

        if (fluid != null) {
            return fluid.getFlowingBlock().material;
        }
        
        return original.call();
    }
    
    @WrapOperation(method = "getFlowingAngle", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/LiquidBlock;getFlow(Lnet/minecraft/world/BlockView;III)Lnet/minecraft/util/math/Vec3d;", ordinal = 0))
    private static Vec3d calculateFlow(LiquidBlock instance, BlockView blockView, int x, int y, int z, Operation<Vec3d> original){
        if (fluid.getFlowingBlock() instanceof FlowingFluidBlock flowingFluidBlock) {
            Vec3d flow = flowingFluidBlock.getFlow(blockView, x,y,z);
            fluid = null;
            return flow;
        }
        
        return original.call(instance, blockView, x, y, z);
    }
}
