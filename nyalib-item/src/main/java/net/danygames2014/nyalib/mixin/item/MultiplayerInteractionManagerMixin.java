package net.danygames2014.nyalib.mixin.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.danygames2014.nyalib.item.EnhancedPlacementContextItem;
import net.danygames2014.nyalib.packet.EnhancedPlayerInteractBlockC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MultiplayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MultiplayerInteractionManager.class)
public class MultiplayerInteractionManagerMixin {
    @WrapOperation(method = "interactBlock", at = @At(value = "NEW", target = "(IIIILnet/minecraft/item/ItemStack;)Lnet/minecraft/network/packet/c2s/play/PlayerInteractBlockC2SPacket;"))
    public PlayerInteractBlockC2SPacket useEnhancedPlacementConext(int x, int y, int z, int side, ItemStack stack, Operation<PlayerInteractBlockC2SPacket> original) {
        if (stack.getItem() instanceof EnhancedPlacementContextItem) {
            return new EnhancedPlayerInteractBlockC2SPacket(x, y, z, side, stack, Minecraft.INSTANCE.crosshairTarget.pos);
        }
        
        return original.call(x, y, z, side, stack);
    }
}
