package net.danygames2014.nyalib.mixin.multipart;

import net.danygames2014.nyalib.impl.RedstoneLevelWorldImpl;
import net.danygames2014.nyalib.multipart.MultipartState;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RedstoneLevelWorldImpl.class)
public class RedstoneLevelWorldImplMixin {

    @Shadow
    World world;

    @Inject(method = "Lnet/danygames2014/nyalib/impl/RedstoneLevelWorldImpl;getStrongPowerLevelOnSide(IIII)I", at = @At("RETURN"), cancellable = true, remap = false)
    private void getMultipartStateStrongPowerLevelOnSide(int x, int y, int z, int side, CallbackInfoReturnable<Integer> cir) {
        if(cir.getReturnValueI() == 0) {
            MultipartState state = world.getMultipartState(x, y, z);
            if(state != null) {
                cir.setReturnValue(state.getStrongPowerLevel(Direction.byId(side)));
            }
        }
    }

    @Inject(method = "Lnet/danygames2014/nyalib/impl/RedstoneLevelWorldImpl;getPowerLevelOnSide(IIII)I", at = @At("RETURN"), cancellable = true, remap = false)
    private void getMultipartStatePowerLevelOnSide(int x, int y, int z, int side, CallbackInfoReturnable<Integer> cir) {
        if(cir.getReturnValueI() == 0) {
            MultipartState state = world.getMultipartState(x, y, z);
            if(state != null) {
                cir.setReturnValue(state.getPowerLevel(Direction.byId(side)));
            }
        }
    }
}
