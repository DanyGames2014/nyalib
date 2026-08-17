package net.danygames2014.nyalib.util;

import net.modificationstation.stationapi.api.util.math.Direction;

public class MultipartDirectionUtil {

    private static final int[] ROTATION_MAP = new int[] {
            -1, -1, 2, 0,
            1, 3, -1, -1,
            2, 0, 3, 1,
            2, 0, -1, -1,
            3, 1, 2, 0,
            -1, -1, 1, 3,
            2, 0, 1, 3,
            -1, -1, 2, 0,
            3, 1, -1, -1
    };

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

    public static int rotateTo(Direction.Axis axis, Direction direction) {
        Direction baseDir = Direction.from(axis, Direction.AxisDirection.NEGATIVE);
        return rotateTo(baseDir, direction);
    }

    public static int rotateTo(Direction dir1, Direction dir2) {
        if (dir1.getAxis() == dir2.getAxis()) {
            throw new IllegalArgumentException("Faces " + dir1 + " and " + dir2 + " are in the same axis or opposite");
        }
        return ROTATION_MAP[dir1.getId() * 6 + dir2.getId()];
    }

    public static Direction getAttachmentDirectionFromWallMountedBlockMeta(int meta) {
        return switch (meta) {
            case 1 -> Direction.WEST;
            case 2 -> Direction.EAST;
            case 3 -> Direction.NORTH;
            case 4 -> Direction.SOUTH;
            default -> Direction.DOWN;
        };
    }
}
