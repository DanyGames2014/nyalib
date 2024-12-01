package net.danygames2014.nyalib.energy;

import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public interface EnergyConsumer extends EnergyHandler {
    // Input Parameters

    /**
     * Get the maximum input voltage that this machine can handle
     *
     * @param direction The direction to query from
     * @return The maximum input voltage this machine can handle
     */
    int getMaxInputVoltage(@Nullable Direction direction);

    /**
     * Get the maximum input amperage this machine can consume
     *
     * @param direction The direction to query from
     * @return The maximum input amperage this machine can consume
     */
    double getMaxInputAmperage(@Nullable Direction direction);

    // Receiving Energy

    /**
     * Query if this machine can accept energy
     *
     * @param direction The direction to query
     * @return If this machine can accept energy on the given fae
     */
    boolean canReceiveEnergy(@Nullable Direction direction);

    /**
     * Provide the machine energy
     *
     * @param direction The face to provide the energy on
     * @param voltage   The voltage level provided
     * @param power     The power provided
     * @return The power actually used by the machine
     */
    default int receiveEnergy(@Nullable Direction direction, int voltage, int power) {
        // If we cannot receive energy in this direction, dont care, return zero
        if (!canReceiveEnergy(direction)) {
            return 0;
        }

        if (power <= 0) {
            return 0;
        }

        if (getRemainingCapacity() <= 0) {
            return 0;
        }

        // Check if the voltage is higher than the maximum input voltage
        if (voltage > getMaxInputVoltage(direction)) {
            this.onOvervoltage(direction, voltage);
            return 0;
        }

        // Calculate the received and unused power
        int unusedPower = power - addEnergy(Math.min(power, (int) (voltage * getMaxInputAmperage(direction))));

        // Return the used amperage
        return power - unusedPower;
    }
}
