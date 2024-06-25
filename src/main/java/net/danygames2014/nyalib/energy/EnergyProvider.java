package net.danygames2014.nyalib.energy;

import net.modificationstation.stationapi.api.util.math.Direction;

public interface EnergyProvider extends EnergyCapable {
    /**
     * Extract energy from the provider
     *
     * @param amount    Amount of energy to be extracted
     * @param simulate  If <code>true</code> the action will only be simulated
     * @param direction {@link Direction} from which the energy is extracted from
     * @return Amount of energy that was (or would have been, if simulated) extracted
     */
    int extractEnergy(int amount, boolean simulate, Direction direction);
}
