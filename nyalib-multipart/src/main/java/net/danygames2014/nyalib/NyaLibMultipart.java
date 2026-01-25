package net.danygames2014.nyalib;

import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import org.apache.logging.log4j.Logger;

public class NyaLibMultipart {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @Entrypoint.Logger
    public static Logger LOGGER;
    
    // TODO: Multiplayer syncing
    // TODO: Bounds checking
    // TODO: BlockEntity support
    // TODO: Fake block when no block present
    // TODO: Swap fake block for real block when a real block is placed
    // TODO: Rendering
    // TODO: Explosio nhandling
    // TODO: luminance?
}
