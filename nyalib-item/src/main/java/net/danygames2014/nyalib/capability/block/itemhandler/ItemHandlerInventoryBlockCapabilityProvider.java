package net.danygames2014.nyalib.capability.block.itemhandler;

import net.danygames2014.nyalib.capability.block.BlockCapabilityProvider;
import net.minecraft.inventory.Inventory;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ItemHandlerInventoryBlockCapabilityProvider extends BlockCapabilityProvider<ItemHandlerBlockCapability> {
    @Override
    public @Nullable ItemHandlerBlockCapability getCapability(World world, int x, int y, int z) {
        if (world.getBlockEntity(x,y,z) instanceof Inventory inventory) {
            return new ItemHandlerInventoryBlockCapability(inventory);
        }
        return null;
    }
}
