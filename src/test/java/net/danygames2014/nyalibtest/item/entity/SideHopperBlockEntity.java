package net.danygames2014.nyalibtest.item.entity;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.capability.CapabilityHelper;
import net.danygames2014.nyalib.capability.block.itemhandler.ItemHandlerBlockCapability;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.state.property.Properties;
import net.modificationstation.stationapi.api.util.math.Direction;

public class SideHopperBlockEntity extends BlockEntity {
    public int tickCounter;
    public ItemStack internalBuffer;
    public Direction facing;


    public SideHopperBlockEntity() {
        tickCounter = 0;

    }

    @Override
    public void tick() {
        if (tickCounter <= 0) {
            if (facing == null) {
                facing = world.getBlockState(x, y, z).get(Properties.HORIZONTAL_FACING);
            }

            ItemHandlerBlockCapability source = CapabilityHelper.getCapability(world, x + facing.getOpposite().getOffsetX(), y, z + facing.getOpposite().getOffsetZ(), NyaLib.NAMESPACE.id("item_handler"));
            ItemHandlerBlockCapability destination = CapabilityHelper.getCapability(world, x + facing.getOffsetX(), y , z + facing.getOffsetZ(), NyaLib.NAMESPACE.id("item_handler"));
            
            if (source != null) {
                if (internalBuffer == null && source.canExtractItem(facing)) {
                    internalBuffer = source.extractItem(facing);
                }
            }
            
            if (destination != null) {
                if (internalBuffer != null && destination.canInsertItem(facing.getOpposite())) {
                    internalBuffer = destination.insertItem(internalBuffer, facing.getOpposite());
                }
            }

            tickCounter = 10;
        } else {
            tickCounter--;
        }
    }
}
