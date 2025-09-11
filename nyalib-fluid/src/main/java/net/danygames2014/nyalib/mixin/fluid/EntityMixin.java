package net.danygames2014.nyalib.mixin.fluid;

import net.danygames2014.nyalib.block.FlowingFluidBlock;
import net.danygames2014.nyalib.block.StillFluidBlock;
import net.danygames2014.nyalib.entity.FluidEntity;
import net.danygames2014.nyalib.fluid.Fluid;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(Entity.class)
public abstract class EntityMixin implements FluidEntity {
    @Shadow
    public abstract boolean isInFluid(Material material);

    @Shadow
    public World world;

    @Shadow
    @Final
    public Box boundingBox;

    @Override
    public boolean isInFluid(Fluid fluid) {
        return this.isInFluid(fluid.getMaterial());
    }

    @Inject(method = "checkWaterCollisions", at = @At(value = "RETURN"), cancellable = true)
    public void swimming(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            Box entityBoundingBox = this.boundingBox.expand(0.0D, -0.4D, 0.0D).contract(0.001, 0.001, 0.001);
            int x1 = MathHelper.floor(entityBoundingBox.minX);
            int x2 = MathHelper.floor(entityBoundingBox.maxX + (double) 1.0F);
            int y1 = MathHelper.floor(entityBoundingBox.minY);
            int y2 = MathHelper.floor(entityBoundingBox.maxY + (double) 1.0F);
            int z1 = MathHelper.floor(entityBoundingBox.minZ);
            int z2 = MathHelper.floor(entityBoundingBox.maxZ + (double) 1.0F);

            if (!world.isRegionLoaded(x1, y1, z1, x2, y2, z2)) {
                return;
            }

            Entity thisEntity = (Entity) (Object) this;
            for (int blockX = x1; blockX < x2; ++blockX) {
                for (int blockY = y1; blockY < y2; ++blockY) {
                    for (int blockZ = z1; blockZ < z2; ++blockZ) {
                        Block block = Block.BLOCKS[world.getBlockId(blockX, blockY, blockZ)];

                        if (block instanceof StillFluidBlock stillFluidBlock) {
                            if (stillFluidBlock.fluid.canSwim(thisEntity)) {
                                if (this.world.updateMovementInFluid(this.boundingBox.expand(0.0D, -0.4D, 0.0D).contract(0.001, 0.001, 0.001), stillFluidBlock.fluid.getMaterial(), thisEntity)) {
                                    cir.setReturnValue(true);
                                    return;
                                }
                            }
                        }

                        if (block instanceof FlowingFluidBlock flowingFluidBlock) {
                            if (flowingFluidBlock.fluid.canSwim(thisEntity)) {
                                if (this.world.updateMovementInFluid(this.boundingBox.expand(0.0D, -0.4D, 0.0D).contract(0.001, 0.001, 0.001), flowingFluidBlock.fluid.getMaterial(), thisEntity)) {
                                    cir.setReturnValue(true);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
