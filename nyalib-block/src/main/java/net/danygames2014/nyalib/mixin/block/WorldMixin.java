package net.danygames2014.nyalib.mixin.block;

import net.danygames2014.nyalib.block.RedstoneLevelProvider;
import net.danygames2014.nyalib.impl.RedstoneLevelWorldImpl;
import net.danygames2014.nyalib.mixininterface.RedstoneLevelWorld;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.world.StationFlatteningWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(World.class)
public abstract class WorldMixin implements RedstoneLevelWorld, StationFlatteningWorld {

    @Unique
    private final RedstoneLevelWorldImpl redstoneLevelWorldImpl = new RedstoneLevelWorldImpl((World) (Object)this);

    @Shadow
    public abstract int getBlockId(int x, int y, int z);

    @Shadow
    public abstract boolean shouldSuffocate(int x, int y, int z);

    @Override
    public int getStrongPowerLevelOnSide(int x, int y, int z, int side) {
        return redstoneLevelWorldImpl.getStrongPowerLevelOnSide(x, y, z, side);
    }

    @Inject(method = "isStrongPoweringSide", at = @At(value = "HEAD"), cancellable = true)
    public void adaptIsStrongPoweringSide(int x, int y, int z, int side, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.getStrongPowerLevelOnSide(x, y, z, side) > 0);
    }

    @Override
    public int getStrongPowerLevel(int x, int y, int z) {
        return redstoneLevelWorldImpl.getStrongPowerLevel(x, y, z);
    }

    @Inject(method = "isStrongPowered", at = @At(value = "HEAD"), cancellable = true)
    public void adaptIsStrongPowered(int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.getStrongPowerLevel(x, y, z) > 0);
    }

    @Override
    public int getPowerLevelOnSide(int x, int y, int z, int side) {
        return redstoneLevelWorldImpl.getPowerLevelOnSide(x, y, z, side);
    }

    @Inject(method = "isPoweringSide", at = @At(value = "HEAD"), cancellable = true)
    public void adaptIsPoweringSide(int x, int y, int z, int side, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.getPowerLevelOnSide(x, y, z, side) > 0);
    }

    @Override
    public int getPowerLevel(int x, int y, int z) {
        return redstoneLevelWorldImpl.getPowerLevel(x, y, z);
    }

    @Inject(method = "isPowered", at = @At(value = "HEAD"), cancellable = true)
    public void adaptIsPowered(int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.getPowerLevel(x, y, z) > 0);
    }
}
