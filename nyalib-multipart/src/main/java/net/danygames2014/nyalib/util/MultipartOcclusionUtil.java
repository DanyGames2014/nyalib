package net.danygames2014.nyalib.util;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.danygames2014.nyalib.block.voxelshape.VoxelShape;
import net.danygames2014.nyalib.block.voxelshape.VoxelShapes;
import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.danygames2014.nyalib.multipart.PartialOcclusionComponent;

public class MultipartOcclusionUtil {
    public static boolean partialOcclusionTest(ObjectArrayList<MultipartComponent> existingComponents, PartialOcclusionComponent newComponent) {
        ObjectArrayList<PartialOcclusionComponent> partialComponents = new ObjectArrayList<>();
        for(MultipartComponent component : existingComponents) {
            if(component instanceof PartialOcclusionComponent partial) {
                partialComponents.add(partial);
            }
        }
        partialComponents.add(newComponent);

        for(PartialOcclusionComponent partial1 : partialComponents) {
            if(partial1.allowCompleteOcclusion()) {
                continue;
            }
            VoxelShape shape = partial1.getPartialOcclusionShape();
            for(PartialOcclusionComponent partial2 : partialComponents) {
                if(partial1 == partial2 || partial2.allowCompleteOcclusion()) {
                    continue;
                }
                shape = VoxelShapes.subtract(shape, partial2.getPartialOcclusionShape());
                if(shape == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean componentOcclusionTest(MultipartComponent component1, MultipartComponent component2) {
        if(component1.getOcclusionShape() == null) {
            return true;
        }

        VoxelShape shape = component2.getOcclusionShape();
        if(component2 instanceof PartialOcclusionComponent partial) {
            shape = shape != null ? VoxelShapes.union(shape, partial.getPartialOcclusionShape()) : partial.getPartialOcclusionShape();
        }

        return !VoxelShapes.overlaps(shape, component1.getOcclusionShape());
    }
}
