package net.danygames2014.nyalib.energy;

import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public interface EnergyHandler extends EnergyCapable {

    // Input Parameters

    /**
     * Get the maximum input voltage that this machine can handle
     *
     * @param direction The direction to query from
     * @return The maximum input voltage this machine can handle
     */
    int getMaxInputVoltage(@NotNull Direction direction);

    /**
     * Get the maximum input amperage this machine can consume
     *
     * @param direction The direction to query from
     * @return The maximum input amperage this machine can consume
     */
    double getMaxInputAmperage(@NotNull Direction direction);


    // Output Parameters

    /**
     * Get the current output voltage of this machine
     *
     * @param direction The direction to query from
     * @return The current output voltage of this machine
     */
    int getOutputVoltage(@NotNull Direction direction);

    /**
     * Get the maximum output voltage that this machine can supply
     *
     * @param direction The direction to query from
     * @return The maximum output voltage this machine can supply
     */
    int getMaxOutputVoltage(@NotNull Direction direction);

    /**
     * Get the maximum output amperage this machine can supply
     *
     * @param direction The direction to query from
     * @return The maximum output current this machine can supply
     */
    double getMaxOutputAmperage(@NotNull Direction direction);


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
    double receiveEnergy(@Nullable Direction direction, int voltage, double amperage);


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
     *
     * @param direction         The direction to extract from
     * @param requestedAmperage The amperage requested from the machine
     * @return The actual amperage provided
     */
    double extractEnergy(@Nullable Direction direction, double requestedAmperage);


    /**
     * Triggered when a voltage higher than maximum is received on a given side
     *
     * @param direction The side on which overvoltage happened
     * @param voltage   The voltage
     */
    // Events
    void onOvervoltage(@NotNull Direction direction, double voltage);
}
