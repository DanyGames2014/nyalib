package net.danygames2014.nyalib.capability.block.energyhandler;

import net.danygames2014.nyalib.energy.EnergyStorage;

public class EnergyStorageInterfaceBlockCapability extends EnergyStorageBlockCapability {
    private final EnergyStorage energyStorage;
    
    public EnergyStorageInterfaceBlockCapability(EnergyStorage energyStorage) {
        this.energyStorage = energyStorage;
    }
    
    @Override
    public int getEnergyStored() {
        return energyStorage.getEnergyStored();
    }

    @Override
    public int getEnergyCapacity() {
        return energyStorage.getEnergyCapacity();
    }

    @Override
    public int getRemainingCapacity() {
        return energyStorage.getRemainingCapacity();
    }

    @Override
    public int setEnergy(int value) {
        return energyStorage.setEnergy(value);
    }

    @Override
    public int changeEnergy(int difference) {
        return energyStorage.changeEnergy(difference);
    }

    @Override
    public int addEnergy(int amount) {
        return energyStorage.addEnergy(amount);
    }

    @Override
    public int removeEnergy(int amount) {
        return energyStorage.removeEnergy(amount);
    }
}
