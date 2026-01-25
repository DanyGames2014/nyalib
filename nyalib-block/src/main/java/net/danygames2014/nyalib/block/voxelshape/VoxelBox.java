package net.danygames2014.nyalib.block.voxelshape;

import net.minecraft.util.math.Box;

import java.util.Arrays;

public record VoxelBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
    public static VoxelBox voxelify(Box box) {
        return new VoxelBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }

    public static VoxelBox[] voxelify(Box[] boxes) {
        return Arrays.stream(boxes).map(VoxelBox::voxelify).toArray(VoxelBox[]::new);
    }

    public static Box devoxelify(VoxelBox voxelBox) {
        return Box.create(voxelBox.minX, voxelBox.minY, voxelBox.minZ, voxelBox.maxX, voxelBox.maxY, voxelBox.maxZ);
    }

    public static Box[] devoxelify(VoxelBox[] voxelBoxes) {
        return Arrays.stream(voxelBoxes).map(VoxelBox::devoxelify).toArray(Box[]::new);
    }

    public VoxelBox add(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return new VoxelBox(this.minX + minX, this.minY + minY, this.minZ + minZ, this.maxX + maxX, this.maxY + maxY, this.maxZ + maxZ);
    }

    public VoxelBox add(VoxelBox other) {
        return new VoxelBox(this.minX + other.minX, this.minY + other.minY, this.minZ + other.minZ, this.maxX + other.maxX, this.maxY + other.maxY, this.maxZ);
    }
}
