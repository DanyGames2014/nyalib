package net.danygames2014.nyalib.network;

import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3i;

import java.util.Objects;

/**
 * An entry of a component in a network
 *
 */
public final class NetworkComponentEntry {
    public final Vec3i pos;
    public final Block block;
    public final NetworkComponent component;
    public final NbtCompound data;

    /**
     * @param block     The block that the component is
     * @param component The network component interface on the block
     * @param data      Additional NBT data of the component. Beware! While this is saved, it wont persist if the component is moved into a different network. For example when splitting.
     */
    public NetworkComponentEntry(Vec3i pos, Block block, NetworkComponent component, NbtCompound data) {
        this.pos = pos;
        this.block = block;
        this.component = component;
        this.data = data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos, block, component, data);
    }

    @Override
    public String toString() {
        return "NetworkComponentEntry[" +
                "pos=" + pos + ", " +
                "block=" + block + ", " +
                "component=" + component + ", " +
                "data=" + data + ']';
    }


}
