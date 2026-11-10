package net.danygames2014.nyalib.entity.mixin.datatracker;

import net.danygames2014.nyalib.entity.datatracker.ExtendedDataTracker;
import net.danygames2014.nyalib.entity.datatracker.mixininterface.NyaLibExtendedDataTrackerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin implements NyaLibExtendedDataTrackerEntity {
    @Unique
    private ExtendedDataTracker nyalib$extendedDataTracker;
    
    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/data/DataTracker;<init>()V"))
    public void nyalib_createExtendedDataTracker(World world, CallbackInfo ci) {
        nyalib$extendedDataTracker = new ExtendedDataTracker();
    }
    
    @Inject(method = "<init>", at = @At("TAIL"))
    public void nyalib_initExtendedDataTracker(CallbackInfo ci) {
        this.initExtendedDataTracker();
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public ExtendedDataTracker getExtendedDataTracker() {
        return nyalib$extendedDataTracker;
    }
}
