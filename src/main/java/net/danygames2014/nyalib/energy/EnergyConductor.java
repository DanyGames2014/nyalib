package net.danygames2014.nyalib.energy;

import net.danygames2014.nyalib.network.NetworkComponentEntry;
import net.minecraft.world.World;

public interface EnergyConductor {
    /**
     * @return The voltage at which the conductor breaks down 
     */
    int getBreakdownVoltage(World world, NetworkComponentEntry entry);

    /**
     * @return The energy at which the conductor breaks down
     */
    int getBreakdownPower(World world, NetworkComponentEntry entry);

    /**
     * Called when the breakdown voltage is exceeded
     *
     * @param voltage The voltage that is passing thru the conductor
     */
    void onBreakdownVoltage(World world, NetworkComponentEntry entry, int voltage);

    /**
     * Callen when the breakdown power is exceeded
     *
     * @param voltage The voltage that is passing thru the conductor
     * @param power   The power that is passing thru the conductor
     */
    void onBreakdownPower(World world, NetworkComponentEntry entry, int voltage, int power);
}
