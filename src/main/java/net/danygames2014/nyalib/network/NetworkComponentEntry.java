package net.danygames2014.nyalib.network;

import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;

/**
 * An entry of a component in a network
 * 
 * @param block The block that the component is
 * @param component The network component interface on the block
 * @param data Additional NBT data of the component. Beware! While this is saved, it wont persist if the component is moved into a different network. For example when splitting.
 */
public record NetworkComponentEntry(Block block, NetworkComponent component, NbtCompound data){
    
}
