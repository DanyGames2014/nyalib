package net.danygames2014.nyalib;

import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Logger;

public class NyaLib {
    @Entrypoint.Logger
    public static final Logger LOGGER = Null.get();

    @Entrypoint.Namespace
    public static final Namespace NAMESPACE = Null.get();
}
