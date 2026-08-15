package net.danygames2014.nyalib.multipart;

public interface FaceMultipartRedstonePassthrough extends SlottedMultipart {
    default int getRedstonePassthroughMask() {
        return 0;
    }
}
