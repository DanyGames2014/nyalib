package net.danygames2014.nyalib.mixin.fluid;

import net.danygames2014.nyalib.fluid.TankManager;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public class BlockEntityMixin {
    @Unique
    TankManager tankManager;
    
    @SuppressWarnings("MissingUnique")
    public TankManager getTankManager() {
        if (tankManager == null) {
            tankManager = new TankManager();
        }
        
        return tankManager;
    }
    
    @Inject(method = "writeNbt", at = @At(value = "TAIL"))
    public void writeManagedTankNbt(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound managedTankNbt = new NbtCompound();
        getTankManager().writeNbt(managedTankNbt);
        nbt.put("ManagedTankData", managedTankNbt);
    }
    
    @Inject(method = "readNbt", at = @At(value = "TAIL"))
    public void readManagedTankNbt(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound managedTankNbt = nbt.getCompound("ManagedTankData");
        getTankManager().readNbt(managedTankNbt);
    }
}
