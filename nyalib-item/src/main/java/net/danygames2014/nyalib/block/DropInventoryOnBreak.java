package net.danygames2014.nyalib.block;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface DropInventoryOnBreak {
    default boolean shouldDropInventory(World world, int x, int y, int z) {
        return true;
    }
    
    default boolean shouldDropStack(World world, int x, int y, int z, int slot, ItemStack stack) {
        return true;
    }
}
