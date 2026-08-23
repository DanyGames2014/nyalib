package net.danygames2014.nyalib.network;

import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.Objects;

/**
 * An entry of a component in a network
 *
 */
public final class NetworkComponentEntry {
    /**
     * The world the component is in
     */
    private final World world;
    /**
     * The world position at which the component is
     */
    public final Vec3i pos;
    /**
     * The block which is the component
     */
    public Block block;
    /**
     * The network component interface
     */
    public final NetworkComponent component;
    /**
     * Additional NBT data of the component. Beware! While this is saved, it wont persist if the component is moved into a different network. For example when splitting
     */
    public final NbtCompound data;

    /**
     * @param world     The world the component is in
     * @param pos       The world position at which the component is
     * @param block     The block that the component is
     * @param component The network component interface on the block
     * @param data      Additional NBT data of the component. Beware! While this is saved, it wont persist if the component is moved into a different network. For example when splitting.
     */
    public NetworkComponentEntry(World world, Vec3i pos, Block block, NetworkComponent component, NbtCompound data) {
        this.world = world;
        this.pos = pos;
        this.block = block;
        this.component = component;
        this.data = data;
    }

    public Block getBlock() {
        if (block == null && world.isPosLoaded(pos.x, pos.y, pos.z)) {
            block = world.getBlockState(pos.x, pos.y, pos.z).getBlock();
        }

        return block;
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
