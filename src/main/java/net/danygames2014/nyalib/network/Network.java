package net.danygames2014.nyalib.network;

import com.google.common.collect.Sets;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.*;

public class Network {
    protected HashMap<Vec3i, Identifier> blocks;
    public World world;
    protected int id;

    public Network(World world) {
        this.world = world;
        blocks = new HashMap<>();
    }

    public boolean isAt(int x, int y, int z) {
        Vec3i pos = new Vec3i(x, y, z);
        return blocks.containsKey(pos);
    }

    public void addBlock(int x, int y, int z, Identifier identifier){
        blocks.put(new Vec3i(x, y, z), identifier);
    }

    public boolean removeBlock(int x, int y, int z){
        if(blocks.containsKey(new Vec3i(x, y, z))){
            blocks.remove(new Vec3i(x, y, z));
            return true;
        }
        return false;
    }

    /**
     * @param start The position to start walking from
     * @return A list of blocks discovered
     * @author paulevs
     */
    public Set<Vec3i> walk(Vec3i start) {
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

        for (Map.Entry<Vec3i, Identifier> block : blocks.entrySet()) {
            NbtCompound blockNbt = new NbtCompound();

            Vec3i pos = block.getKey();
            blockNbt.putInt("x", pos.x);
            blockNbt.putInt("y", pos.y);
            blockNbt.putInt("z", pos.z);

            blocksNbt.add(blockNbt);
        }

        return tag;
    }

    public static Network readNbt(NbtCompound tag, World world) {
        Network network = new Network(world);
        network.blocks = new HashMap<>();

        network.id = tag.getInt("id");
        NbtList blocksNbt = tag.getList("blocks");

        for (int i = 0; i < blocksNbt.size(); i++) {
            NbtCompound blockNbt = (NbtCompound) blocksNbt.get(i);

            Vec3i pos = new Vec3i(blockNbt.getInt("x"), blockNbt.getInt("y"), blockNbt.getInt("z"));

            network.blocks.put(
                    pos,
                    BlockRegistry.INSTANCE.getId(world.getBlockState(pos.x, pos.y, pos.z).getBlock())
            );
        }

        return network;
    }

    public int getId() {
        return id;
    }
}
