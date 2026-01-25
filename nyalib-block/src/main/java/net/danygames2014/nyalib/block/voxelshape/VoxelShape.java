package net.danygames2014.nyalib.block.voxelshape;

import net.minecraft.util.math.Box;
import net.modificationstation.stationapi.api.util.math.Vec3d;

import java.util.List;

public class VoxelShape {
    private final VoxelData voxelData;
    private final Vec3d offset;

    protected VoxelShape(VoxelData voxelData, Vec3d offset) {
        this.voxelData = voxelData;
        this.offset = offset;
    }

    public List<Box> getOffsetBoxes() {
        return voxelData.getBoxes().stream().map((box) -> box.offset(offset.x, offset.y, offset.z)).toList();
    }

    public List<VoxelBox> getVoxelBoxes() {
        return voxelData.getVoxelBoxes();
    }

    public List<Box> getBoxes() {
        return voxelData.getBoxes();
    }

    public List<Line> getLines() {
        return voxelData.getLines();
    }

    public Vec3d getOffset() {
        return offset;
    }
}
