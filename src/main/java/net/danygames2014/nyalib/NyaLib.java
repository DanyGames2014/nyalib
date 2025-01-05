package net.danygames2014.nyalib;

import net.danygames2014.nyalib.config.Config;
import net.glasslauncher.mods.gcapi3.api.ConfigRoot;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import org.apache.logging.log4j.Logger;

public class NyaLib {
    @Entrypoint.Logger
    public static Logger LOGGER;

    @Entrypoint.Namespace
    public static Namespace NAMESPACE;
    
    @ConfigRoot(value = "item", visibleName = "Item API")
    public static final Config.ItemConfig ITEM_CONFIG = new Config.ItemConfig();
}
