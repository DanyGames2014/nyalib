package net.danygames2014.nyalib.block.voxelshape;

import net.modificationstation.stationapi.api.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Line {
    private final Vec3d start;
    private final Vec3d end;

    public Line(Vec3d start, Vec3d end) {
        this.start = start;
        this.end = end;
    }

    private static Vec3d movePointAwayFromCenter(Vec3d point, Vec3d center, double distance) {
        Vec3d direction = point.subtract(center).normalize();
        Vec3d displacement = direction.multiply(distance);
        return point.add(displacement);
    }

    public Vec3d getMidpoint() {
        return new Vec3d(
                (start.x + end.x) / 2,
                (start.y + end.y) / 2,
                (start.z + end.z) / 2
        );
    }

    public Line expand(double d, Vec3d center) {
        return new Line(movePointAwayFromCenter(start, center, d), movePointAwayFromCenter(end, center, d));
    }

    public boolean coincidesWith(Line other) {
        return (start.equals(other.start) && end.equals(other.end)) ||
                (start.equals(other.end) && start.equals(other.start));
    }

    public boolean partiallyCoincidesWith(Line other) {
        return containsPoint(other.start) && containsPoint(other.end) ||
                other.containsPoint(start) && other.containsPoint(end);
    }

    private boolean containsPoint(Vec3d point) {
        double minX = Math.min(start.x, end.x);
        double maxX = Math.max(start.x, end.x);
        double minY = Math.min(start.y, end.y);
        double maxY = Math.max(start.y, end.y);
        double minZ = Math.min(start.z, end.z);
        double maxZ = Math.max(start.z, end.z);

        return (point.x >= minX && point.x <= maxX) &&
                (point.y >= minY && point.y <= maxY) &&
                (point.z >= minZ && point.z <= maxZ);
    }

    public Line[] getNonOverlappingParts(Line other) {
        List<Line> nonOverlappingParts = new ArrayList<>();

        if (this.containsPoint(other.start) && this.containsPoint(other.end)) {
            // Other line is completely inside this line, split this line into two parts
            if (!start.equals(other.start)) {
                nonOverlappingParts.add(new Line(start, other.start));
            }
            if (!end.equals(other.end)) {
                nonOverlappingParts.add(new Line(other.end, end));
            }
        } else if (this.containsPoint(other.start)) {
            // Other line overlaps at the start of this line
            nonOverlappingParts.add(new Line(start, other.start));
            nonOverlappingParts.add(new Line(other.start, end));
        } else if (this.containsPoint(other.end)) {
            // Other line overlaps at the end of this line
            nonOverlappingParts.add(new Line(start, other.end));
            nonOverlappingParts.add(new Line(other.end, end));
        } else {
            // This line is completely inside other line
            if (!other.start.equals(start)) {
                nonOverlappingParts.add(new Line(other.start, start));
            }
            if (!other.end.equals(end)) {
                nonOverlappingParts.add(new Line(end, other.end));
            }
        }

        return nonOverlappingParts.toArray(new Line[0]);
    }

    public Vec3d getStart() {
        return start;
    }

    public Vec3d getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Line) obj;
        return Objects.equals(this.start, that.start) &&
                Objects.equals(this.end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return "Line[" +
                "start=" + start + ", " +
                "end=" + end + ']';
    }

}