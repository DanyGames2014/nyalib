package net.danygames2014.nyalibtest.capability.block;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.capability.block.BlockCapabilityRegistry;
import net.danygames2014.nyalib.capability.block.itemhandler.ItemHandlerBlockCapability;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;

public class ItemHandlerBlockCapabilityTesterBlock extends TemplateBlock {
    public ItemHandlerBlockCapabilityTesterBlock(Identifier identifier) {
        super(identifier, Material.METAL);
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        for (Direction side : Direction.values()) {
            ItemHandlerBlockCapability capability = null;

            // Method 1
            capability = BlockCapabilityRegistry.getCapability(world, x + side.getOffsetX(), y + side.getOffsetY(), z + side.getOffsetZ(), NyaLib.NAMESPACE.id("item_handler"));

            // Method 2
//            capability = BlockCapabilityRegistry.getCapability(world, x + side.getOffsetX(), y + side.getOffsetY(), z + side.getOffsetZ(), ItemHandlerBlockCapability.class);
            

            // Check if capability was found
            if (capability == null) {
                continue;
            }

            // Extract Item and drop it
            ItemStack stack = capability.extractItem(side.getOpposite());
            if (stack != null) {
                this.dropStack(world, x, y + 1, z, stack);
            }
        }

        return true;
    }
}
