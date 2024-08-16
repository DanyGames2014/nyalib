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
    protected HashMap<Vec3i, Block> blocks;
    public World world;
    public NetworkType type;
    protected int id;

    public Network(World world, NetworkType type) {
        this.world = world;
        this.type = type;
        blocks = new HashMap<>();
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
        return blocks.containsKey(pos);
    }

    public void addBlock(int x, int y, int z, Block block) {
        blocks.put(new Vec3i(x, y, z), block);

        if (block instanceof NetworkComponent component) {
            component.onAddedToNet(x, y, z, this, world);
        }
    }

    public boolean removeBlock(int x, int y, int z) {
        Vec3i pos = new Vec3i(x, y, z);
        if (blocks.containsKey(pos)) {

            if (blocks.get(pos) instanceof NetworkComponent component) {
                component.onRemovedFromNet(x, y, z, this, world);
            }

            blocks.remove(pos);
            return true;
        }
        return false;
    }

    public void update() {
        for (Map.Entry<Vec3i, Block> block : blocks.entrySet()) {
            Vec3i pos = block.getKey();

            if (block.getValue() instanceof NetworkComponent component) {
                component.update(pos.x, pos.y, pos.z, this, world);
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
        boolean added = true;
        while (added) {
            Set<Vec3i> oldEdge = edges.get(n & 1);
            Set<Vec3i> newEdge = edges.get((n + 1) & 1);
            n = (byte) ((n + 1) & 1);
            oldEdge.forEach(pos -> {
                for (Direction dir : Direction.values()) {
                    Vec3i side = new Vec3i(pos.x + dir.getOffsetX(), pos.y + dir.getOffsetY(), pos.z + dir.getOffsetZ());
                    if (blocks.containsKey(side) && !result.contains(side)) {
                        newEdge.add(side);
                    }
                }
            });
            added = !oldEdge.isEmpty();
            result.addAll(oldEdge);
            oldEdge.clear();
        }

        return result;
    }

    public NbtCompound writeNbt() {
        NbtCompound tag = new NbtCompound();
        NbtList blocksNbt = new NbtList();

        tag.putInt("id", id);
        tag.put("blocks", blocksNbt);

        for (Map.Entry<Vec3i, Block> block : blocks.entrySet()) {
            NbtCompound blockNbt = new NbtCompound();

            Vec3i pos = block.getKey();
            blockNbt.putInt("x", pos.x);
            blockNbt.putInt("y", pos.y);
            blockNbt.putInt("z", pos.z);

            if (block.getValue() instanceof NetworkComponent component) {
                component.writeNbt(pos.x, pos.y, pos.z, this, world, blockNbt);
            }

            blocksNbt.add(blockNbt);
        }

        return tag;
    }

    public static Network readNbt(NbtCompound tag, World world, Identifier networkTypeIdentifier) {
        NetworkType networkType = NetworkTypeRegistry.get(networkTypeIdentifier);

        if(networkType == null){
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

        network.blocks = new HashMap<>();

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
                component.readNbt(pos.x, pos.y, pos.z, network, world, blockNbt);
            }

            // Put the block in Network
            network.blocks.put(
                    pos,
                    block
            );
        }

        // Return the loaded network
        return network;
    }

    public int getId() {
        return id;
    }
}
