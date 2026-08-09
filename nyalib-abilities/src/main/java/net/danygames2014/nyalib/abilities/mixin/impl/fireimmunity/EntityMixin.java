package net.danygames2014.nyalib.abilities.mixin.impl.fireimmunity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {
    @Shadow
    public int fireTicks;
    
    @WrapOperation(method = "baseTick", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;fireImmune:Z", opcode = Opcodes.GETFIELD))
    public boolean baseTickFireImmune(Entity instance, Operation<Boolean> original) {
        return original.call(instance);
    }

    @WrapOperation(method = "setOnFire", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;fireImmune:Z", opcode = Opcodes.GETFIELD))
    public boolean setOnFireFireImmune(Entity instance, Operation<Boolean> original) {
        return original.call(instance);
    }

    @WrapOperation(method = "damage(I)V", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;fireImmune:Z", opcode = Opcodes.GETFIELD))
    public boolean burnFireImmune(Entity instance, Operation<Boolean> original) {
        return original.call(instance);
    }

    @WrapOperation(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isFireOrLavaInBox(Lnet/minecraft/util/math/Box;)Z"))
    public boolean moveIsInFireOrLava(World instance, Box box, Operation<Boolean> original) {
        return original.call(instance, box);
    }
}
