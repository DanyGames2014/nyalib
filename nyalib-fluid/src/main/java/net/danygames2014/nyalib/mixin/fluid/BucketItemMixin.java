package net.danygames2014.nyalib.mixin.fluid;

import com.llamalad7.mixinextras.sugar.Local;
import net.danygames2014.nyalib.fluid.Fluid;
import net.danygames2014.nyalib.fluid.FluidBucket;
import net.danygames2014.nyalib.fluid.FluidRegistry;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.danygames2014.nyalib.sound.SoundHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.States;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(BucketItem.class)
public class BucketItemMixin implements FluidBucket {
    @Shadow
    private int fluidBlockId;

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getMaterial(III)Lnet/minecraft/block/material/Material;", ordinal = 0, shift = At.Shift.BEFORE), cancellable = true)
    public void pain(ItemStack stack, World world, PlayerEntity player, CallbackInfoReturnable<ItemStack> cir, @Local HitResult hitResult) {
        int x = hitResult.blockX;
        int y = hitResult.blockY;
        int z = hitResult.blockZ;

        Fluid fluid = FluidRegistry.get(world.getBlockId(x, y, z));
        if (fluid != null && fluid.getBucketItem() != null) {
            world.setBlockStateWithNotify(x, y, z, States.AIR.get());
            SoundHelper.playSound(player, fluid.getFillSound(), 1.0F, 1.0F);
            cir.setReturnValue(new ItemStack(fluid.getBucketItem()));
        }
    }

    @Inject(method = "use", at = @At(value = "FIELD", target = "Lnet/minecraft/item/BucketItem;fluidBlockId:I", ordinal = 2, opcode = Opcodes.GETFIELD), cancellable = true)
    public void preventPlacingInWorld(ItemStack stack, World world, PlayerEntity player, CallbackInfoReturnable<ItemStack> cir) {
        Fluid fluid = FluidRegistry.get(fluidBlockId);
        if (fluid != null && !fluid.isPlaceableInWorld()) {
            cir.setReturnValue(stack);
        }
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlock(IIIII)Z", ordinal = 0), cancellable = true)
    public void playEmptySound(ItemStack stack, World world, PlayerEntity player, CallbackInfoReturnable<ItemStack> cir) {
        Fluid fluid = getFluid();
        
        if (!fluid.isPlaceableInWorld()) {
            cir.setReturnValue(stack);
        }

        SoundHelper.playSound(world, player.x, player.y, player.z, fluid.getEmptySound(), 0.5F, 1.0F, 16);
    }

    @Override
    public Fluid getFluid() {
        return FluidRegistry.get(fluidBlockId);
    }
}
