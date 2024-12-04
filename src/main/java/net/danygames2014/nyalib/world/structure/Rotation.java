package net.danygames2014.nyalib.world.structure;

public enum Rotation {
    NONE(false, false, false),
    ROTATE_90(true,true,false),
    ROTATE_180(false, true, true),
    ROTATE_270(true, false, true);

    public final boolean swapXZ;
    public final boolean flipX;
    public final boolean flipZ;

    Rotation(boolean swapXZ, boolean flipX, boolean flipZ) {
        this.swapXZ = swapXZ;
        this.flipX = flipX;
        this.flipZ = flipZ;
    }

    public int getXMultiplier(){
        return flipX ? -1 : 1;
    }

    public int getZMultiplier(){
        return flipZ ? -1 : 1;
    }

    public static Rotation getRotation(int index){
        return switch (index){
            case 1 -> ROTATE_90;
            case 2 -> ROTATE_180;
            case 3 -> ROTATE_270;
            default -> NONE;
        };
    }
}
