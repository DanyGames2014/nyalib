package net.danygames2014.nyalib.energy.simple;

import net.danygames2014.nyalib.energy.EnergyCapable;

public interface SimpleEnergyStorage extends EnergyCapable {
    /**
     * @return The amount of energy stored
     */
    int getEnergyStored();


    /**
     * @return The maximum amount of energy stored
     */
    int getMaxEnergyStored();
}
