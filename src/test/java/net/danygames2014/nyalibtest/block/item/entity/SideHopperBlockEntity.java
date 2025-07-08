package net.danygames2014.nyalibtest.block.item.entity;

import net.danygames2014.nyalib.capability.Capability;
import net.danygames2014.nyalib.capability.CapabilityHelper;
import net.danygames2014.nyalib.capability.block.itemhandler.ItemHandlerBlockCapability;
import net.danygames2014.nyalib.item.ItemHandler;
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

            ItemHandlerBlockCapability source = CapabilityHelper.getCapability(world, x + facing.getOffsetX(), y, z + facing.getOffsetZ(), Capability.ITEM_HANDLER_CAPABILITY);
            ItemHandlerBlockCapability destination = CapabilityHelper.getCapability(world, x + facing.getOpposite().getOffsetX(), y , facing.getOpposite().getOffsetZ(), Capability.ITEM_HANDLER_CAPABILITY);
            
            if (source != null) {
                if (internalBuffer == null && source.canExtractItem(facing.getOpposite())) {
                    internalBuffer = source.extractItem(facing.getOpposite());
                }
            }
            
            

            if (world.getBlockEntity(x + facing.getOffsetX(), y, z + facing.getOffsetZ()) instanceof ItemHandler sourcex && world.getBlockEntity(x + facing.getOpposite().getOffsetX(), y, z + facing.getOpposite().getOffsetZ()) instanceof ItemHandler destinationx) {
                // If there is nothing in internal buffer, try to retrieve item
                if (internalBuffer == null) {
                    if (sourcex.canExtractItem(facing.getOpposite())) {
                        for (int i = 0; i < sourcex.getItemSlots(facing.getOpposite()); i++) {
                            internalBuffer = sourcex.extractItem(i, 999, facing.getOpposite());
                            if (internalBuffer != null) {
                                break;
                            }
                        }
                    }
                }

                // If there is something in internal buffer, try to get rid of it
                if (internalBuffer != null) {
                    if (destinationx.canInsertItem(facing)) {
                        internalBuffer = destinationx.insertItem(internalBuffer, facing);
                    }
                }
            }

            tickCounter = 10;
        } else {
            tickCounter--;
        }
    }
}
