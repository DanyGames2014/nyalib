package net.danygames2014.nyalib.energy;

/**
 * A base EnergyStorage that can be implemented on blocks, items and entities
 * It can be shared between normal Energy and Simple Energy
 */
@SuppressWarnings("unused")
public interface EnergyStorage {
    /**
     * Get the energy stored in the internal buffer.
     *
     * @return The energy stored in Wt
     */
    // Energy Buffer
    int getEnergyStored();

    /**
     * Get the internal energy buffer capacity
     *
     * @return The internal energy buffer capacity in Wt
     */
    int getEnergyCapacity();

    /**
     * Get the remaining capacity of the internal energy buffer
     *
     * @return The remaining capacity of the buffer in Wt
     */
    default int getRemainingCapacity() {
        return getEnergyCapacity() - getEnergyStored();
    }

    /**
     * Set the internal buffer of the machine
     *
     * @param value The new desired value
     * @return The value the buffer was set to
     */
    int setEnergy(int value);

    /**
     * Change the energy buffer amount
     *
     * @param difference The amount to change by
     * @return The amount the buffer was changed by
     */
    default int changeEnergy(int difference) {
        // Store the current energy amount
        int currentEnergy = this.getEnergyStored();

        // Calculate the desired energy amount
        int desiredEnergy = currentEnergy + difference;

        // If the desired energy is higher than the capacity, try to set the energy to maximum
        if (desiredEnergy > this.getEnergyCapacity()) {
            return this.setEnergy(this.getEnergyCapacity());
        }

        // If the desired energy is lower than the capacity, try to set it to zero
        if (desiredEnergy < 0) {
            return this.setEnergy(0);
        }

        // Calculate by how much the energy has changed and return
        int newEnergy = this.setEnergy(desiredEnergy);
        return newEnergy - currentEnergy;
    }

    /**
     * Add energy to the internal buffer
     *
     * @param amount The amount to add to the internal buffer
     * @return The amount of energy added to the buffer
     */
    default int addEnergy(int amount) {
        return changeEnergy(amount);
    }

    /**
     * Remove energy from the internal buffer
     *
     * @param amount The amount to remove from the internal buffer
     * @return The amount of energy removed from the buffer
     */
    default int removeEnergy(int amount) {
        return Math.abs(changeEnergy(-amount));
    }
}
