package net.danygames2014.nyalibtest.mixin;

import net.danygames2014.nyalib.block.voxelshape.HasVoxelShape;
import net.danygames2014.nyalib.block.voxelshape.VoxelBox;
import net.danygames2014.nyalib.block.voxelshape.VoxelData;
import net.danygames2014.nyalib.block.voxelshape.VoxelShape;
import net.minecraft.block.StairsBlock;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(StairsBlock.class)
public class StairsBlockMixin implements HasVoxelShape {
    @Unique
    private static final VoxelData voxelshapes_BASE_VOXEL_DATA = new VoxelData(new VoxelBox(0, 0, 0, 1, 0.5, 1));
    @Unique
    private static final VoxelData[] voxelshapes_VOXEL_DATUM = new VoxelData[4];

    static {
        voxelshapes_VOXEL_DATUM[0] = voxelshapes_BASE_VOXEL_DATA.withBox(new VoxelBox(.5, .5, 0, 1, 1, 1)).preCache();
        voxelshapes_VOXEL_DATUM[1] = voxelshapes_BASE_VOXEL_DATA.withBox(new VoxelBox(0, .5, 0, .5, 1, 1)).preCache();
        voxelshapes_VOXEL_DATUM[2] = voxelshapes_BASE_VOXEL_DATA.withBox(new VoxelBox(0, .5, .5, 1, 1, 1)).preCache();
        voxelshapes_VOXEL_DATUM[3] = voxelshapes_BASE_VOXEL_DATA.withBox(new VoxelBox(0, .5, 0, 1, 1, .5)).preCache();
    }

    @Override
    public @Nullable VoxelShape getVoxelShape(World world, int x, int y, int z) {
        int meta = world.getBlockMeta(x, y, z);
        if (meta < 0 || meta > 3) return null;
        return voxelshapes_VOXEL_DATUM[meta].withOffset(x, y, z);
    }
}
