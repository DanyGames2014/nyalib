package net.danygames2014.nyalib.multipart;

import net.minecraft.util.math.Vec3d;
import net.modificationstation.stationapi.api.util.math.Direction;

public class MultipartHitResult {
    public static MultipartHitResult lastHit;
    
    public int blockX;
    public int blockY;
    public int blockZ;
    public Vec3d pos;
    public Direction face;

    public MultipartHitResult(int blockX, int blockY, int blockZ, Vec3d pos, Direction face) {
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
        this.pos = pos;
        this.face = face;
    }
}
