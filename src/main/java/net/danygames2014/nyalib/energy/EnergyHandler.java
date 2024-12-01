package net.danygames2014.nyalib.energy;

import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface EnergyHandler extends EnergyCapable, EnergyStorage {
    // Events

    /**
     * Triggered when a voltage higher than maximum is received on a given side
     * <p>
     * @param direction The side on which overvoltage happened
     *
     * @param voltage The voltage
     */
    void onOvervoltage(@Nullable Direction direction, double voltage);
}
