package net.danygames2014.nyalib.network;

import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;

public record NetworkComponentEntry(Block block, NetworkComponent component, NbtCompound data){
    
}
