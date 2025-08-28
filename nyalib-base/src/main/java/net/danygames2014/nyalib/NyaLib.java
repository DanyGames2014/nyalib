package net.danygames2014.nyalib;

import net.danygames2014.nyalib.config.Config;
import net.glasslauncher.mods.gcapi3.api.ConfigRoot;
import net.modificationstation.stationapi.api.util.Namespace;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NyaLib {
    public static Logger LOGGER = LogManager.getLogger("NyaLib");

    public static Namespace NAMESPACE = Namespace.of("nyalib");

    @ConfigRoot(value = "item", visibleName = "Item API")
    public static final Config.ItemConfig ITEM_CONFIG = new Config.ItemConfig();
    
    // TODO: Consistent sorting of methods across interfaces and capabilities
    // TODO: Consistent documentation on item/fluid/energy interface and their capabilities with propagated docs
    // TODO: Move config to configuration module?
}
