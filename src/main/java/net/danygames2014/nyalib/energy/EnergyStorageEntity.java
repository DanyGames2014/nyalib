package net.danygames2014.nyalib.energy;

/**
 * A base interface to implement on an Entity which can store energy
 */
public interface EnergyStorageEntity {
    /**
     * Get the energy stored in the internal buffer.
     *
     * @return The amount of energy stored amount
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
     * Set the internal buffer of the entity
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

    // Input

    /**
     * Query if this entity can accept energy
     *
     * @return If this entity can accept energy
     */
    boolean canReceiveEnergy();

    /**
     * Get the maximum input power this entity can accept in one tick
     *
     * @return The maximum input power this entity can accept in one tick
     */
    int getMaxEnergyInput();

    /**
     * Provide energy to the entity
     *
     * @param energy The amount of energy provided
     * @return The energy actually accepted by the entity
     */
    default int receiveEnergy(int energy) {
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

    /**
     * Query if you can extract energy from this entity
     *
     * @return If this entity allows extraction
     */
    boolean canExtractEnergy();

    /**
     * Get the maximum amount of energy this entity can supply in one tick
     *
     * @return The maximum amount of energy this entity can supply in one tick
     */
    int getMaxEnergyOutput();

    /**
     * Extract energy from the entity
     *
     * @param requestedEnergy The energy requested from the entity
     * @return The actual amount energy extracted from the entity
     */
    default int extractEnergy(int requestedEnergy) {
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
