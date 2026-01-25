package net.danygames2014.nyalibtest.mixin;

import net.danygames2014.nyalib.block.voxelshape.*;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings({"AddedMixinMembersNamePattern"})
@Mixin(FenceBlock.class)
public abstract class FenceBlockMixin extends Block implements HasVoxelShape, HasCollisionVoxelShape {
    @Unique
    private static final VoxelBox voxelshapes_BASE_VOXEL_DATA = new VoxelBox(0.375F, 0, 0.375F, 0.625F, 1.F, 0.625F);
    @Unique
    private static final VoxelBox voxelshapes_BOX_NEG_X = new VoxelBox(0, 0, 0.375F, 0.375F, 1.F, 0.625F);
    @Unique
    private static final VoxelBox voxelshapes_BOX_POS_X = new VoxelBox(0.625F, 0, 0.375F, 1.F, 1.F, 0.625F);
    @Unique
    private static final VoxelBox voxelshapes_BOX_NEG_Z = new VoxelBox(0.375F, 0, 0, 0.625F, 1.F, 0.375F);
    @Unique
    private static final VoxelBox voxelshapes_BOX_POS_Z = new VoxelBox(0.375F, 0, 0.625F, 0.625F, 1.F, 1.F);
    @Unique
    private static final VoxelBox voxelshapes_COLLISION_MODIFIER = new VoxelBox(0, 0, 0, 0, 0.5, 0);

    
    @Unique
    private static final VoxelData[] voxelshapes_VOXEL_DATUM = new VoxelData[16];
    @Unique
    private static final VoxelData[] voxelshapes_COLLISION_DATUM = new VoxelData[16];

    static {
        for (int i = 0; i < 16; i++) {
            VoxelData voxelData = new VoxelData(voxelshapes_BASE_VOXEL_DATA);
            VoxelData collisionData = new VoxelData(voxelshapes_BASE_VOXEL_DATA.add(voxelshapes_COLLISION_MODIFIER));

            if ((i & 1) != 0) {
                voxelData = voxelData.withBox(voxelshapes_BOX_POS_Z);
                collisionData = collisionData.withBox(voxelshapes_BOX_POS_Z.add(voxelshapes_COLLISION_MODIFIER));
            }
            if ((i & 2) != 0) {
                voxelData = voxelData.withBox(voxelshapes_BOX_NEG_Z);
                collisionData = collisionData.withBox(voxelshapes_BOX_NEG_Z.add(voxelshapes_COLLISION_MODIFIER));
            }
            if ((i & 4) != 0) {
                voxelData = voxelData.withBox(voxelshapes_BOX_NEG_X);
                collisionData = collisionData.withBox(voxelshapes_BOX_NEG_X.add(voxelshapes_COLLISION_MODIFIER));
            }
            if ((i & 8) != 0) {
                voxelData = voxelData.withBox(voxelshapes_BOX_POS_X);
                collisionData = collisionData.withBox(voxelshapes_BOX_POS_X.add(voxelshapes_COLLISION_MODIFIER));
            }

            voxelshapes_VOXEL_DATUM[i] = voxelData.preCache();
            voxelshapes_COLLISION_DATUM[i] = collisionData;
        }
    }

    public FenceBlockMixin(int id, Material material) {
        super(id, material);
    }

    @Unique
    private int voxelshapes_getVoxelDatumIndex(World world, int x, int y, int z) {
        boolean posX = world.getBlockId(x + 1, y, z) == id;
        boolean negX = world.getBlockId(x - 1, y, z) == id;
        boolean posZ = world.getBlockId(x, y, z + 1) == id;
        boolean negZ = world.getBlockId(x, y, z - 1) == id;

        int index = 0;
        if (posZ) index |= 1;
        if (negZ) index |= 2;
        if (negX) index |= 4;
        if (posX) index |= 8;

        return index;
    }

    @Override
    public @Nullable VoxelShape getVoxelShape(World world, int x, int y, int z) {
        return voxelshapes_VOXEL_DATUM[voxelshapes_getVoxelDatumIndex(world, x, y, z)].withOffset(x, y, z);
    }

    @Override
    public @Nullable VoxelShape getCollisionVoxelShape(World world, int x, int y, int z) {
        return voxelshapes_COLLISION_DATUM[voxelshapes_getVoxelDatumIndex(world, x, y, z)].withOffset(x, y, z);
    }
}
