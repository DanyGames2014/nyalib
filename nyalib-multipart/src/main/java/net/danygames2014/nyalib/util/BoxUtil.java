package net.danygames2014.nyalib.util;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.math.Box;

public class BoxUtil {
    public static boolean isFullyCovered(Box target, ObjectArrayList<Box> covers) {
        ObjectArrayList<Box> remainingPieces = new ObjectArrayList<>();
        remainingPieces.add(target);

        for (Box cover : covers) {
            ObjectArrayList<Box> nextLevelPieces = new ObjectArrayList<>();
            for (Box piece : remainingPieces) {
                nextLevelPieces.addAll(subtract(piece, cover));
            }
            remainingPieces = nextLevelPieces;
            if (remainingPieces.isEmpty()) return true;
        }

        return remainingPieces.isEmpty();
    }

    public static ObjectArrayList<Box> subtract(Box box, Box other) {
        ObjectArrayList<Box> result = new ObjectArrayList<>();

        if (!box.intersects(other)) {
            result.add(box);
            return result;
        }

        double curMinX = box.minX, curMaxX = box.maxX;
        double curMinY = box.minY, curMaxY = box.maxY;
        double curMinZ = box.minZ, curMaxZ = box.maxZ;

        if (other.minX > curMinX) {
            result.add(Box.create(curMinX, curMinY, curMinZ, other.minX, curMaxY, curMaxZ));
            curMinX = other.minX;
        }
        if (other.maxX < curMaxX) {
            result.add(Box.create(other.maxX, curMinY, curMinZ, curMaxX, curMaxY, curMaxZ));
            curMaxX = other.maxX;
        }

        if (other.minY > curMinY) {
            result.add(Box.create(curMinX, curMinY, curMinZ, curMaxX, other.minY, curMaxZ));
            curMinY = other.minY;
        }
        if (other.maxY < curMaxY) {
            result.add(Box.create(curMinX, other.maxY, curMinZ, curMaxX, curMaxY, curMaxZ));
            curMaxY = other.maxY;
        }

        if (other.minZ > curMinZ) {
            result.add(Box.create(curMinX, curMinY, curMinZ, curMaxX, curMaxY, other.minZ));
        }
        if (other.maxZ < curMaxZ) {
            result.add(Box.create(curMinX, curMinY, other.maxZ, curMaxX, curMaxY, curMaxZ));
        }

        return result;
    }
}
