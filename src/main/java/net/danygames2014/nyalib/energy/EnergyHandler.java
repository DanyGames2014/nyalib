package net.danygames2014.nyalib.energy;

import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public interface EnergyHandler extends EnergyCapable, EnergyStorage {

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
        
//        if(amperage <= 0.0){
//            return 0;
//        }

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

        // Return the unused amperage to the precision of 3 decimal places
        return Math.floor(((double) unusedPower / voltage) * 1000) / 1000;
    }


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
     * @param requestedAmperage The amperage requested from the machine
     * @return The actual amperage provided
     */
    default double extractEnergy(@Nullable Direction direction, double requestedAmperage) {
        // If there is no energy, skip the calculations
        if(getEnergyStored() <= 0){
            return 0;
        }
        
        // Calculate output power
        int outputVoltage = getOutputVoltage(direction);
        double outputAmperage = Math.min(requestedAmperage, getMaxOutputAmperage(direction));

        // Calculate the energy output
        int outputEnergy = (int) Math.floor(Math.min(outputVoltage * outputAmperage, getEnergyStored()) * 1000) / 1000;
        
        // Get the actual energy extracted
        int extractedEnergy = this.removeEnergy(outputEnergy);

        // Return the extracted amperage to the precision of 3 decimal places
        return Math.floor(((double) extractedEnergy / outputVoltage) * 1000) / 1000;
    }

    // Events

    /**
     * Triggered when a voltage higher than maximum is received on a given side
     * <p>
     * $ * @param direction The side on which overvoltage happened
     *
     * @param voltage The voltage
     */
    void onOvervoltage(@Nullable Direction direction, double voltage);
}
