package net.danygames2014.nyalib.mixin.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.danygames2014.nyalib.item.EnhancedPlacementContextItem;
import net.minecraft.client.InteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InteractionManager.class)
public class InteractionManagerMixin {
    @WrapOperation(method = "interactBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;useOnBlock(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;IIII)Z"))
    public boolean useEnhancedPlacementConext(ItemStack stack, PlayerEntity player, World world, int x, int y, int z, int side, Operation<Boolean> original) {
        if (stack.getItem() instanceof EnhancedPlacementContextItem enhancedPlacementContextItem) {
            return enhancedPlacementContextItem.useOnBlock(stack, player, world, x, y, z, side, Minecraft.INSTANCE.crosshairTarget.pos);
        }
        return original.call(stack, player, world, x, y, z, side);
    }
}
