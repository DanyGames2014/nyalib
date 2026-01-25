package net.danygames2014.nyalib.mixin.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.danygames2014.nyalib.item.EnhancedPlacementContextItem;
import net.danygames2014.nyalib.packet.EnhancedPlayerInteractBlockC2SPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin extends NetworkHandler {
    @WrapOperation(method = "onPlayerInteractBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerInteractionManager;interactBlock(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)Z"))
    public boolean useEnhancedInteractionContext(ServerPlayerInteractionManager instance, PlayerEntity player, World world, ItemStack stack, int x, int y, int z, int side, Operation<Boolean> original, @Local(ordinal = 0, argsOnly = true) PlayerInteractBlockC2SPacket packet) {
        if (packet instanceof EnhancedPlayerInteractBlockC2SPacket enhancedPacket) {
            if (stack.getItem() instanceof EnhancedPlacementContextItem enhancedPlacementContextItem) {
                return enhancedPlacementContextItem.useOnBlock(stack, player, world, x, y, z, side, enhancedPacket.hitVec);
            }
        }
        
        return original.call(instance, player, world, stack, x, y, z, side);
    }
}
