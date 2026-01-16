package net.danygames2014.nyalibtest.mixin;

import net.danygames2014.nyalib.fluid.FluidStack;
import net.danygames2014.nyalib.fluid.entity.ManagedFluidHandlerEntity;
import net.danygames2014.nyalibtest.NyaLibTest;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SheepEntity.class)
public class SheepEntityMixin implements ManagedFluidHandlerEntity {
    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void addFluidTanks(World par1, CallbackInfo ci) {
        this.getTankManager().addSlot(2500).setAllowedFluids(NyaLibTest.glowstoneFluid);
    }
    
    @Inject(method = "interact", at = @At("HEAD"))
    public void addGlowstoneFluid(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        this.insertFluid(new FluidStack(NyaLibTest.glowstoneFluid, 50));
    }
}
