package net.danygames2014.nyalib.mixin.fluid;

import net.danygames2014.nyalib.fluid.Fluid;
import net.danygames2014.nyalib.fluid.FluidRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    @Shadow
    private int blockId;

    @Inject(method = "useOnBlock", at = @At(value = "HEAD"), cancellable = true)
    public void preventPlacing(ItemStack stack, PlayerEntity player, World world, int x, int y, int z, int side, CallbackInfoReturnable<Boolean> cir) {
        Fluid fluid = FluidRegistry.get(blockId);
        
        if (fluid != null && !fluid.isPlaceableInWorld()) {
            cir.setReturnValue(false);
        }
    }
}
