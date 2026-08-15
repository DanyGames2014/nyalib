package net.danygames2014.nyalib.util;

import net.modificationstation.stationapi.api.util.math.Direction;

public class MultipartDirectionUtil {
    public static Direction getAdjacentSide(Direction side, int index) {
        Direction.Axis faceAxis = side.getAxis();

        Direction.Axis axis1 = (faceAxis == Direction.Axis.X) ? Direction.Axis.Y : Direction.Axis.X;
        Direction.Axis axis2 = (faceAxis == Direction.Axis.Z) ? Direction.Axis.Y : Direction.Axis.Z;

        return switch (index & 3) {
            case 0 -> Direction.from(axis1, Direction.AxisDirection.POSITIVE);
            case 1 -> Direction.from(axis2, Direction.AxisDirection.POSITIVE);
            case 2 -> Direction.from(axis1, Direction.AxisDirection.NEGATIVE);
            case 3 -> Direction.from(axis2, Direction.AxisDirection.NEGATIVE);
            default -> Direction.DOWN;
        };
    }

    // TODO: confirm this works
    public static int rotateTo(Direction dir1, Direction dir2) {
        Direction current = dir1.getAxis().isVertical() ? Direction.NORTH : dir1;
        for (int rotation = 0; rotation < 4; rotation++) {
            if (current == dir2) {
                return rotation;
            }
            current = current.rotateYClockwise();
        }
        return -1;
    }
}
