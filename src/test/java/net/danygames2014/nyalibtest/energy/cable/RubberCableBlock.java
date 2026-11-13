package net.danygames2014.nyalibtest.energy.cable;

import net.danygames2014.nyalib.network.NetworkComponentEntry;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

public class RubberCableBlock extends CableBlockTemplate {
    public RubberCableBlock(Identifier identifier, Material material) {
        super(identifier, material);
    }

    @Override
    public int getBreakdownVoltage(World world, NetworkComponentEntry networkComponentEntry) {
        return 300;
    }

    @Override
    public int getBreakdownPower(World world, NetworkComponentEntry networkComponentEntry) {
        return 128;
    }
}
