package net.danygames2014.nyalibtest.capability.improper;

import net.danygames2014.nyalib.capability.block.BlockCapabilityProvider;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DefaultImproperBlockCapabilityProvider extends BlockCapabilityProvider<ImproperBlockCapability> {
    @Override
    public @Nullable DefaultImpromperBlockCapability getCapability(World world, int x, int y, int z) {
        return new DefaultImpromperBlockCapability(world, x, y, z);
    }
}
