package net.danygames2014.nyalibtest.energy;

import net.danygames2014.nyalib.network.Network;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class HighResistanceWireBlock extends WireBlock {
    public HighResistanceWireBlock(Identifier identifier) {
        super(identifier);
    }

    @Override
    public double getPathingCost(World world, int x, int y, int z, @Nullable Network network) {
        return 5.0D;
    }
}
