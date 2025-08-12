package net.danygames2014.nyalib.capability.block.energyhandler;

import net.danygames2014.nyalib.capability.block.BlockCapability;
import net.danygames2014.nyalib.energy.EnergyStorage;

public abstract class EnergyStorageBlockCapability extends BlockCapability implements EnergyStorage {
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
}
