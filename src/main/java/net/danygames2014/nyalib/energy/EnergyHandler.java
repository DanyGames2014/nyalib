package net.danygames2014.nyalib.energy;

import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

/**
 * A common interface for blocks capable of storing energy and connecting with other energy devices
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface EnergyHandler extends EnergyCapable, EnergyStorage {
}
