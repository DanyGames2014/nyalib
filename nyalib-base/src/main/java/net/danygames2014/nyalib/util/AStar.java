package net.danygames2014.nyalib.util;

import net.danygames2014.nyalib.NyaLib;
import net.minecraft.util.math.Vec3i;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AStar {
    Vec3i start;
    Vec3i end;
    HashMap<Vec3i, AStarNode> open;
    HashMap<Vec3i, AStarNode> closed;
    HashMap<Vec3i, AStarNode> validNodes;

    public AStar(Vec3i start, Vec3i end, Vec3i[] avalibleNodes) {
        this.open = new HashMap<>();
        this.closed = new HashMap<>();
        this.validNodes = new HashMap<>();
        this.start = start;
        this.end = end;

        for (Vec3i node : avalibleNodes) {
            validNodes.put(node, new AStarNode(node, null));
        }

        open.put(start, new AStarNode(start, null));
    }

    public Vec3i[] calculate() {
        AStarNode endNode = null;

        while (endNode == null) {
            AStarNode current = getLowestFCost();

            if (current == null) {
                NyaLib.LOGGER.debug("Current node is null. No path found.");
                return null;
            }

            open.remove(current.position);
            closed.put(current.position, current);

            if (current.position.equals(end)) {
                endNode = current;
            }

            for (Direction direction : Direction.values()) {
                Vec3i neighbor = new Vec3i(current.position.x + direction.getOffsetX(), current.position.y + direction.getOffsetY(), current.position.z + direction.getOffsetZ());
                if (!validNodes.containsKey(neighbor) || closed.containsKey(neighbor)) {
                    continue;
                }

                AStarNode neighborNode = validNodes.get(neighbor);
                double neighborCost = neighborNode.fCost;
                double newCost = neighborNode.calculateCost(start, end);

                if (newCost < neighborCost || !open.containsKey(neighbor)) {
                    neighborNode.fCost = newCost;
                    neighborNode.parent = current;
                    if (!open.containsKey(neighbor)) {
                        open.put(neighbor, neighborNode);
                    }
                }

            }
        }

        AStarNode traversedNode = endNode;
        ArrayList<Vec3i> path = new ArrayList<>();

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

        if (open.isEmpty()) {
            NyaLib.LOGGER.debug("Lowest F cost requested for empty path.");
            return null;
        }

        for (Map.Entry<Vec3i, AStarNode> nodeEntry : open.entrySet()) {
            AStarNode node = nodeEntry.getValue();
            double cost = node.calculateCost(start, end);
            if (cost < lowestCost) {
                lowestCost = node.fCost = cost;
                bestNode = node;
            }
        }

        return bestNode;
    }
}

// gCost = Distance from starting Node
// hCost = Distance from end Node
// fCost = gCost + hCost
class AStarNode {
    Vec3i position;
    AStarNode parent;
    double fCost;

    public AStarNode(Vec3i position, AStarNode parent) {
        this.position = position;
        this.parent = parent;
        this.fCost = Double.MAX_VALUE;
    }

    public double calculateCost(Vec3i start, Vec3i end) {
        double gCostT = position.distanceTo(start.x, start.y, start.z);
        double hCostT = position.distanceTo(end.x, end.y, end.z);
        return gCostT + hCostT;
    }
}
