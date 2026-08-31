package net.danygames2014.nyalib.block;

import net.modificationstation.stationapi.api.block.BlockState;

public interface BlockEntityInit {
    /**
     * This method gets called when the BlockEntity is newly initialized or when its loaded from NBT
     * It is called after the world and coordinate fields have been set
     * 
     * @param state The {@link BlockState} at the position of this BlockEntity
     */
    void init(BlockState state);
}
