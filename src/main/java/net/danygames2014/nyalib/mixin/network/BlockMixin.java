package net.danygames2014.nyalib.mixin.network;

import net.danygames2014.nyalib.network.NetworkComponent;
import net.danygames2014.nyalib.network.NetworkManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {
    @SuppressWarnings({"unchecked", "ConstantValue"})
    @Inject(method = "onPlaced(Lnet/minecraft/world/World;III)V", at = @At(value = "TAIL"))
    public <T extends Block & NetworkComponent> void addToNetOnPlaced(World world, int x, int y, int z, CallbackInfo ci) {
        if (!((Object) this instanceof BlockWithEntity)) {
            if ((Object) this instanceof Block && (Object) this instanceof NetworkComponent) {
                NetworkManager.addBlock(world, x, y, z, (T) (Object) this);
                System.out.println("onPlaced BlockMixin");
            }
        }
    }

    @SuppressWarnings({"ConstantValue", "unchecked"})
    @Inject(method = "onBreak", at = @At(value = "HEAD"))
    public <T extends Block & NetworkComponent> void removeFromNetOnPlaced(World world, int x, int y, int z, CallbackInfo ci) {
        if (!((Object) this instanceof BlockWithEntity)) {
            if ((Object) this instanceof Block && (Object) this instanceof NetworkComponent) {
                NetworkManager.removeBlock(world, x, y, z, (T) (Object) this);
                System.out.println("onBreak BlockMixin");
            }
        }
    }
}
