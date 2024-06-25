package net.danygames2014.nyalib.energy;

import net.modificationstation.stationapi.api.util.math.Direction;

/**
 * An Energy Capable block which can receive energy
 */
public interface EnergyReceiver extends EnergyCapable {
    /**
     * Provide energy to the receiver
     *
     * @param amount    Amount of energy provided to the receiver
     * @param simulate  If <code>true</code> the action will only be simulated
     * @param direction {@link Direction} from which the energy is received
     * @return Amount of energy that was (or would have been, if simulated) received
     */
    int insertEnergy(int amount, boolean simulate, Direction direction);
}
