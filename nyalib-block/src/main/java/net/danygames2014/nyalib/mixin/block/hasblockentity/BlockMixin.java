package net.danygames2014.nyalib.mixin.block.hasblockentity;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.block.HasBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "<init>(ILnet/minecraft/block/material/Material;)V", at = @At(value = "TAIL"))
    public void markAsBlockEntity(int id, Material material, CallbackInfo ci) {
        if (this instanceof HasBlockEntity) {
            //noinspection ConstantValue not true, this is not always true
            if ((Object) this instanceof BlockWithEntity) {
                NyaLib.LOGGER.error("HasBlockEntity is implemented on {} which extends BlockWithEntity, this is not allowed!", this.getClass().getName());
                throw new IllegalStateException("HasBlockEntity is implemented on BlockWithEntity");
            }
            
            Block.BLOCKS_WITH_ENTITY[id] = true;
        }
    }
    
    @Inject(method = "onPlaced(Lnet/minecraft/world/World;III)V", at = @At(value = "HEAD"))
    public void setBlockEntityOnPlaced(World world, int x, int y, int z, CallbackInfo ci) {
        if (this instanceof HasBlockEntity hasBlockEntity) {
            world.setBlockEntity(x, y, z, hasBlockEntity.createBlockEntity(world, x, y, z));
        }
    }
    
    @Inject(method = "onBreak", at = @At(value = "TAIL"))
    public void removeBlockEntityOnBreak(World world, int x, int y, int z, CallbackInfo ci) {
        if (this instanceof HasBlockEntity) {
            world.removeBlockEntity(x, y, z);
        }
    }
}
