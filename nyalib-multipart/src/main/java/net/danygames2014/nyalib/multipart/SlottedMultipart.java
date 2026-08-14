package net.danygames2014.nyalib.multipart;

public interface SlottedMultipart {
    MultipartSlot getSlot();
    void setSlot(MultipartSlot slot);
    int getMask();
}
