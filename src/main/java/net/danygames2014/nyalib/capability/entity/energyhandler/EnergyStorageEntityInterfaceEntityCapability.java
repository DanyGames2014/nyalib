package net.danygames2014.nyalib.capability.entity.energyhandler;

import net.danygames2014.nyalib.energy.EnergyStorageEntity;

public class EnergyStorageEntityInterfaceEntityCapability extends EnergyStorageEntityCapability {
    private final EnergyStorageEntity energyStorageEntity;

    public EnergyStorageEntityInterfaceEntityCapability(EnergyStorageEntity energyStorageEntity) {
        this.energyStorageEntity = energyStorageEntity;
    }

    // Storage
    @Override
    public int getEnergyStored() {
        return energyStorageEntity.getEnergyStored();
    }

    @Override
    public int getEnergyCapacity() {
        return energyStorageEntity.getEnergyCapacity();
    }

    @Override
    public int getRemainingCapacity() {
        return energyStorageEntity.getRemainingCapacity();
    }

    @Override
    public int setEnergy(int value) {
        return energyStorageEntity.setEnergy(value);
    }

    @Override
    public int changeEnergy(int difference) {
        return energyStorageEntity.changeEnergy(difference);
    }

    @Override
    public int addEnergy(int amount) {
        return energyStorageEntity.addEnergy(amount);
    }

    @Override
    public int removeEnergy(int amount) {
        return energyStorageEntity.removeEnergy(amount);
    }

    // Input
    @Override
    public boolean canReceiveEnergy() {
        return energyStorageEntity.canReceiveEnergy();
    }

    @Override
    public int getMaxEnergyInput() {
        return energyStorageEntity.getMaxEnergyInput();
    }

    @Override
    public int receiveEnergy(int energy) {
        return energyStorageEntity.receiveEnergy(energy);
    }

    // Output
    @Override
    public boolean canExtractEnergy() {
        return energyStorageEntity.canExtractEnergy();
    }

    @Override
    public int getMaxEnergyOutput() {
        return energyStorageEntity.getMaxEnergyOutput();
    }

    @Override
    public int extractEnergy(int requestedEnergy) {
        return energyStorageEntity.extractEnergy(requestedEnergy);
    }
}
