package net.danygames2014.nyalib.mixin.multipart;

import com.llamalad7.mixinextras.sugar.Local;
import net.danygames2014.nyalib.multipart.MultipartState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "rawtypes"})
@Mixin(World.class)
public class WorldMixin {
    @Shadow
    private ArrayList tempCollisionBoxes;

    @Shadow
    public boolean isRemote;

    @Inject(method = "getEntityCollisions", at = @At(value = "FIELD", target = "Lnet/minecraft/block/Block;BLOCKS:[Lnet/minecraft/block/Block;", opcode = Opcodes.GETSTATIC))
    public void handleMultipartCollision(Entity entity, Box box, CallbackInfoReturnable<List<?>> cir, @Local(ordinal = 6) int var9, @Local(ordinal = 7) int var10, @Local(ordinal = 8) int var11) {
        MultipartState state = entity.world.getMultipartState(var9, var11, var10);
        if (state != null) {
            System.err.println("\n");
            System.err.println(state);
            System.err.println("BEFORE:");
            for (Object leBoxO : tempCollisionBoxes) {
                Box leBox = (Box) leBoxO;
                System.err.println(leBox);
            }
            
            state.getCollisionBoxes((ArrayList<Box>)this.tempCollisionBoxes);
            
            System.err.println("AFTER:");
            for (Object leBoxO : tempCollisionBoxes) {
                Box leBox = (Box) leBoxO;
                System.err.println(leBox);
            }
        }
    }
}
