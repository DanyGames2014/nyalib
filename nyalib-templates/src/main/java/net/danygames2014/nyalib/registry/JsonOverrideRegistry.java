package net.danygames2014.nyalib.registry;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.Identifier;

@Environment(EnvType.CLIENT)
public class JsonOverrideRegistry {
    public static final Object2ObjectOpenHashMap<Identifier, String> modelOverrides = new Object2ObjectOpenHashMap<>();

    public static final Object2ObjectOpenHashMap<Identifier, Object2ObjectOpenHashMap<String, Identifier>> modelTextureOverrides = new Object2ObjectOpenHashMap<>();

    public static final Object2ObjectOpenHashMap<Identifier, ObjectArrayList<String>> blockstateOverrides = new Object2ObjectOpenHashMap<>();
    
    // Item Model Override
    public static void registerItemModelOverride(Identifier itemIdentifier, String model) {
        Identifier identifier = Identifier.of(itemIdentifier.namespace + ":item/" + itemIdentifier.path + "#inventory");
        modelOverrides.put(identifier, model);
    }

    public static void registerItemModelOverride(String  itemIdentifier, String model) {
        registerItemModelOverride(Identifier.of(itemIdentifier), model);
    }
    
    // Item Model Texture Override
    public static void registerItemModelTextureOverride(Identifier itemIdentifier, String texture, Identifier textureIdentifier) {
        Identifier identifier = Identifier.of(itemIdentifier.namespace + ":item/" + itemIdentifier.path + "#inventory");

        Object2ObjectOpenHashMap<String, Identifier> modelTextures = modelTextureOverrides.computeIfAbsent(identifier, (id) -> new Object2ObjectOpenHashMap<>());
        modelTextures.put(texture, textureIdentifier);
    }
    
    public static void registerItemModelTextureOverride(String itemIdentifier, String texture, Identifier textureIdentifier) {
        registerItemModelTextureOverride(Identifier.of(itemIdentifier), texture, textureIdentifier);
    }
    
    // Block Model Override
    public static void registerBlockModelOverride(Identifier blockIdentifier, String model) {
        Identifier identifier = Identifier.of(blockIdentifier.namespace + ":block/" + blockIdentifier.path);
        modelOverrides.put(identifier, model);
    }

    public static void registerBlockModelOverride(String blockIdentifier, String model) {
        registerBlockModelOverride(Identifier.of(blockIdentifier), model);
    }
    
    // Block Model Texture Override
    public static void registerBlockModelTextureOverride(Identifier blockIdentifier, String texture, Identifier textureIdentifier) {
        Identifier identifier = Identifier.of(blockIdentifier.namespace + ":block/" + blockIdentifier.path);

        Object2ObjectOpenHashMap<String, Identifier> blockModelTextures = modelTextureOverrides.computeIfAbsent(identifier, (id) -> new Object2ObjectOpenHashMap<>());
        blockModelTextures.put(texture, textureIdentifier);
    }

    public static void registerBlockModelTextureOverride(String blockIdentifier, String texture, Identifier textureIdentifier) {
        registerBlockModelTextureOverride(Identifier.of(blockIdentifier), texture, textureIdentifier);        
    }
    
    // Blockstate Override
    public static void registerBlockstateOverride(Identifier blockIdentifier, String blockstate) {
        ObjectArrayList<String> blockEntries = blockstateOverrides.computeIfAbsent(blockIdentifier, (id) -> new ObjectArrayList<>());
        blockEntries.add(blockstate);
    }

    public static void registerBlockstateOverride(String blockIdentifier, String blockstate) {
        registerBlockstateOverride(Identifier.of(blockIdentifier), blockstate);
    }
}
