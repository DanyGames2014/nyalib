package net.danygames2014.nyalib.mixin.multipart;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.PistonBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PistonBlock.class)
public class PistonBlockMixin {
    @WrapOperation(method = "canExtend", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockId(III)I"))
    private static int checkMultipart(World instance, int x, int y, int z, Operation<Integer> original){
        if(instance.getMultipartState(x, y ,z) != null) {
            return Block.BEDROCK.id;
        }
        return original.call(instance, x, y, z);
    }
}
