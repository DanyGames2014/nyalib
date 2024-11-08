package net.danygames2014.nyalib.util;

import net.minecraft.util.math.Vec3i;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.ArrayList;

public class AStar {
    Vec3i start;
    Vec3i end;
    ArrayList<AStarNode> open;
    ArrayList<AStarNode> closed;
    ArrayList<Vec3i> avalibleNodes;

    public AStar(Vec3i start, Vec3i end, Vec3i[] avalibleNodes) {
        this.open = new ArrayList<>(avalibleNodes.length);
        this.closed = new ArrayList<>(avalibleNodes.length);
        this.start = start;
        this.end = end;
        
        open.add(new AStarNode(start, null));
    }

    public Vec3i[] calculate() {
        boolean done = false;
        
        while (!done) {
            AStarNode current = getLowestFCost();
            open.remove(current);
            closed.add(current);

            if(current.position.equals(end)){
                done = true;
            }
            
            for (Direction direction : Direction.values()) {
                Vec3i side = new Vec3i(current.position.x + direction.getOffsetX(), current.position.y + direction.getOffsetY(), current.position.z + direction.getOffsetZ());
                if(!avalibleNodes.contains(side)){
                    continue;
                }

            }
        }
        
        return null;
    }
    
    AStarNode getLowestFCost(){
        double lowestCost = Double.MAX_VALUE;
        AStarNode bestNode = null;

        for (AStarNode node : open) {
            node.calculateCost(start, end);
            if (node.fCost < lowestCost) {
                lowestCost = node.fCost;
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
    double gCost;
    double hCost;
    double fCost;
    
    public AStarNode(Vec3i position, AStarNode parent){
        this.position = position;
        this.parent = parent;
        this.gCost = Double.MAX_VALUE;
        this.hCost = Double.MAX_VALUE;
        this.fCost = Double.MAX_VALUE;
    }
    
    public void calculateCost(Vec3i start, Vec3i end){
        this.gCost = position.distanceTo(start.x, start.y, start.z);
        this.hCost = position.distanceTo(end.x, end.y, end.z);
        this.fCost = this.gCost + this.hCost;
    }
}
