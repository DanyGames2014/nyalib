package net.danygames2014.nyalib.capability.entity.energyhandler;

import net.danygames2014.nyalib.capability.entity.EntityCapability;

public abstract class EnergyStorageEntityCapability extends EntityCapability {
    // Storage
    public abstract int getEnergyStored();

    public abstract int getEnergyCapacity();

    public int getRemainingCapacity() {
        return getEnergyCapacity() - getEnergyStored();
    }

    public abstract int setEnergy(int value);

    public int changeEnergy(int difference) {
        // Store the current energy amount
        int prevEnergy = this.getEnergyStored();

        // Calculate the desired energy amount
        int desiredEnergy = prevEnergy + difference;

        // If the desired energy is higher than the capacity, try to set the energy to maximum
        if (desiredEnergy > this.getEnergyCapacity()) {
            this.setEnergy(this.getEnergyCapacity());
        } else if (desiredEnergy < 0) {
            this.setEnergy(0);
        } else {
            this.setEnergy(desiredEnergy);
        }


        // Calculate by how much the energy has changed and return
        int newEnergy = this.getEnergyStored();
        return newEnergy - prevEnergy;
    }

    public int addEnergy(int amount) {
        return changeEnergy(amount);
    }

    public int removeEnergy(int amount) {
        return Math.abs(changeEnergy(-amount));
    }

    // Input
    public abstract boolean canReceiveEnergy();

    public abstract int getMaxEnergyInput();

    public int receiveEnergy(int energy) {
        // If the received power is zero or negative, return zero
        if (energy <= 0) {
            return 0;
        }

        // If we wouldn't be able to store any power anyway, dont bother calculating and return zero
        if (getRemainingCapacity() <= 0) {
            return 0;
        }

        // Return the used power
        return addEnergy(Math.min(energy, getMaxEnergyInput()));
    }

    // Output
    public abstract boolean canExtractEnergy();

    public abstract int getMaxEnergyOutput();

    public int extractEnergy(int requestedEnergy) {
        // If energy cannot be extracted on this side, return zero
        if (!canExtractEnergy()) {
            return 0;
        }

        // If there is no energy, skip the calculations
        if (getEnergyStored() <= 0) {
            return 0;
        }

        // Return the extracted energy
        return this.removeEnergy(Math.min(requestedEnergy, getMaxEnergyOutput()));
    }
}
