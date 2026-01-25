package net.danygames2014.nyalib.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * When implemented, the item will get additional hit vector in the useOnBlock method
 * <p>
 * WARNING: The original useOnBlock method will not be called     
 */
public interface EnhancedPlacementContextItem {
    boolean useOnBlock(ItemStack stack, PlayerEntity player, World world, int x, int y, int z, int side, Vec3d hitVec);
}
