package net.danygames2014.nyalib.config;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;

public class Config {
    public static class ItemConfig {
        @ConfigEntry(name = "Simplified Furnace Handling", description = "If enabled, furnaces don't care about insertion/extraction sides", multiplayerSynced = true)
        public Boolean simplifiedFurnaceHandling = false;
    }
    
    public static class FluidConfig {
        @ConfigEntry(name = "Allow Placing Milk In World", description = "If enabled, milk can be placed in the world", requiresRestart = true, multiplayerSynced = true)
        public Boolean allowPlacingMilkInWorld = false;
    }
}
