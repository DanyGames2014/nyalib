package net.danygames2014.nyalib.mixin.block;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(RedstoneWireBlock.class)
public abstract class RedstoneWireBlockMixin {
    @Shadow
    private Set neighborsToUpdate;

    @Unique
    int powerLevel; 
    
    @Unique
    boolean updated = false;
    
    @Unique
    int initialLevel = 0;
    
    @WrapOperation(method = "doUpdatePower", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isPowered(III)Z"))
    public boolean trickTheCheck(World world, int x, int y, int z, Operation<Boolean> original) {
        initialLevel = world.getBlockMeta(x,y,z);
        
        powerLevel = world.getPowerLevel(x, y, z);
        updated = false;
        
        return powerLevel > 0;
    }
    
    @ModifyExpressionValue(method = "doUpdatePower", at = @At(value = "CONSTANT", args = "intValue=15", ordinal = 0))
    public int insertPowerLevel(int original) {
        return powerLevel;
    }
  
    @Inject(method = "doUpdatePower", at = @At(value = "INVOKE", target = "Ljava/util/Set;add(Ljava/lang/Object;)Z", ordinal = 0))
    public void checkIfRan2(World world, int x, int y, int z, int sourceX, int sourceY, int sourceZ, CallbackInfo ci) {
        updated = true;   
    }
    
    @SuppressWarnings("unchecked")
    @Inject(method = "doUpdatePower", at = @At("TAIL"))
    public void updateIfNeccessary(World world, int x, int y, int z, int sourceX, int sourceY, int sourceZ, CallbackInfo ci, @Local(ordinal = 6) int var8, @Local(ordinal = 7) int var9) {
        if (!updated && initialLevel != world.getBlockMeta(x,y,z)) {
            this.neighborsToUpdate.add(new BlockPos(x, y, z));
            this.neighborsToUpdate.add(new BlockPos(x - 1, y, z));
            this.neighborsToUpdate.add(new BlockPos(x + 1, y, z));
            this.neighborsToUpdate.add(new BlockPos(x, y - 1, z));
            this.neighborsToUpdate.add(new BlockPos(x, y + 1, z));
            this.neighborsToUpdate.add(new BlockPos(x, y, z - 1));
            this.neighborsToUpdate.add(new BlockPos(x, y, z + 1));
        }
    }
}
