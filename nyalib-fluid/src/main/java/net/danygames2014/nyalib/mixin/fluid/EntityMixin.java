package net.danygames2014.nyalib.mixin.fluid;

import net.danygames2014.nyalib.entity.FluidEntity;
import net.danygames2014.nyalib.fluid.Fluid;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(Entity.class)
public abstract class EntityMixin implements FluidEntity {
    @Shadow
    public abstract boolean isInFluid(Material material);

    @Override
    public boolean isInFluid(Fluid fluid) {
        return this.isInFluid(fluid.getMaterial());
    }
}
