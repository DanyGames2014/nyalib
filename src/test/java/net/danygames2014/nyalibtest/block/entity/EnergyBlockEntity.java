package net.danygames2014.nyalibtest.block.entity;

import net.danygames2014.nyalib.energy.EnergyCapable;
import net.danygames2014.nyalib.energy.EnergyStorage;
import net.minecraft.block.entity.BlockEntity;
import net.modificationstation.stationapi.api.util.math.Direction;

public class EnergyBlockEntity extends BlockEntity implements EnergyCapable {
    @Override
    public boolean canConnect(Direction direction) {
        return false;
    }
}
