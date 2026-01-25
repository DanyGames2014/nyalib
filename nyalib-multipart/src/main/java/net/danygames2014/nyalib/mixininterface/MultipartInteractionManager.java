package net.danygames2014.nyalib.mixininterface;

import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.math.Direction;

public interface MultipartInteractionManager {
    default void attackMultipart(int blockX, int blockY, int blockZ, Vec3d pos, Direction face, MultipartComponent component) {
        Util.assertImpl();
    }
    
    default boolean interactMultipart(ItemStack stack, int blockX, int blockY, int blockZ, Vec3d pos, Direction face, MultipartComponent component) {
        return Util.assertImpl();
    }
}
