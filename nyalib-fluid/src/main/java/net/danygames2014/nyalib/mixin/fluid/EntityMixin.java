package net.danygames2014.nyalib.mixin.fluid;

import net.danygames2014.nyalib.entity.FluidEntity;
import net.danygames2014.nyalib.fluid.Fluid;
import net.danygames2014.nyalib.fluid.FluidRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
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
            for (Fluid fluid : FluidRegistry.getRegistry().values()) {
                if (fluid.canSwim((Entity) (Object) this)) {
                    if (this.world.updateMovementInFluid(this.boundingBox.expand((double) 0.0F, (double) -0.4F, (double) 0.0F).contract(0.001, 0.001, 0.001), fluid.getMaterial(), (Entity) (Object) this)) {
                        cir.setReturnValue(true);
                    }
                }
            }
        }
    }
}
