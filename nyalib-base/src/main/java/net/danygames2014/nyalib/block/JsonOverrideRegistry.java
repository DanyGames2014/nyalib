package net.danygames2014.nyalib.block;

import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonOverrideRegistry {
    public static final HashMap<Identifier, String> modelOverrides = new HashMap<>();

    public static final HashMap<Identifier, HashMap<String, Identifier>> modelTextureOverrides = new HashMap<>();

    public static final HashMap<Identifier, ArrayList<String>> blockstateOverrides = new HashMap<>();
    
    public static void registerItemModelOverride(Identifier itemIdentifier, String model) {
        Identifier identifier = Identifier.of(itemIdentifier.namespace + ":item/" + itemIdentifier.path + "#inventory");
        modelOverrides.put(identifier, model);
    }
    
    public static void registerItemModelTextureOverride(Identifier itemIdentifier, String texture, Identifier textureIdentifier) {
        Identifier identifier = Identifier.of(itemIdentifier.namespace + ":item/" + itemIdentifier.path + "#inventory");
        
        if (!modelTextureOverrides.containsKey(identifier)) {
            modelTextureOverrides.put(identifier, new HashMap<>());
        }
        
        HashMap<String, Identifier> textures = modelTextureOverrides.get(identifier);
        textures.put(texture, textureIdentifier);
    }
    
    public static void registerBlockModelOverride(Identifier blockIdentifier, String model) {
        Identifier identifier = Identifier.of(blockIdentifier.namespace + ":block/" + blockIdentifier.path);
        modelOverrides.put(identifier, model);
    }
    
    public static void registerBlockModelTextureOverride(Identifier blockIdentifier, String texture, Identifier textureIdentifier) {
        Identifier identifier = Identifier.of(blockIdentifier.namespace + ":block/" + blockIdentifier.path);
        
        if (!modelTextureOverrides.containsKey(identifier)) {
            modelTextureOverrides.put(identifier, new HashMap<>());
        }

        HashMap<String, Identifier> textures = modelTextureOverrides.get(identifier);
        textures.put(texture, textureIdentifier);
    }
    
    public static void registerBlockstateOverride(Identifier blockIdentifier, String blockstate) {
        Identifier identifier = Identifier.of(blockIdentifier.namespace + ":stationapi/blockstates/" + blockIdentifier.path + ".json");
        
        if (!blockstateOverrides.containsKey(identifier)) {
            blockstateOverrides.put(identifier, new ArrayList<>());
        }
        
        blockstateOverrides.get(identifier).add(blockstate);
    }
}
