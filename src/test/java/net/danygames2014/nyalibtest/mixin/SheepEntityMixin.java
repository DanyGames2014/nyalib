package net.danygames2014.nyalibtest.mixin;

import net.danygames2014.nyalib.fluid.FluidStack;
import net.danygames2014.nyalib.fluid.entity.ManagedFluidHandlerEntity;
import net.danygames2014.nyalib.item.entity.ManagedItemHandlerEntity;
import net.danygames2014.nyalibtest.NyaLibTest;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SheepEntity.class)
public abstract class SheepEntityMixin extends Entity implements ManagedFluidHandlerEntity, ManagedItemHandlerEntity {
    public SheepEntityMixin(World world) {
        super(world);
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void addFluidTanks(World par1, CallbackInfo ci) {
        addFluidSlot(2500).setAllowedFluids(NyaLibTest.glowstoneFluid);
        addItemSlot();
    }
    
    @Inject(method = "interact", at = @At("HEAD"))
    public void addGlowstoneFluid(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        this.insertFluid(new FluidStack(NyaLibTest.glowstoneFluid, 50));
        
        if (player.isSneaking()) {
            this.insertItem(new ItemStack(Block.BEDROCK, 1));
        }
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void addBedrock(Entity attacker, int amount, CallbackInfoReturnable<Boolean> cir) {
        if (attacker instanceof PlayerEntity player) {
            this.hearts = 10;
            
            if (player.isSneaking()) {
                this.insertItem(new ItemStack(Block.BEDROCK, 1));
                cir.setReturnValue(true);
            } else {
                ItemStack extractedStack = this.extractItem(1);
                if (extractedStack != null) {
                    ItemEntity itemEntity = new ItemEntity(world, x, y, z, extractedStack);
                    world.spawnEntity(itemEntity);
                }
            }
        }
    }
}
