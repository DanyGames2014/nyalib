package net.danygames2014.nyalib;

import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import org.apache.logging.log4j.Logger;

public class NyaLibFluid {
    @Entrypoint.Logger
    public static Logger LOGGER;

    @Entrypoint.Namespace
    public static Namespace NAMESPACE;
    
    // TODO: SimpleTank
    // TODO: ItemDrainer (unify Bucket and Handler logic with min/max withdrawal and deposit)
    // TODO: ManagedItemHandler
    // TODO: ManagedEnergyHandler (idk about this one)
}
