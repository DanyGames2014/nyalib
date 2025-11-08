package net.danygames2014.nyalib.network;

import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.danygames2014.nyalib.util.AStar;
import net.minecraft.util.math.Vec3i;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.HashMap;
import java.util.Map;

public class NetworkPathManager {
    Network network;
    HashMap<Vec3i, HashMap<Vec3i, NetworkPath>> pathCache;

    private NetworkPathManager() {
    }

    public NetworkPathManager(Network network) {
        this.network = network;
        pathCache = new HashMap<>();
    }

    public void clearCache() {
        pathCache.clear();
    }
    
    public NetworkPath getPath(Vec3i from, Vec3i to) {
        // If there isnt a hashmap to the source component, create it
        if (!pathCache.containsKey(from)) {
            pathCache.put(from, new HashMap<>());
        }

        // If the destination path isnt cached, compute it. If it is cached, validate it
        if (!pathCache.get(from).containsKey(to) || !validatePath(pathCache.get(from).get(to))) {
            NetworkPath path = computePath(from, to);

            if (path != null) {
                pathCache.get(from).put(to, path);
            } else {
                return null;
            }
        }

        // Return the path
        return pathCache.get(from).get(to);
    }

    public boolean validatePath(NetworkPath path) {
        for (Vec3i point : path.path) {
            if (!network.components.containsKey(point)) {
                return false;
            }
        }

        return network.isPathValid(path);
    }

    public NetworkPath computePath(Vec3i from, Vec3i to) {
        // Calculate Path
        ObjectSet<Vec3i> availibleNodes = network.getNonEdgeNodes().keySet();
        availibleNodes.add(from);
        availibleNodes.add(to);
        AStar aStar = new AStar(from, to, availibleNodes.toArray(new Vec3i[0]));
        Vec3i[] path = aStar.calculate();

        if (path == null || path.length == 0) {
            return null;
        }

        // Calculate Path Cost
        int cost = 0;
        for (Vec3i pos : path) {
            if (network.world.getBlockState(pos.x, pos.y, pos.z).getBlock() instanceof NetworkComponent component) {
                cost += component.getPathingCost(network.world, pos.x, pos.y, pos.z, network);
            }
        }

        // Determine the face
        Vec3i end = path[path.length - 1];
        Vec3i beforeEnd = path[path.length - 2];

        Direction endFace = null;
        for (Direction dir : Direction.values()) {
            if (beforeEnd.x + dir.getOffsetX() == end.x && beforeEnd.y + dir.getOffsetY() == end.y && beforeEnd.z + dir.getOffsetZ() == end.z) {
                endFace = dir.getOpposite();
            }
        }

        Direction startFace = null;

        if (path.length >= 2) {
            Vec3i start = path[0];
            Vec3i afterStart = path[1];

            for (Direction dir : Direction.values()) {
                if (start.x + dir.getOffsetX() == afterStart.x && start.y + dir.getOffsetY() == afterStart.y && start.z + dir.getOffsetZ() == afterStart.z) {
                    startFace = dir;
                }
            }
        }


        // Return path
        return new NetworkPath(from, startFace, to, endFace, path, cost);
    }
}
