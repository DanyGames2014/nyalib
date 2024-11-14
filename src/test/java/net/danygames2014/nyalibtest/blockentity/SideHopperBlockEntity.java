package net.danygames2014.nyalibtest.blockentity;

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
            if(facing == null){
                facing = world.getBlockState(x,y,z).get(Properties.HORIZONTAL_FACING);
            }

            if(world.getBlockEntity(x + facing.getOffsetX(), y, z + facing.getOffsetZ()) instanceof ItemHandler south && world.getBlockEntity(x + facing.getOpposite().getOffsetX(), y, z + facing.getOpposite().getOffsetZ()) instanceof ItemHandler north){
                // If there is nothing in internal buffer, try to retrive item
                if (internalBuffer == null){
                    if(south.canExtractItem(facing.getOpposite())){
                        for (int i = 0; i < south.getItemSlots(facing.getOpposite()); i++) {
                            internalBuffer = south.extractItem(i, 999, facing.getOpposite());
                            if(internalBuffer != null){
                                break;
                            }
                        }
                    }
                }

                // If there is something in internal buffer, try to get rid of it
                if(internalBuffer != null){
                    if(north.canInsertItem(facing)){
                        internalBuffer = north.insertItem(internalBuffer, facing);
                    }
                }
            }

            tickCounter = 10;
        } else {
            tickCounter--;
        }
    }
}
