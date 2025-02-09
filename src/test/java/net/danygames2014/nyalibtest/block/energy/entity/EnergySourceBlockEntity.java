package net.danygames2014.nyalibtest.block.energy.entity;

import net.danygames2014.nyalib.energy.template.block.entity.EnergySourceBlockEntityTemplate;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class EnergySourceBlockEntity extends EnergySourceBlockEntityTemplate {
    @Override
    public int getMaxOutputVoltage(@Nullable Direction direction) {
        return 12;
    }

    @Override
    public int getOutputVoltage(@Nullable Direction direction) {
        return 12;
    }

    @Override
    public int getMaxEnergyOutput(@Nullable Direction direction) {
        return 1;
    }

    @Override
    public boolean canExtractEnergy(@Nullable Direction direction) {
        return true;
    }

    @Override
    public boolean canConnectEnergy(Direction direction) {
        return true;
    }

    @Override
    public int getEnergyCapacity() {
        return 240;
    }
}
