package net.danygames2014.nyalib.multipart;

public interface FaceComponentRedstonePassthrough extends SlottedComponent {
    default int getRedstonePassthroughMask() {
        return 0;
    }
}
