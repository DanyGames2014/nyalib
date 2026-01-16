package net.danygames2014.nyalib.mixin.fluid;

import net.danygames2014.nyalib.fluid.TankManager;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class FluidHandlerEntityMixin {
    @Unique
    private TankManager tankManager;

    @SuppressWarnings("MissingUnique")
    public TankManager getTankManager() {
        if (tankManager == null) {
            tankManager = new TankManager(true);
        }

        return tankManager;
    }

    @Inject(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtCompound;putBoolean(Ljava/lang/String;Z)V", shift = At.Shift.AFTER))
    public void writeManagedTankNbt(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound managedTankNbt = new NbtCompound();
        getTankManager().writeNbt(managedTankNbt);
        nbt.put("ManagedTankData", managedTankNbt);
    }

    @Inject(method = "read", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setRotation(FF)V", shift = At.Shift.AFTER))
    public void readManagedTankNbt(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound managedTankNbt = nbt.getCompound("ManagedTankData");
        getTankManager().readNbt(managedTankNbt);
    }
}
