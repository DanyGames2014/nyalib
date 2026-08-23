package net.danygames2014.nyalib.multipart;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.math.StationBlockPos;

public class MultipartEvent{
    public MultipartComponent component;
    public long time;
    public boolean initialized = false;

    private BlockPos pos;
    private int index;

    public MultipartEvent(MultipartComponent component, long time) {
        this.component = component;
        this.time = time;
        this.initialized = true;
    }

    private MultipartEvent(BlockPos pos, int index, long time) {
        this.pos = pos;
        this.index = index;
        this.time = time;
    }

    public void initialize(World world) {
        this.component = world.getMultipartState(pos.getX(), pos.getY(), pos.getZ()).components.get(index);
        this.initialized = true;
    }

    public NbtCompound writeNbt() {
        NbtCompound nbt = new NbtCompound();
        int index = component.state.components.indexOf(component);
        BlockPos pos = new BlockPos(component.x, component.y, component.z);
        nbt.putInt("index", index);
        nbt.putLong("pos", pos.asLong());
        nbt.putLong("time", time);
        return nbt;
    }

    public static MultipartEvent fromNbt(NbtCompound nbt) {
        BlockPos pos = StationBlockPos.fromLong(nbt.getLong("pos"));
        int index = nbt.getInt("index");
        long time = nbt.getLong("time");
        return new MultipartEvent(pos, index, time);
    }
}
