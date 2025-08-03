package net.danygames2014.nyalib.energy;

/**
 * A base interface to implement on a Block Entity which can store energy
 */
@SuppressWarnings({"unused", "ManualMinMaxCalculation", "UnusedReturnValue"})
public interface EnergyStorage {
    /**
     * Get the energy stored in the internal buffer.
     *
     * @return The amount of energy stored
     */
    // Energy Buffer
    int getEnergyStored();

    /**
     * Get the internal energy buffer capacity
     *
     * @return The internal energy buffer capacity
     */
    int getEnergyCapacity();

    /**
     * Get the remaining capacity of the internal energy buffer
     *
     * @return The remaining capacity of the buffer
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
