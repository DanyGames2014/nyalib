package net.danygames2014.nyalibtest.capability.meow;

import net.danygames2014.nyalib.capability.block.BlockCapabilityProvider;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import org.jetbrains.annotations.Nullable;

public class DefaultMeowBlockCapabilityProvider extends BlockCapabilityProvider<MeowBlockCapability> {
    @Override
    public @Nullable MeowBlockCapability getCapability(World world, int x, int y, int z) {
        BlockState state = world.getBlockState(x, y, z);
        
        if (state.isOf(Block.IRON_BLOCK) || state.isOf(Block.GOLD_BLOCK) || state.isOf(Block.DIAMOND_BLOCK)) {
            return new DefaultMeowBlockCapability(world, x, y, z);
        }
            
        return null;
    }
}
