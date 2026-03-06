package net.danygames2014.nyalib.mixin.fluid;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.danygames2014.nyalib.block.FlowingFluidBlock;
import net.danygames2014.nyalib.fluid.Fluid;
import net.danygames2014.nyalib.fluid.FluidRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingLiquidBlock;
import net.minecraft.block.LiquidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlowingLiquidBlock.class)
public abstract class FlowingLiquidBlockMixin extends LiquidBlock {
    @Shadow
    protected abstract boolean isLiquidBreaking(World world, int x, int y, int z);

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
    
    @Inject(method = "canSpreadTo", at = @At("HEAD"), cancellable = true)
    public void handleFluidInteractions(World world, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        Fluid fluid = FluidRegistry.get(this.id);
        Fluid otherFluid = FluidRegistry.get(world.getBlockId(x, y, z));
        Block otherBlock = world.getBlockState(x, y, z).getBlock();

        // Neither of the fluids are NyaLib managed fluids
        if (fluid == null && otherFluid == null) {
            return; // Vanilla Logic
        }
        
        // If we cant fetch the fluid for this block, this is probably a modded fluid not managed by NyaLib
        if (fluid == null) {
            // A modded fluid is trying to spread into NyaLib fluid
            // If the NyaLib fluid has less priority than default, we allow it
            cir.setReturnValue(otherFluid.getSpreadPriority(null, this) < 1000);
            return;
        }

        // If the other fluid is null, it is either not a fluid or a fluid from another mod
        if (otherFluid == null) {
            return; // Vanilla Logic
        }

        // At this point we know that these are 2 NyaLib managed fluids interacting
        // Disallow spreading into itself
        if (fluid == otherFluid) {
            cir.setReturnValue(false);
            return;
        }

        // If this fluid has higher spread priority, allow its spread over the other fluid
        cir.setReturnValue(fluid.getSpreadPriority(otherFluid, otherBlock) > otherFluid.getSpreadPriority(fluid, this));
    }
}
