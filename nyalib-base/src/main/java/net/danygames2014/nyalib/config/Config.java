package net.danygames2014.nyalib.config;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;

public class Config {
    public static class ItemConfig {
        @ConfigEntry(name = "Simplified Furnace Handling", description = "If enabled, furnaces don't care about insertion/extraction sides")
        public Boolean simplifiedFurnaceHandling = true;
    }
}
