package net.danygames2014.nyalib.capability.block.itemhandler;

import net.danygames2014.nyalib.capability.block.BlockCapabilityProvider;
import net.danygames2014.nyalib.item.block.ItemHandler;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ItemHandlerInterfaceBlockCapabilityProvider extends BlockCapabilityProvider<ItemHandlerBlockCapability> {
    @Override
    public @Nullable ItemHandlerBlockCapability getCapability(World world, int x, int y, int z) {
        if (world.getBlockEntity(x, y, z) instanceof ItemHandler itemHandler) {
            return new ItemHandlerInterfaceBlockCapability(itemHandler);
        }
        return null;
    }
}
