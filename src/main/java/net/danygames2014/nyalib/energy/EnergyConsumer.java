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
     * Get the maximum input power this machine can consume in one tick
     *
     * @param direction The direction to query from
     * @return The maximum input power this machine can consume in one tick
     */
    int getMaxEnergyInput(@Nullable Direction direction);

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
     * @param energy    The energy provided
     * @return The energy actually used by the machine
     */
    default int receiveEnergy(@Nullable Direction direction, int voltage, int energy) {
        // If we cannot receive energy in this direction, dont care, return zero
        if (!canReceiveEnergy(direction)) {
            return 0;
        }

        // If the received power is zero or negative, return zero
        if (energy <= 0) {
            return 0;
        }

        // If we wouldn't be able to store any power anyway, dont bother calculating and return zero
        if (getRemainingCapacity() <= 0) {
            return 0;
        }

        // Check if the voltage is higher than the maximum input voltage
        if (voltage > getMaxInputVoltage(direction)) {
            // If the voltage is higher, trigger an overvoltage event and return zero
            this.onOvervoltage(direction, voltage);
            return 0;
        }

        // Calculate the received and unused power
        int unusedPower = energy - addEnergy(Math.min(energy, voltage));

        // Return the used power
        return energy - unusedPower;
    }
}
