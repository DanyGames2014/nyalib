package net.danygames2014.nyalib.network;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.HashMap;
import java.util.Map;

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
