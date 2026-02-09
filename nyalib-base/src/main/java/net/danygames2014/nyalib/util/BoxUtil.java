package net.danygames2014.nyalib.util;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.math.Box;
import net.modificationstation.stationapi.api.util.math.Direction;

@SuppressWarnings("SuspiciousNameCombination")
public class BoxUtil {

    public static Box rotateXClockwise(Box box) {
        return rotateXClockwise(box, false);
    }

    public static Box rotateXClockwise(Box box, boolean modifyBox) {
        double minY = box.minY;
        double maxY = box.maxY;

        double minZ = box.minZ;
        double maxZ = box.maxZ;

        if(modifyBox) {
            return box.set(box.minX, minZ, 1 - maxY, box.maxX, maxZ, 1 - minY);
        } else {
            return Box.create(box.minX, minZ, 1 - maxY, box.maxX, maxZ, 1 - minY);
        }
    }

    public static Box rotateXCounterClockwise(Box box) {
        return rotateXCounterClockwise(box, false);
    }

    public static Box rotateXCounterClockwise(Box box, boolean modifyBox) {
        double minY = box.minY;
        double maxY = box.maxY;

        double minZ = box.minZ;
        double maxZ = box.maxZ;

        if(modifyBox) {
            return box.set(box.minX, 1 - maxZ, minY, box.maxX, 1 - minZ, maxY);
        } else {
            return Box.create(box.minX, 1 - maxZ, minY, box.maxX, 1 - minZ, maxY);
        }
    }

    public static Box rotateYClockwise(Box box) {
        return rotateYClockwise(box, false);
    }

    public static Box rotateYClockwise(Box box, boolean modifyBox) {
        double minX = box.minX;
        double maxX = box.maxX;

        double minZ = box.minZ;
        double maxZ = box.maxZ;

        if(modifyBox) {
            return box.set(1 - maxZ, box.minY, minX, 1 - minZ, box.maxY, maxX);
        } else {
            return Box.create(1 - maxZ, box.minY, minX, 1 - minZ, box.maxY, maxX);
        }
    }

    public static Box rotateYCounterClockwise(Box box) {
        return rotateYCounterClockwise(box, false);
    }

    public static Box rotateYCounterClockwise(Box box, boolean modifyBox) {
        double minX = box.minX;
        double maxX = box.maxX;

        double minZ = box.minZ;
        double maxZ = box.maxZ;

        if(modifyBox) {
            return box.set(minZ, box.minY, 1 - maxX, maxZ, box.maxY, 1 - minX);
        } else {
            return Box.create(minZ, box.minY, 1 - maxX, maxZ, box.maxY, 1 - minX);
        }
    }

    public static Box rotateZClockwise(Box box) {
        return rotateZClockwise(box, false);
    }

    public static Box rotateZClockwise(Box box, boolean modifyBox) {
        double minX = box.minX;
        double maxX = box.maxX;

        double minY = box.minY;
        double maxY = box.maxY;

        if(modifyBox) {
            return box.set(minY, 1 - maxX, box.minZ, maxY, 1 - minX, box.maxZ);
        } else {
            return Box.create(minY, 1 - maxX, box.minZ, maxY, 1 - minX, box.maxZ);
        }
    }

    public static Box rotateZCounterClockwise(Box box) {
        return rotateZCounterClockwise(box, false);
    }

    public static Box rotateZCounterClockwise(Box box, boolean modifyBox) {
        double minX = box.minX;
        double maxX = box.maxX;

        double minY = box.minY;
        double maxY = box.maxY;

        if(modifyBox) {
            return box.set(1 - maxY, minX, box.minZ, 1 - minY, maxX, box.maxZ);
        } else {
            return Box.create(1 - maxY, minX, box.minZ, 1 - minY, maxX, box.maxZ);
        }
    }

    // normal facing is expected to be east (+x)
    public static Box rotate(Box box, Direction direction){
        Box origin = box.copy();
        switch (direction){
            case NORTH -> {
                rotateYCounterClockwise(origin, true);
            }
            case SOUTH -> {
                rotateYClockwise(origin, true);
            }
            case WEST -> {
                rotateYClockwise(origin, true);
                rotateYClockwise(origin, true);
            }
            case UP -> {
                rotateZCounterClockwise(origin, true);
            }
            case DOWN -> {
                rotateZClockwise(origin, true);
            }
        }
        return origin;
    }

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
