package net.danygames2014.nyalibtest.mixin;

import net.danygames2014.nyalib.entity.datatracker.datatype.ExtendedDataTrackerDataTypes;
import net.danygames2014.nyalib.entity.datatracker.mixininterface.NyaLibExtendedDataTrackerEntity;
import net.danygames2014.nyalibtest.NyaLibTest;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PigEntity.class)
public class PigEntityMixin extends AnimalEntity implements NyaLibExtendedDataTrackerEntity {
    public PigEntityMixin(World world) {
        super(world);
    }

    @Override
    public void initExtendedDataTracker() {
        this.getExtendedDataTracker().startTracking(NyaLibTest.NAMESPACE.id("test_entry"), ExtendedDataTrackerDataTypes.BYTE, (byte) 0);
    }
    
    @Inject(method = "interact", at = @At(value = "HEAD"))
    public void testChangingTracker(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (player.isSneaking()) {
            System.err.println(this.id + " => " + this.getExtendedDataTracker().getByte(NyaLibTest.NAMESPACE.id("test_entry")));
            return;
        }
        
        if (world.isRemote) {
            return;
        }

        ItemStack hand = player.getHand();
        int value = hand != null ? hand.count : 0;
        this.getExtendedDataTracker().setByte(NyaLibTest.NAMESPACE.id("test_entry"), (byte) value);
        System.err.println("Set tracker to " + value);
    }
}
