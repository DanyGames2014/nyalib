package net.danygames2014.nyalib.multipart;

import net.danygames2014.nyalib.block.voxelshape.VoxelShape;

public interface PartialOcclusionComponent {
    VoxelShape getPartialOcclusionShape();

    default boolean allowCompleteOcclusion() {
        return false;
    }
}
