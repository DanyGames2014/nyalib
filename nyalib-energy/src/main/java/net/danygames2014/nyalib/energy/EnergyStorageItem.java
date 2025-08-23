package net.danygames2014.nyalib.energy;

import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.util.math.Direction;

/**
 * A base interface to implement on an Item which can store energy
 */
public interface EnergyStorageItem {
    /**
     * Get the energy stored in the internal buffer.
     *
     * @return The amount of energy stored amount
     */
    // Energy Buffer
    int getEnergyStored(ItemStack stack);

    /**
     * Get the internal energy buffer capacity
     *
     * @return The internal energy buffer capacity
     */
    int getEnergyCapacity(ItemStack stack);

    /**
     * Get the remaining capacity of the internal energy buffer
     *
     * @return The remaining capacity of the buffer
     */
    default int getRemainingCapacity(ItemStack stack) {
        return getEnergyCapacity(stack) - getEnergyStored(stack);
    }

    /**
     * Set the internal buffer of the item
     *
     * @param value The new desired value
     * @return The value the buffer was set to
     */
    int setEnergy(ItemStack stack, int value);

    /**
     * Change the energy buffer amount
     *
     * @param difference The amount to change by
     * @return The amount the buffer was changed by
     */
    default int changeEnergy(ItemStack stack, int difference) {
        // Store the current energy amount
        int prevEnergy = this.getEnergyStored(stack);

        // Calculate the desired energy amount
        int desiredEnergy = prevEnergy + difference;

        // If the desired energy is higher than the capacity, try to set the energy to maximum
        if (desiredEnergy > this.getEnergyCapacity(stack)) {
            this.setEnergy(stack, this.getEnergyCapacity(stack));
        } else if (desiredEnergy < 0) {
            this.setEnergy(stack, 0);
        } else {
            this.setEnergy(stack, desiredEnergy);
        }


        // Calculate by how much the energy has changed and return
        int newEnergy = this.getEnergyStored(stack);
        return newEnergy - prevEnergy;
    }

    /**
     * Add energy to the internal buffer
     *
     * @param amount The amount to add to the internal buffer
     * @return The amount of energy added to the buffer
     */
    default int addEnergy(ItemStack stack, int amount) {
        return changeEnergy(stack, amount);
    }

    /**
     * Remove energy from the internal buffer
     *
     * @param amount The amount to remove from the internal buffer
     * @return The amount of energy removed from the buffer
     */
    default int removeEnergy(ItemStack stack, int amount) {
        return Math.abs(changeEnergy(stack, -amount));
    }

    // Input
    /**
     * Query if this item can accept energy
     *
     * @return If this item can accept energy
     */
    boolean canReceiveEnergy(ItemStack stack);

    /**
     * Get the maximum input power this item can accept in one tick
     *
     * @return The maximum input power this item can accept in one tick
     */
    int getMaxEnergyInput(ItemStack stack);

    /**
     * Provide energy to the item
     *
     * @param energy    The amount of energy provided
     * @return The energy actually accepted by the item
     */
    default int receiveEnergy(ItemStack stack, int energy) {
        // If the received power is zero or negative, return zero
        if (energy <= 0) {
            return 0;
        }

        // If we wouldn't be able to store any power anyway, dont bother calculating and return zero
        if (getRemainingCapacity(stack) <= 0) {
            return 0;
        }

        // Return the used power
        return addEnergy(stack, Math.min(energy, getMaxEnergyInput(stack)));
    }

    // Output
    /**
     * Query if you can extract energy from this item
     *
     * @return If this item allows extraction
     */
    boolean canExtractEnergy(ItemStack stack);

    /**
     * Get the maximum amount of energy this item can supply in one tick
     *
     * @return The maximum amount of energy this item can supply in one tick
     */
    int getMaxEnergyOutput(ItemStack stack);

    /**
     * Extract energy from the item
     *
     * @param requestedEnergy   The energy requested from the item
     * @return The actual amount energy extracted from the item
     */
    default int extractEnergy(ItemStack stack, int requestedEnergy) {
        // If energy cannot be extracted on this side, return zero
        if (!canExtractEnergy(stack)) {
            return 0;
        }

        // If there is no energy, skip the calculations
        if (getEnergyStored(stack) <= 0) {
            return 0;
        }

        // Return the extracted energy
        return this.removeEnergy(stack, Math.min(requestedEnergy, getMaxEnergyOutput(stack)));
    }
}
