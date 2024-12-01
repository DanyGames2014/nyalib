package net.danygames2014.nyalib.energy;

import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public interface EnergySource extends EnergyHandler {
    // Output Parameters
    /**
     * Get the current output voltage of this machine
     *
     * @param direction The direction to query from
     * @return The current output voltage of this machine
     */
    int getOutputVoltage(@Nullable Direction direction);

    /**
     * Get the maximum output voltage that this machine can supply
     *
     * @param direction The direction to query from
     * @return The maximum output voltage this machine can supply
     */
    int getMaxOutputVoltage(@Nullable Direction direction);

    /**
     * Get the maximum output amperage this machine can supply
     *
     * @param direction The direction to query from
     * @return The maximum output current this machine can supply
     */
    double getMaxOutputAmperage(@Nullable Direction direction);

    // Extracting Energy
    /**
     * Query if you can extract energy from this machine on the given side
     *
     * @param direction The direction to query
     * @return If this machine allows extraction on the given side
     */
    boolean canExtractEnergy(@Nullable Direction direction);

    /**
     * Extract energy from the machine on the given side
     * The voltage is the output of {@link #getOutputVoltage(Direction)}
     *
     * @param direction         The direction to extract from
     * @param requestedPower The amperage requested from the machine
     * @return The actual amperage provided
     */
    default int extractEnergy(@Nullable Direction direction, int requestedPower) {
        // If there is no energy, skip the calculations
        if(getEnergyStored() <= 0){
            return 0;
        }

        // Return the extracted energy
        return this.removeEnergy(Math.min(requestedPower, (int)(getMaxOutputAmperage(direction) * getOutputVoltage(direction))));
    }
}
