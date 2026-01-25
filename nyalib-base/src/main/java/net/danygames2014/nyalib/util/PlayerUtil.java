package net.danygames2014.nyalib.util;

import net.minecraft.entity.player.PlayerEntity;
import net.modificationstation.stationapi.api.util.math.Vec3d;

public class PlayerUtil {
    public static Vec3d getRenderPosition(PlayerEntity player, float tickDelta){
        double x = player.lastTickX + (player.x - player.lastTickX) * (double)tickDelta;
        double y = player.lastTickY + (player.y - player.lastTickY) * (double)tickDelta;
        double z = player.lastTickZ + (player.z - player.lastTickZ) * (double)tickDelta;
        return  new Vec3d(x, y, z);
    }
}
