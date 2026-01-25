package net.danygames2014.nyalib.util;

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
            return box.set(minZ, box.minY, 1 - minX, maxZ, box.maxY, 1 - maxX);
        } else {
            return Box.create(minZ, box.minY, 1 - minX, maxZ, box.maxY, 1 - maxX);
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
}
