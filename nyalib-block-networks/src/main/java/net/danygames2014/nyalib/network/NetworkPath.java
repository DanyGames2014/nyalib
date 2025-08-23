package net.danygames2014.nyalib.network;

import net.minecraft.util.math.Vec3i;
import net.modificationstation.stationapi.api.util.math.Direction;

public class NetworkPath {
    /**
     * The starting component
     */
    public final Vec3i start;

    public final Direction startFace;
    
    /**
     * The destination component
     */
    public final Vec3i end;
    
    /**
     * The face of the destination component
     */
    public final Direction endFace;

    /**
     * The cost of the path, unless modified this is 1 unit / block
     * Higher Cost = Worse Path
     */
    public final int cost;
    
    /**
     * The path taken. Including the start and end edges/nodes
     */
    public final Vec3i[] path;

    public NetworkPath(Vec3i start, Direction startFace, Vec3i end, Direction endFace, Vec3i[] path, int cost) {
        this.start = start;
        this.startFace = startFace;
        this.end = end;
        this.endFace = endFace;
        this.cost = cost;
        this.path = path;
    }
}
