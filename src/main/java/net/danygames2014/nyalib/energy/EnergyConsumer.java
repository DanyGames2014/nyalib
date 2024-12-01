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
     * @param amperage  The amperage provided
     * @return The amperage actually used by the machine
     */
    default double receiveEnergy(@Nullable Direction direction, int voltage, double amperage) {
        // If we cannot receive energy in this direction, dont care, return zero
        if (!canReceiveEnergy(direction)) {
            return 0;
        }

        if (amperage <= 0.0) {
            return 0;
        }

        // The lowest receive current we will care about is 10mA
        if (getRemainingCapacity() < (voltage * 0.01)) {
            return 0;
        }

        // Check if the voltage is higher than the maximum input voltage
        if (voltage > getMaxInputVoltage(direction)) {
            this.onOvervoltage(direction, voltage);
            return 0;
        }

        // Calculate the received and unused power
        int receivedPower = (int) (voltage * Math.min(amperage, getMaxInputAmperage(direction)));
        int unusedPower = receivedPower - addEnergy(receivedPower);

        // Return the used amperage to the precision of 3 decimal places
        return (double) (receivedPower - unusedPower) / voltage;
    }
}
