package net.danygames2014.nyalib.network;

import com.google.common.collect.Sets;
import net.danygames2014.nyalib.NyaLib;
import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.*;

@SuppressWarnings("unused")
public class Network {
    protected HashMap<Vec3i, NetworkComponentEntry> components;
    public World world;
    public NetworkType type;
    protected int id;

    public Network(World world, NetworkType type) {
        this.world = world;
        this.type = type;
        components = new HashMap<>();
    }

    private Network(World world) {
        throw new IllegalArgumentException("The use of this contructor is not supported. Use the Network(World, NetworkType) constructor");
    }

    private Network(NetworkType type) {
        throw new IllegalArgumentException("The use of this contructor is not supported. Use the Network(World, NetworkType) constructor");
    }

    private Network() {
        throw new IllegalArgumentException("The use of this contructor is not supported. Use the Network(World, NetworkType) constructor");
    }

    public boolean isAt(int x, int y, int z) {
        Vec3i pos = new Vec3i(x, y, z);
        return components.containsKey(pos);
    }

    public NetworkComponentEntry getAt(int x, int y, int z) {
        Vec3i pos = new Vec3i(x, y, z);
        return components.get(pos);
    }

    public NetworkComponentEntry getAt(Vec3i pos) {
        return components.get(pos);
    }

    public void addBlock(int x, int y, int z, Block block) {
        if (block instanceof NetworkComponent component) {
            components.put(new Vec3i(x, y, z), new NetworkComponentEntry(block, component));
            component.onAddedToNet(world, x, y, z, this);
        }
    }

    public boolean removeBlock(int x, int y, int z) {
        Vec3i pos = new Vec3i(x, y, z);
        if (components.containsKey(pos)) {

            if (components.get(pos).block() instanceof NetworkComponent component) {
                component.onRemovedFromNet(world, x, y, z, this);
            }

            components.remove(pos);
            return true;
        }
        return false;
    }

    /**
     * Called on every world tick
     */
    public void tick() {

    }

    /**
     * Called when there is a change to the network physical topology
     */
    public void update() {
        for (Map.Entry<Vec3i, NetworkComponentEntry> block : components.entrySet()) {
            Vec3i pos = block.getKey();

            if (block.getValue().block() instanceof NetworkComponent component) {
                component.update(world, pos.x, pos.y, pos.z, this);
            }
        }
    }

    /**
     * @param start The position to start walking from
     * @return A list of blocks discovered
     * @author paulevs
     */
    public HashSet<Vec3i> walk(Vec3i start) {
        ArrayList<Set<Vec3i>> edges = new ArrayList<>();
        HashSet<Vec3i> result = new HashSet<>();

        edges.add(Sets.newHashSet(start));
        edges.add(Sets.newHashSet());

        byte n = 0;
        boolean done = false;
        while (!done) {
            Set<Vec3i> oldEdge = edges.get(n & 1);
            Set<Vec3i> newEdge = edges.get((n + 1) & 1);
            n = (byte) ((n + 1) & 1);
            oldEdge.forEach(pos -> {
                for (Direction dir : Direction.values()) {
                    Vec3i side = new Vec3i(pos.x + dir.getOffsetX(), pos.y + dir.getOffsetY(), pos.z + dir.getOffsetZ());
                    if (components.containsKey(side) && !result.contains(side)) {
                        if (getAt(side).component().canConnectTo(world, pos.x, pos.y, pos.z, this, dir)) {
                            if (!(getAt(pos).block() instanceof NetworkEdgeComponent && getAt(side).block() instanceof NetworkEdgeComponent)) {
                                newEdge.add(side);
                            }
                        }
                    }
                }
            });
            done = oldEdge.isEmpty();
            result.addAll(oldEdge);
            oldEdge.clear();
        }

        return result;
    }

    public void writeNbt(NbtCompound tag) {

    }

    public void readNbt(NbtCompound tag) {

    }

    public NbtCompound toNbt() {
        NbtCompound tag = new NbtCompound();
        NbtList blocksNbt = new NbtList();

        tag.putInt("id", id);
        tag.put("blocks", blocksNbt);

        for (Map.Entry<Vec3i, NetworkComponentEntry> entry : components.entrySet()) {
            NbtCompound blockNbt = new NbtCompound();

            Vec3i pos = entry.getKey();
            blockNbt.putInt("x", pos.x);
            blockNbt.putInt("y", pos.y);
            blockNbt.putInt("z", pos.z);

            if (entry.getValue().block() instanceof NetworkComponent component) {
                component.writeNbt(world, pos.x, pos.y, pos.z, this, blockNbt);
            }

            blocksNbt.add(blockNbt);
        }

        this.writeNbt(tag);

        return tag;
    }

    public static Network fromNbt(NbtCompound tag, World world, Identifier networkTypeIdentifier) {
        NetworkType networkType = NetworkTypeRegistry.get(networkTypeIdentifier);

        if (networkType == null) {
            NyaLib.LOGGER.error("Network of type {} not found in registry. Has the modlist been changed? Skipping the loading of this network.", networkTypeIdentifier);
            return null;
        }

        Network network;

        try {
            network = networkType.getNetworkClass().getDeclaredConstructor(World.class, NetworkType.class).newInstance(world, networkType);
        } catch (Exception e) {
            NyaLib.LOGGER.error("Error when creating a network of type {}", networkType.getIdentifier(), e);
            return null;
        }

        network.components = new HashMap<>();

        network.id = tag.getInt("id");
        NbtList blocksNbt = tag.getList("blocks");

        // Iterate over all the block NBT Compounds
        for (int i = 0; i < blocksNbt.size(); i++) {
            // Get this block NBT Compound
            NbtCompound blockNbt = (NbtCompound) blocksNbt.get(i);

            // Fetch the position of the block
            Vec3i pos = new Vec3i(blockNbt.getInt("x"), blockNbt.getInt("y"), blockNbt.getInt("z"));

            // Fetch the type of the block
            Block block = world.getBlockState(pos.x, pos.y, pos.z).getBlock();

            // Mod NBT
            if (block instanceof NetworkComponent component) {
                component.readNbt(world, pos.x, pos.y, pos.z, network, blockNbt);

                // Put the block in Network
                network.components.put(
                        pos,
                        new NetworkComponentEntry(block, component)
                );
            }
        }

        network.readNbt(tag);

        // Return the loaded network
        return network;
    }

    public int getId() {
        return id;
    }
}
