package net.danygames2014.nyalib.mixin.multipart;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.danygames2014.nyalib.multipart.MultipartState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
    
    @Unique
    private final ObjectArrayList<Box> tempMultipartCollisionBoxes = new ObjectArrayList<>();

    @Inject(method = "getEntityCollisions", at = @At(value = "HEAD"))
    public void clearMultipartCollisionBoxes(Entity entity, Box entityBox, CallbackInfoReturnable<List> cir) {
        tempMultipartCollisionBoxes.clear();
    }
    
    @Inject(method = "getEntityCollisions", at = @At(value = "FIELD", target = "Lnet/minecraft/block/Block;BLOCKS:[Lnet/minecraft/block/Block;", opcode = Opcodes.GETSTATIC))
    public void handleMultipartCollision(Entity entity, Box entityBox, CallbackInfoReturnable<List<?>> cir, @Local(ordinal = 6) int var9, @Local(ordinal = 7) int var10, @Local(ordinal = 8) int var11) {
        MultipartState state = entity.world.getMultipartState(var9, var11, var10);
        
        if (state != null) {
            state.getCollisionBoxes(tempMultipartCollisionBoxes);
            
            for (Box box : tempMultipartCollisionBoxes) {
                if (entityBox.intersects(box)) {
                    tempCollisionBoxes.add(box);
                }
            }
        }
    }
}
