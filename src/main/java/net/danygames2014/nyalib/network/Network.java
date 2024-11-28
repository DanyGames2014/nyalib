package net.danygames2014.nyalib.network;

import net.danygames2014.nyalib.NyaLib;
import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Network {
    protected HashMap<Vec3i, NetworkComponentEntry> components;
    public NetworkPathManager pathManager;
    public World world;
    public NetworkType type;
    protected int id;

    public Network(World world, NetworkType type) {
        this.world = world;
        this.type = type;
        components = new HashMap<>();
        pathManager = new NetworkPathManager(this);
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

    public boolean isAt(Vec3i pos) {
        return components.containsKey(pos);
    }

    public NetworkComponentEntry getEntry(int x, int y, int z) {
        Vec3i pos = new Vec3i(x, y, z);
        return components.get(pos);
    }

    public NetworkComponentEntry getEntry(Vec3i pos) {
        return components.get(pos);
    }

    public NetworkPath getPath(Vec3i from, Vec3i to) {
        return pathManager.getPath(from, to);
    }

    public NetworkPath getPath(NetworkComponentEntry from, NetworkComponentEntry to) {
        return getPath(from.pos(), to.pos());
    }

    /**
     * Allows you to add a check on paths validity,
     * Keep in mind that the path manager already checks if all components on the path exist
     *
     * @param path The path to validate
     * @return <code>true</code> if the path is valid
     */
    public boolean isPathValid(NetworkPath path) {
        return true;
    }

    public void addBlock(int x, int y, int z, Block block) {
        if (block instanceof NetworkComponent component) {
            Vec3i pos = new Vec3i(x, y, z);
            components.put(pos, new NetworkComponentEntry(pos, block, component, new NbtCompound()));
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
     * Called before entities/block entities are ticked
     */
    public void tick() {

    }

    /**
     * Called after entities/block entities are ticked
     */
    public void postEntityTick() {

    }

    /**
     * Called on every world tick, which is after entities/block entities are ticked
     */
    public void worldTick() {

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

    public ArrayList<Vec3i> walk(Vec3i start) {
        // ArrayList for list of blocks yet to explore
        ArrayList<Vec3i> open = new ArrayList<>();
        // ArrayList for list of blocks that have been found
        ArrayList<Vec3i> closed = new ArrayList<>();

        // Add the starting position to explore
        open.add(start);

        // Go until open isnt empty
        while (!open.isEmpty()) {
            // Get the position to explore
            Vec3i pos = open.get(0);
            // Look at all of its sides
            for (Direction dir : Direction.values()) {
                // Get the side and see if there is a block on it. Then check if it doesnt already exist
                Vec3i side = new Vec3i(pos.x + dir.getOffsetX(), pos.y + dir.getOffsetY(), pos.z + dir.getOffsetZ());
                if (components.containsKey(side) && !closed.contains(side)) {
                    // Check if the side block can connect to this block and reverse
                    if (getEntry(side).component().canConnectTo(world, pos.x, pos.y, pos.z, this, dir) && getEntry(pos).component().canConnectTo(world, side.x, side.y, side.z, this, dir)) {
                        // Check if the component is an edge, or a node
                        NetworkComponent component = getEntry(side).component();
                        if (component instanceof NetworkNodeComponent) {
                            open.add(side);
                        } else if (component instanceof NetworkEdgeComponent) {
                            closed.add(side);
                        }
                    }
                }
            }

            // Add the position to closed and remove it from open
            closed.add(pos);
            open.remove(pos);
        }

        return closed;
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

            blockNbt.put("entryData", entry.getValue().data());

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
                        new NetworkComponentEntry(pos, block, component, blockNbt.getCompound("entryData"))
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
