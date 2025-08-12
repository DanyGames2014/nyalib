package net.danygames2014.nyalib.fluid;

import net.danygames2014.nyalib.NyaLib;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.HashMap;

@SuppressWarnings("unused")
public class FluidRegistry {
    public final HashMap<Identifier, Fluid> registry;
    private static FluidRegistry INSTANCE;

    private static FluidRegistry getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FluidRegistry();
        }
        return INSTANCE;
    }

    public FluidRegistry() {
        this.registry = new HashMap<>();
    }

    public static void register(Fluid fluid) {
        FluidRegistry.register(fluid.getIdentifier(), fluid);
    }
    
    public static void register(Identifier identifier, Fluid fluid) {
        if (getInstance().registry.containsKey(identifier)) {
            return;
        }

        NyaLib.LOGGER.info("Registering fluid {}", identifier);
        getInstance().registry.put(identifier, fluid);
    }

    public static Fluid get(Identifier identifier) {
        return getInstance().registry.getOrDefault(identifier, null);
    }

    public static HashMap<Identifier, Fluid> getRegistry() {
        return getInstance().registry;
    }
}
