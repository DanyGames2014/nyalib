package net.danygames2014.nyalib.network.energy;

import net.danygames2014.nyalib.energy.EnergyHandler;
import net.danygames2014.nyalib.network.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class EnergyNetwork extends Network {
    public HashMap<Vec3i, NetworkComponentEntry> edges;
    public HashMap<Vec3i, ArrayList<SourcePath>> sourcesCache;

    public EnergyNetwork(World world, NetworkType type) {
        super(world, type);
        edges = new HashMap<>();
        sourcesCache = new HashMap<>();
    }

    @Override
    public void update() {
        super.update();

        sourcesCache.clear();
        for (NetworkComponentEntry componentEntry : components.values()) {
            if (componentEntry.component() instanceof NetworkEdgeComponent) {
                edges.put(componentEntry.pos(), componentEntry);
            }
        }
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

    public void requestEnergy(EnergyHandler requestor, Vec3i requestorPos, double amperage) {
        double remaining = amperage;

        // For each valid source, try to extract energy
        for (var source : getValidSources(requestorPos)) {
            double extracted = source.handler.extractEnergy(source.path.endFace, remaining);
            remaining -= extracted;

            // If we reached zero, there is no point in going further
            if (remaining <= 0) {
                break;
            }

            requestor.receiveEnergy(source.path().endFace, source.handler.getOutputVoltage(source.path().endFace), extracted);
        }
    }

    public ArrayList<SourcePath> getValidSources(Vec3i destination) {
        if (sourcesCache.containsKey(destination)) {
            return sourcesCache.get(destination);
        } else {
            ArrayList<SourcePath> sources = new ArrayList<>();

            for (var edge : edges.keySet()) {
                if (edge.equals(destination)) {
                    continue;
                }

                NetworkPath path = this.getPath(destination, edge);
                if (world.getBlockEntity(edge.x, edge.y, edge.z) instanceof EnergyHandler handler) {
                    if (handler.canExtractEnergy(path.endFace)) {
                        sources.add(new SourcePath(handler, path));
                    }
                }
            }

            sourcesCache.put(destination, sources);
            return sources;
        }
    }

    public record SourcePath(EnergyHandler handler, NetworkPath path) {
    }
}
