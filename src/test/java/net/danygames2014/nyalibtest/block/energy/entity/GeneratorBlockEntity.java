package net.danygames2014.nyalibtest.block.energy.entity;

import net.danygames2014.nyalib.energy.EnergyHandler;
import net.minecraft.block.entity.BlockEntity;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class GeneratorBlockEntity extends BlockEntity implements EnergyHandler {

    public int energy = 0;

    @Override
    public int getMaxInputVoltage(@Nullable Direction direction) {
        return 0;
    }

    @Override
    public double getMaxInputAmperage(@Nullable Direction direction) {
        return 0;
    }

    @Override
    public int getOutputVoltage(@Nullable Direction direction) {
        return 220;
    }

    @Override
    public int getMaxOutputVoltage(@Nullable Direction direction) {
        return 220;
    }

    @Override
    public double getMaxOutputAmperage(@Nullable Direction direction) {
        return 2;
    }

    @Override
    public boolean canReceiveEnergy(@Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canExtractEnergy(@Nullable Direction direction) {
        return true;
    }

    @Override
    public void onOvervoltage(@Nullable Direction direction, double voltage) {

    }

    @Override
    public boolean canConnectEnergy(Direction direction) {
        return true;
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public int getEnergyCapacity() {
        return 1000;
    }

    @Override
    public int setEnergy(int value) {
        this.energy = value;

        if (energy > getEnergyCapacity()) {
            energy = getEnergyCapacity();
        }

        return energy;
    }
}
