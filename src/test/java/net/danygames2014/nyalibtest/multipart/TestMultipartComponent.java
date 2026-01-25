package net.danygames2014.nyalibtest.multipart;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.minecraft.util.math.Box;

public class TestMultipartComponent extends MultipartComponent {
    @Override
    public ObjectArrayList<Box> getBoundingBoxes() {
        ObjectArrayList<Box> boxes = new ObjectArrayList<>();
        boxes.add(Box.createCached(this.x + 0.5D, this.y, this.z, this.x + 1.0D, this.y + 1.0D, this.z + 1.0D));
        return boxes;
    }
}
