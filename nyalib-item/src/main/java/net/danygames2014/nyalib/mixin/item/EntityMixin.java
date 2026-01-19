package net.danygames2014.nyalib.mixin.item;

import net.danygames2014.nyalib.item.InventoryManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(Entity.class)
public class EntityMixin {
    @Shadow
    public double x;
    @Shadow
    public double y;
    @Shadow
    public double z;
    @Shadow
    public World world;
    @Unique
    private InventoryManager inventoryManager;

    @SuppressWarnings("MissingUnique")
    public InventoryManager getInventoryManager() {
        if (inventoryManager == null) {
            inventoryManager = new InventoryManager();
        }

        return inventoryManager;
    }

    @Inject(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtCompound;putBoolean(Ljava/lang/String;Z)V", shift = At.Shift.AFTER))
    public void writeManagedInventoryNbt(NbtCompound nbt, CallbackInfo ci) {
        if (inventoryManager != null) {
            NbtCompound managedInventoryNbt = new NbtCompound();
            getInventoryManager().writeNbt(managedInventoryNbt);
            nbt.put("ManagedInventoryData", managedInventoryNbt);
        }
    }

    @Inject(method = "read", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setRotation(FF)V", shift = At.Shift.AFTER))
    public void readManagedInventoryNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("ManagedInventoryData")) {
            NbtCompound managedInventoryNbt = nbt.getCompound("ManagedInventoryData");
            getInventoryManager().readNbt(managedInventoryNbt);
        }
    }
    
    @Inject(method = "markDead", at = @At(value = "HEAD"))
    public void dropInventoryOnDeath(CallbackInfo ci) {
        if (inventoryManager == null) {
            return;
        }

        Random random = new Random();
        
        for (ItemStack stack : inventoryManager.getInventory(null)) {
            if (stack == null) {
                continue;
            }

            float xPos = random.nextFloat() * 0.3F + 0.1F;
            float yPos = random.nextFloat() * 0.3F + 0.1F;
            float zPos = random.nextFloat() * 0.3F + 0.1F;

            ItemEntity itemEntity = new ItemEntity(world, (float) x + xPos, (float) y + yPos, (float) z + zPos, stack.copy());
            itemEntity.velocityX = (float) random.nextGaussian() * 0.02F;
            itemEntity.velocityY = (float) random.nextGaussian() * 0.02F + 0.1F;
            itemEntity.velocityZ = (float) random.nextGaussian() * 0.02F;
            world.spawnEntity(itemEntity);
        }
    }
}
