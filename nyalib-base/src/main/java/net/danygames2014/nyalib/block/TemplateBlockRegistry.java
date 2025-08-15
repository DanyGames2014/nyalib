package net.danygames2014.nyalib.block;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.HashMap;

public class TemplateBlockRegistry {
    // Block Identifier -> Texture Identifier
    public static HashMap<Identifier, Identifier> stairs = new HashMap<>();

    // Block Identifier -> Texture Identifier
    public static HashMap<Identifier, Identifier> slabs = new HashMap<>();

    // Block Identifier -> Texture Identifier
    public static HashMap<Identifier, Identifier> fences = new HashMap<>();

    // Block Identifier -> Texture Identifier
    public static HashMap<Identifier, Identifier> fenceGates = new HashMap<>();

    // Block Identifier -> End Texture Identifier, Side Texture Identifier
    public static HashMap<Identifier, Pair<Identifier, Identifier>> rotateableBlockTemplate = new HashMap<>();

    public static void registerStairs(Identifier blockIdentifier, Identifier texture) {
        stairs.put(blockIdentifier, texture);
        JsonOverrideRegistry.registerBlockModelOverride(blockIdentifier, stairsJson);
        JsonOverrideRegistry.registerBlockModelTextureOverride(blockIdentifier, "all", texture);
        JsonOverrideRegistry.registerItemModelOverride(blockIdentifier, itemJson.replace("PATH", getBlockModelPath(blockIdentifier)));
        JsonOverrideRegistry.registerBlockstateOverride(blockIdentifier, stairsStateJson.replace("PATH", getBlockModelPath(blockIdentifier)));
    }

    public static void registerSlab(Identifier blockIdentifier, Identifier texture) {
        slabs.put(blockIdentifier, texture);
        JsonOverrideRegistry.registerBlockModelOverride(blockIdentifier, slabJson);
        JsonOverrideRegistry.registerBlockModelTextureOverride(blockIdentifier, "all", texture);

        JsonOverrideRegistry.registerBlockModelOverride(Identifier.of(blockIdentifier + "_double"), doubleSlabJson);
        JsonOverrideRegistry.registerBlockModelTextureOverride(Identifier.of(blockIdentifier + "_double"), "all", texture);

        JsonOverrideRegistry.registerItemModelOverride(blockIdentifier, itemJson.replace("PATH", getBlockModelPath(blockIdentifier)));

        String slabState = slabStateJson;
        slabState = slabState.replace("SINGLE", getBlockModelPath(blockIdentifier));
        slabState = slabState.replace("DOUBLE", getBlockModelPath(Identifier.of(blockIdentifier + "_double")));
        JsonOverrideRegistry.registerBlockstateOverride(blockIdentifier, slabState);
    }

    public static void registerFence(Identifier blockIdentifier, Identifier texture) {
        fences.put(blockIdentifier, texture);
    }

    public static void registerFenceGate(Identifier blockIdentifier, Identifier texture) {
        fenceGates.put(blockIdentifier, texture);
    }

    public static void registerRotateableBlock(Identifier blockIdentifier, Identifier endTexture, Identifier sideTexture) {
        rotateableBlockTemplate.put(blockIdentifier, new ObjectObjectImmutablePair<>(endTexture, sideTexture));
    }

    public static String getBlockModelPath(Identifier blockIdentifier) {
        return blockIdentifier.namespace + ":block/" + blockIdentifier.path;
    }

    // Item
    public static final String itemJson = ("""
            {
              "parent": "PATH"
            }"""
    );

    // Stairs
    public static final String stairsJson = ("""
            {
              "parent": "nyalib-base:block/stairs",
              "textures": {
              }
            }"""
    );

    public static final String stairsStateJson = ("""
            {
              "variants": {
                "facing=north": {
                  "model": "PATH"
                },
                "facing=east": {
                  "model": "PATH",
                  "y": 90
                },
                "facing=south": {
                  "model": "PATH",
                  "y": 180
                },
                "facing=west": {
                  "model": "PATH",
                  "y": 270
                }
              }
            }"""
    );

    // Slab
    public static final String slabJson = ("""
            {
              "parent": "nyalib-base:block/slab",
              "textures": {
              }
            }"""
    );

    public static final String doubleSlabJson = ("""
            {
              "parent": "minecraft:block/cube_all",
              "textures": {
              }
            }"""
    );

    public static final String slabStateJson = ("""
            {
              "variants": {
                "slab_type=bottom": {
                  "model": "SINGLE"
                },
            
                "slab_type=top": {
                  "model": "SINGLE",
                  "x": 180
                },
            
                "slab_type=double": {
                  "model": "DOUBLE"
                }
              }
            }"""
    );

    // Fence
    String fenceSideJson = ("""
            {
              "parent": "nyalib-base:block/fence_side",
              "textures": {
                "texture": "minecraft:block/sponge"
              }
            }"""
    );

    String fencePostJson = ("""
            {
              "parent": "nyalib-base:block/fence_post",
              "textures": {
                "side": "minecraft:block/sponge",
                "end": "minecraft:block/sponge"
              }
            }"""
    );

    // Fence Gate
    String fenceGateOpenJson = ("""
            {
              "parent": "nyalib-base:block/fence_gate_open",
              "textures": {
                "texture": "minecraft:block/sponge"
              }
            }"""
    );

    String fenceGateCloseJson = ("""
            {
              "parent": "nyalib-base:block/fence_gate_closed",
              "textures": {
                "texture": "minecraft:block/sponge"
              }
            }"""
    );
}
