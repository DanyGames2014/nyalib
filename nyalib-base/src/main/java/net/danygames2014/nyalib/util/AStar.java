package net.danygames2014.nyalib.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.danygames2014.nyalib.NyaLib;
import net.minecraft.util.math.Vec3i;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.ToDoubleFunction;

public class AStar {
    final Vec3i start;
    final Vec3i end;
    final Object2ObjectOpenHashMap<Vec3i, AStarNode> open;
    final Object2ObjectOpenHashMap<Vec3i, AStarNode> closed;
    final Object2ObjectOpenHashMap<Vec3i, AStarNode> validNodes;

    ToDoubleFunction<Vec3i> pathCostFunction = value -> 1.0D;

    public AStar(Vec3i start, Vec3i end, Vec3i[] avalibleNodes) {
        this.open = new Object2ObjectOpenHashMap<>();
        this.closed = new Object2ObjectOpenHashMap<>();
        this.validNodes = new Object2ObjectOpenHashMap<>();
        this.start = start;
        this.end = end;

        for (Vec3i node : avalibleNodes) {
            validNodes.put(node, new AStarNode(node, null));
        }

        // Initialize the starting node
        AStarNode startNode = new AStarNode(start, null);
        startNode.gCost = 0;
        startNode.fCost = manhattanDistance(start, end);
        open.put(start, startNode);
    }

    public AStar(Vec3i start, Vec3i end, Vec3i[] avalibleNodes, ToDoubleFunction<Vec3i> pathCostFunction) {
        this(start, end, avalibleNodes);
        this.pathCostFunction = pathCostFunction;
    }

    public Vec3i[] calculate() {
        AStarNode endNode = null;

        while (!open.isEmpty()) {
            AStarNode current = getLowestFCost();

            if (current == null) {
                NyaLib.LOGGER.debug("Current node is null. No path found.");
                return null;
            }

            open.remove(current.position);
            closed.put(current.position, current);

            if (current.position.equals(end)) {
                endNode = current;
                break;
            }

            for (Direction direction : Direction.values()) {
                Vec3i neighbor = new Vec3i(current.position.x + direction.getOffsetX(), current.position.y + direction.getOffsetY(), current.position.z + direction.getOffsetZ());

                if (!validNodes.containsKey(neighbor)) {
                    continue;
                }

                // We check what the cost would be to this neighbor
                double candidateGCost = current.gCost + pathCostFunction.applyAsDouble(neighbor);
                AStarNode neighborNode = validNodes.get(neighbor);

                if (closed.containsKey(neighbor) && candidateGCost >= neighborNode.gCost) {
                    continue;
                }

                // Check if its a shorter path
                if (candidateGCost < neighborNode.gCost) {
                    neighborNode.parent = current;
                    neighborNode.gCost = candidateGCost;
                    neighborNode.fCost = candidateGCost + manhattanDistance(neighbor, end);

                    closed.remove(neighbor);
                    
                    if (!open.containsKey(neighbor)) {
                        open.put(neighbor, neighborNode);
                    }
                }
            }
        }

        if (endNode == null) {
            return null;
        }

        ArrayList<Vec3i> path = new ArrayList<>();
        AStarNode traversedNode = endNode;

        while (traversedNode != null) {
            path.add(traversedNode.position);
            traversedNode = traversedNode.parent;
        }

        Collections.reverse(path);
        return path.toArray(new Vec3i[0]);
    }

    AStarNode getLowestFCost() {
        double lowestCost = Double.MAX_VALUE;
        AStarNode bestNode = null;

        for (AStarNode node : open.values()) {
            if (node.fCost < lowestCost) {
                lowestCost = node.fCost;
                bestNode = node;
            }
        }

        return bestNode;
    }

    static double manhattanDistance(Vec3i a, Vec3i b) {
        return (Math.abs(a.x - b.x) + Math.abs(a.y - b.y) + Math.abs(a.z - b.z)) * 0.1D;
    }
}

// gCost = Distance from starting Node
// hCost = Distance from end Node
// fCost = gCost + hCost
class AStarNode {
    final Vec3i position;
    AStarNode parent;
    double gCost;
    double fCost;

    public AStarNode(Vec3i position, AStarNode parent) {
        this.position = position;
        this.parent = parent;
        this.gCost = Double.MAX_VALUE;
        this.fCost = Double.MAX_VALUE;
    }
}