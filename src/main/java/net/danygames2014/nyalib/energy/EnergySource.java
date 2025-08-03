package net.danygames2014.nyalib.energy;

import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

/**
 * An interface to be implemented on a block entity of an Energy Source block
 * <p>
 * <p>See {@link net.danygames2014.nyalib.energy.template.block.entity.EnergySourceBlockEntityTemplate} for a proper implementation
 * <p>    
 * <p>NOTE: While the {@link #extractEnergy(Direction, int)} (Direction, int, int)} method does contain basic logic for supplying energy, it does not contain the logic to respect the maximum energy input per tick
 */
@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted"})
public interface EnergySource extends EnergyHandler {
    // Output Parameters
    /**
     * Get the maximum output voltage that this machine can supply
     *
     * @param direction The direction to query from
     * @return The maximum output voltage this machine can supply
     */
    int getMaxOutputVoltage(@Nullable Direction direction);

    /**
     * Get the current output voltage of this machine
     *
     * @param direction The direction to query from
     * @return The current output voltage of this machine
     */
    int getOutputVoltage(@Nullable Direction direction);

    /**
     * Get the maximum amount of energy this machine can supply in one tick
     *
     * @param direction The direction to query from
     * @return The maximum amount of energy this machine can supply in one tick
     */
    int getMaxEnergyOutput(@Nullable Direction direction);

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
     * @param requestedEnergy   The energy requested from the machine
     * @return The actual amount energy extracted from the machine
     */
    default int extractEnergy(@Nullable Direction direction, int requestedEnergy) {
        // If energy cannot be extracted on this side, return zero
        if(!canExtractEnergy(direction)) {
            return 0;
        }
        
        // If there is no energy, skip the calculations
        if(getEnergyStored() <= 0){
            return 0;
        }

        // Return the extracted energy
        return this.removeEnergy(Math.min(requestedEnergy, getMaxEnergyOutput(direction)));
    }
}
