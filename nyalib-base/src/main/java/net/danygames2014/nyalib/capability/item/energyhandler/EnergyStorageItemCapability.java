package net.danygames2014.nyalib.capability.item.energyhandler;

import net.danygames2014.nyalib.capability.item.ItemCapability;

public abstract class EnergyStorageItemCapability extends ItemCapability {
    // Storage
    public abstract int getEnergyStored();

    public abstract int getEnergyCapacity();

    public abstract int getRemainingCapacity();

    public abstract int setEnergy(int value);

    public abstract int changeEnergy(int difference);

    public abstract int addEnergy(int amount);

    public abstract int removeEnergy(int amount);

    // Input
    public abstract boolean canReceiveEnergy();

    public abstract int getMaxEnergyInput();

    public abstract int receiveEnergy(int energy);

    // Output
    public abstract boolean canExtractEnergy();

    public abstract int getMaxEnergyOutput();

    public abstract int extractEnergy(int requestedEnergy);
}
