package net.danygames2014.nyalib.energy;

import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public interface EnergyDevice extends EnergyCapable{

    /**
     * @param direction Direction to check
     * @return <code>true</code> if the device supports energy extraction from the given direction
     */
    boolean canExtractEnergy(@Nullable Direction direction);

    /**
     * Extract energy from the device
     *
     * @param amount    Amount of energy to be extracted
     * @param simulate  If <code>true</code> the action will only be simulated
     * @param direction {@link Direction} from which the energy is extracted from
     * @return Amount of energy that was (or would have been, if simulated) extracted
     */
    int extractEnergy(int amount, boolean simulate, @Nullable Direction direction);


    /**
     * @param direction Direction to check
     * @return <code>true</code> if the device supports energy insertion from the given direction
     */
    boolean canInsertEnergy(@Nullable Direction direction);

    /**
     * Provide energy to the device
     *
     * @param amount    Amount of energy provided to the device
     * @param simulate  If <code>true</code> the action will only be simulated
     * @param direction {@link Direction} from which the energy is received
     * @return Amount of energy that was (or would have been, if simulated) received
     */
    int insertEnergy(int amount, boolean simulate, @Nullable Direction direction);
}
