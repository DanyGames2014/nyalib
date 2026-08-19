package net.danygames2014.nyalib.multipart;

import net.danygames2014.nyalib.block.voxelshape.VoxelShape;
import org.jetbrains.annotations.NotNull;

public interface PartialOcclusionComponent {
    @NotNull
    VoxelShape getPartialOcclusionShape();

    default boolean allowCompleteOcclusion() {
        return false;
    }
}
