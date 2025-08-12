package net.danygames2014.nyalib.capability.block.energyhandler;

import net.danygames2014.nyalib.capability.block.BlockCapability;
import net.danygames2014.nyalib.energy.EnergyConsumer;
import net.danygames2014.nyalib.energy.EnergySource;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public abstract class EnergyHandlerBlockCapability extends BlockCapability implements EnergyConsumer, EnergySource {
    // EnergyCapable
    @Override
    public abstract boolean canConnectEnergy(Direction direction);

    // EnergyStorage
    @Override
    public abstract int getEnergyStored();

    @Override
    public abstract int getEnergyCapacity();

    @Override
    public abstract int getRemainingCapacity();

    @Override
    public abstract int setEnergy(int value);

    @Override
    public abstract int changeEnergy(int difference);

    @Override
    public abstract int addEnergy(int amount);

    @Override
    public abstract int removeEnergy(int amount);
    
    // EnergyConsumer
    @Override
    public abstract int getMaxInputVoltage(@Nullable Direction direction);

    @Override
    public abstract int getMaxEnergyInput(@Nullable Direction direction);

    @Override
    public abstract boolean canReceiveEnergy(@Nullable Direction direction);

    @Override
    public abstract int receiveEnergy(@Nullable Direction direction, int voltage, int energy);

    @Override
    public abstract void onOvervoltage(@Nullable Direction direction, double voltage);
    
    // EnergySource
    @Override
    public abstract int getMaxOutputVoltage(@Nullable Direction direction);

    @Override
    public abstract int getOutputVoltage(@Nullable Direction direction);

    @Override
    public abstract int getMaxEnergyOutput(@Nullable Direction direction);

    @Override
    public abstract boolean canExtractEnergy(@Nullable Direction direction);

    @Override
    public abstract int extractEnergy(@Nullable Direction direction, int requestedEnergy);
}
