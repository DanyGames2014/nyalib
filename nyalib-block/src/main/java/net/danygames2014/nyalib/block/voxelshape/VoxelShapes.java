package net.danygames2014.nyalib.block.voxelshape;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VoxelShapes {
    public static VoxelShape union(VoxelShape shape1, VoxelShape shape2) {
        if(shape1 == null || shape1.getBoxes().isEmpty()) {
            return shape2;
        }
        if(shape2 == null || shape2.getBoxes().isEmpty()) {
            return shape1;
        }

        List<Box> boxes = new ArrayList<>(shape1.getBoxes());
        boxes.addAll(shape2.getBoxes());

        VoxelData voxelData = new VoxelData(boxes.toArray(new Box[0]));
        return voxelData.withOffset(shape1.getOffset());
    }

    public static boolean overlaps(VoxelShape shape1, VoxelShape shape2) {
        if(shape1 == null || shape2 == null) {
            return false;
        }

        List<Box> boxes1 = shape1.getOffsetBoxes();
        List<Box> boxes2 = shape2.getOffsetBoxes();

        for(Box b1 : boxes1) {
            for (Box b2 : boxes2) {
                if(b1.intersects(b2)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Nullable
    public static VoxelShape subtract(VoxelShape shape1, VoxelShape shape2) {
        if(shape1 == null || shape1.getVoxelBoxes().isEmpty()) {
            return null;
        }
        if(shape2 == null || shape2.getVoxelBoxes().isEmpty()) {
            return null;
        }

        ObjectArrayList<Box> remainingBoxes = new ObjectArrayList<>(shape1.getOffsetBoxes());
        ObjectArrayList<Box> cutterBoxes = new ObjectArrayList<>(shape2.getOffsetBoxes());

        for(Box cutter : cutterBoxes) {
            ObjectArrayList<Box> cutBoxes = new ObjectArrayList<>();

            for(Box box : remainingBoxes) {
                cutBoxes.addAll(subtractBox(box, cutter));
            }
            remainingBoxes = cutBoxes;
            if(remainingBoxes.isEmpty()) {
                break;
            }
        }

        if(remainingBoxes.isEmpty()) {
            return null;
        }
        VoxelData data = new VoxelData(remainingBoxes.toArray(new Box[0]));
        return data.withOffset(shape1.getOffset());
    }

    private static ObjectArrayList<Box> subtractBox(Box target, Box cutter) {
        ObjectArrayList<Box> result = new ObjectArrayList<>();

        if(!target.intersects(cutter)) {
            result.add(target);
            return result;
        }

        double curMinX = target.minX;
        double curMinY = target.minY;
        double curMinZ = target.minZ;

        double curMaxX = target.maxX;
        double curMaxY = target.maxY;
        double curMaxZ = target.maxZ;

        if (cutter.minX > curMinX) {
            result.add(Box.create(curMinX, curMinY, curMinZ, cutter.minX, curMaxY, curMaxZ));
            curMinX = cutter.minX;
        }
        if (cutter.maxX < curMaxX) {
            result.add(Box.create(cutter.maxX, curMinY, curMinZ, curMaxX, curMaxY, curMaxZ));
            curMaxX = cutter.maxX;
        }

        if (cutter.minY > curMinY) {
            result.add(Box.create(curMinX, curMinY, curMinZ, curMaxX, cutter.minY, curMaxZ));
            curMinY = cutter.minY;
        }
        if (cutter.maxY < curMaxY) {
            result.add(Box.create(curMinX, cutter.maxY, curMinZ, curMaxX, curMaxY, curMaxZ));
            curMaxY = cutter.maxY;
        }

        if (cutter.minZ > curMinZ) {
            result.add(Box.create(curMinX, curMinY, curMinZ, curMaxX, curMaxY, cutter.minZ));
        }
        if (cutter.maxZ < curMaxZ) {
            result.add(Box.create(curMinX, curMinY, cutter.maxZ, curMaxX, curMaxY, curMaxZ));
        }

        return result;
    }
}
