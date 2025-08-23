package net.danygames2014.nyalib.mixin.item;

import net.danygames2014.nyalib.block.DropInventoryOnBreak;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(BlockWithEntity.class)
public class BlockWithEntityMixin {
    @Inject(method = "onBreak", at = @At(value = "HEAD"))
    public void dropIt(World world, int x, int y, int z, CallbackInfo ci) {
        BlockEntity blockEntity = world.getBlockEntity(x, y, z);

        if (this instanceof DropInventoryOnBreak dropInventoryOnBreak && blockEntity instanceof Inventory inventory) {
            if (!dropInventoryOnBreak.shouldDropInventory(world, x, y, z)) {
                return;
            }

            Random random = new Random();

            for (int i = 0; i < inventory.size(); ++i) {
                ItemStack stack = inventory.getStack(i);
                if (stack != null) {
                    float xPos = random.nextFloat() * 0.8F + 0.1F;
                    float yPos = random.nextFloat() * 0.8F + 0.1F;
                    float zPos = random.nextFloat() * 0.8F + 0.1F;

                    ItemEntity itemEntity = new ItemEntity(world, (float) x + xPos, (float) y + yPos, (float) z + zPos, stack.copy());
                    itemEntity.velocityX = (float) random.nextGaussian() * 0.05F;
                    itemEntity.velocityY = (float) random.nextGaussian() * 0.05F + 0.2F;
                    itemEntity.velocityZ = (float) random.nextGaussian() * 0.05F;
                    world.spawnEntity(itemEntity);
                }
            }
        }
    }
}
