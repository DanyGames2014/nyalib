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
    
    @ConfigRoot(value = "fluid", visibleName = "Fluid API")
    public static final Config.FluidConfig FLUID_CONFIG = new Config.FluidConfig();
    
    // TODO: Move config to configuration (more like the base mod pls) module?
}
