package net.danygames2014.nyalib.mixininterface;

import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.math.Direction;

public interface MultipartAwareItemStack {
    default boolean useOnMultipart(PlayerEntity player, World world, int x, int y, int z, Direction face, Vec3d hitPos, MultipartComponent component) {
        return Util.assertImpl();
    }

    default float getMultipartMiningSpeedMultiplier(PlayerEntity player, World world, int x, int y, int z, MultipartComponent component) {
        return Util.assertImpl();
    }

    default void postMineMultipart(LivingEntity entity, World world, int x, int y, int z, MultipartComponent component) {
        Util.assertImpl();
    }

    default boolean isSuitableForMultipart(PlayerEntity player, World world, int x, int y, int z, MultipartComponent component) {
        return Util.assertImpl();
    }
}
