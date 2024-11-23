package net.danygames2014.nyalib.network.energy;

import net.danygames2014.nyalib.network.Network;
import net.danygames2014.nyalib.network.NetworkType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import java.time.LocalDateTime;

public class EnergyNetwork extends Network {
    public EnergyNetwork(World world, NetworkType type) {
        super(world, type);
    }

    @Override
    public void tick() {
        for (var entry : this.components.entrySet()) {
            entry.getValue().data().putInt("test", world.random.nextInt(50));
        }
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        tag.putString("writtenOn", LocalDateTime.now().toString());
    }

    @Override
    public void readNbt(NbtCompound tag) {
        System.out.println("Reading network written at " + tag.getString("writtenOn"));
    }
}
