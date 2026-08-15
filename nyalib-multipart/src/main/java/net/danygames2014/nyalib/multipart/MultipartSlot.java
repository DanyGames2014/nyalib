package net.danygames2014.nyalib.multipart;

public enum MultipartSlot {
    FACE_NEG_Y(0),
    FACE_POS_Y(1),
    FACE_NEG_Z(2), // north
    FACE_POS_Z(3), // south
    FACE_NEG_X(4), // west
    FACE_POS_X(5), // east

    CENTER(6),

    CORNER_BOT_NEG_X_NEG_Z(7),
    CORNER_TOP_NEG_X_NEG_Z(8),
    CORNER_BOT_NEG_X_POS_Z(9),
    CORNER_TOP_NEG_X_POS_Z(10),
    CORNER_BOT_POS_X_NEG_Z(11),
    CORNER_TOP_POS_X_NEG_Z(12),
    CORNER_BOT_POS_X_POS_Z(13),
    CORNER_TOP_POS_X_POS_Z(14),

    EDGE_MID_NEG_X_NEG_Z(15), //15
    EDGE_MID_NEG_X_POS_Z(16), //16
    EDGE_MID_POS_X_NEG_Z(17), //17
    EDGE_MID_POS_X_POS_Z(18), //18
    EDGE_BOT_NEG_X(19), //19
    EDGE_BOT_POS_X(20), //20
    EDGE_TOP_NEG_X(21), //21
    EDGE_TOP_POS_X(22), //22
    EDGE_BOT_NEG_Z(23), //23
    EDGE_TOP_NEG_Z(24), //24
    EDGE_BOT_POS_Z(25), //25
    EDGE_TOP_POS_Z(26),

    CUSTOM(-1);

    private static final MultipartSlot[] VALUES = values();

    private static final int[] edgeBetweenMap = new int[] { -1, -1, 8, 10, 4, 5, -1, -1, 9, 11, 6, 7, -1, -1, -1, -1, 0, 2, -1, -1, -1, -1, 1, 3 };

    public final int slotIndex;
    public final int mask;
    MultipartSlot(int slotIndex) {
        this.slotIndex = slotIndex;
        this.mask = 1 << slotIndex;
    }

    public static MultipartSlot fromOrdinal(int ordinal) {
        if (ordinal < 0) {
            ordinal = 0;
        }
        if(ordinal >= VALUES.length) {
            ordinal = VALUES.length - 1;
        }
        return VALUES[ordinal];
    }

    public static MultipartSlot fromSlotIndex(int index) {
        if (index == -1) {
            return CUSTOM;
        }
        if (index >= 0 && index < 27) {
            return VALUES[index];
        }
        return CUSTOM;
    }

    public static MultipartSlot fromMask(int mask) {
        if (mask <= 0 || (mask & (mask - 1)) != 0) {
            return CUSTOM;
        }

        int index = Integer.numberOfTrailingZeros(mask);

        if (index < 27) {
            return VALUES[index];
        }

        return CUSTOM;
    }

    public int getPriority() {
        if(ordinal() < 6) {
            return  2;
        } else if(ordinal() < 15) {
            return 1;
        }
        return 0;
    }

    public static int edgeAxisMask(int e)
    {
        return switch (e >> 2) {
            case 0 -> 6;
            case 1 -> 5;
            case 2 -> 3;
            default -> 0;
        };
    }

    public static int unpackEdgeBits(int e)
    {
        return switch (e >> 2) {
            case 0 -> (e & 3) << 1;
            case 1 -> (e & 2) >> 1 | (e & 1) << 2;
            case 2 -> (e & 3);
            default -> 0;
        };
    }

    public static int packEdgeBits(int e, int bits)
    {
        return switch (e >> 2) {
            case 0 -> e & 0xC | bits >> 1;
            case 1 -> e & 0xC | (bits & 4) >> 2 | (bits & 1) << 1;
            case 2 -> e & 0xC | bits & 3;
            default -> 0;
        };
    }

    public static int edgeBetween(int s1, int s2) {
        if (s2 < s1) {
            return edgeBetween(s2, s1);
        }
        if ((s1 & 6) == (s2 & 6)) {
            throw new IllegalArgumentException("Faces " + s1 + " and " + s2 + " are opposites");
        }
        return 15 + edgeBetweenMap[s1 * 6 + s2];
    }
}
