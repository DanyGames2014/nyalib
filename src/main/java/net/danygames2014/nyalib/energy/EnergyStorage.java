package net.danygames2014.nyalib.energy;

public interface EnergyStorage extends EnergyCapable {
    /**
     * @return The amount of energy stored
     */
    int getEnergyStored();


    /**
     * @return The maximum amount of energy stored
     */
    int getMaxEnergyStored();
}
