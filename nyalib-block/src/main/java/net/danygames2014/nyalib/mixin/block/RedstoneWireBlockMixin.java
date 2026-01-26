package net.danygames2014.nyalib.mixin.block;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RedstoneWireBlock.class)
public class RedstoneWireBlockMixin {
    @Unique
    int powerLevel; 
    
    // TODO: Fix updating from 0 to 1 on strong redstone not working
    
    @WrapOperation(method = "doUpdatePower", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isPowered(III)Z"))
    public boolean trickTheCheck(World world, int x, int y, int z, Operation<Boolean> original) {
        powerLevel = world.getPowerLevel(x, y, z);
        
        return powerLevel > 0;
    }
    
    @ModifyExpressionValue(method = "doUpdatePower", at = @At(value = "CONSTANT", args = "intValue=15", ordinal = 0))
    public int insertPowerLevel(int original) {
        return powerLevel;
    }
    
    @WrapOperation(method = "doUpdatePower", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockMeta(III)I"))
    public int whyNotch(World instance, int x, int y, int z, Operation<Integer> original) {
        return original.call(instance,x,y,z);
    }
}
