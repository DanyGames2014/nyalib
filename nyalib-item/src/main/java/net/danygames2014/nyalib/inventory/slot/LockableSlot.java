package net.danygames2014.nyalib.inventory.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.Util;

public interface LockableSlot {
    default boolean isLocked(int index, ItemStack stackInSlot, PlayerEntity player) {
        return Util.assertImpl();
    }
}
