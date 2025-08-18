package net.danygames2014.nyalib.block;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
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
    
    // Block Identifier -> Texture Identifier
    public static HashMap<Identifier, Identifier> buttons = new HashMap<>();
    
    // Block Identifier -> Texture Identifier
    public static HashMap<Identifier, Identifier> walls = new HashMap<>();
    
    // TODO: TemplatePressurePlate, TemplateLadder, TemplateDoor, TemplateTrapdoor, TemplateFlowerPot, TemplateWall, TemplateThinSometing(glasspane, iron bars), TemplateCarpet, TemplateSign, TemplateTorch
    
    // Block Identifier -> End Texture Identifier, Side Texture Identifier
    public static HashMap<Identifier, Pair<Identifier, Identifier>> rotateableBlockTemplate = new HashMap<>();
    
    public static void registerStairs(Identifier blockIdentifier, Identifier texture) {
        stairs.put(blockIdentifier, texture);

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            return;
        }
        
        JsonOverrideRegistry.registerBlockModelOverride(blockIdentifier, stairsJson);
        JsonOverrideRegistry.registerBlockModelTextureOverride(blockIdentifier, "all", texture);

        JsonOverrideRegistry.registerItemModelOverride(blockIdentifier, itemJson.replace("PATH", getBlockModelPath(blockIdentifier)));

        JsonOverrideRegistry.registerBlockstateOverride(blockIdentifier, stairsStateJson.replace("PATH", getBlockModelPath(blockIdentifier)));
    }

    public static void registerSlab(Identifier blockIdentifier, Identifier texture) {
        slabs.put(blockIdentifier, texture);

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            return;
        }
        
        JsonOverrideRegistry.registerBlockModelOverride(blockIdentifier + "_single", slabJson);
        JsonOverrideRegistry.registerBlockModelTextureOverride(blockIdentifier + "_single", "all", texture);

        JsonOverrideRegistry.registerBlockModelOverride(blockIdentifier + "_double", doubleSlabJson);
        JsonOverrideRegistry.registerBlockModelTextureOverride(blockIdentifier + "_double", "all", texture);

        JsonOverrideRegistry.registerItemModelOverride(blockIdentifier, itemJson.replace("PATH", getBlockModelPath(blockIdentifier) + "_single"));

        String slabState = slabStateJson;
        slabState = slabState.replace("SINGLE", getBlockModelPath(blockIdentifier + "_single"));
        slabState = slabState.replace("DOUBLE", getBlockModelPath(blockIdentifier + "_double"));
        JsonOverrideRegistry.registerBlockstateOverride(blockIdentifier, slabState);
    }

    public static void registerFence(Identifier blockIdentifier, Identifier texture) {
        fences.put(blockIdentifier, texture);

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            return;
        }
        
        JsonOverrideRegistry.registerBlockModelOverride(blockIdentifier + "_post", fencePostJson);
        JsonOverrideRegistry.registerBlockModelTextureOverride(blockIdentifier + "_post", "side", texture);
        JsonOverrideRegistry.registerBlockModelTextureOverride(blockIdentifier + "_post", "end", texture);

        JsonOverrideRegistry.registerBlockModelOverride(blockIdentifier + "_side", fenceSideJson);
        JsonOverrideRegistry.registerBlockModelTextureOverride(blockIdentifier + "_side", "texture", texture);

        JsonOverrideRegistry.registerItemModelOverride(blockIdentifier, fenceInventoryJson);
        JsonOverrideRegistry.registerItemModelTextureOverride(blockIdentifier, "texture", texture);

        String fenceState = fenceStateJson;
        fenceState = fenceState.replace("POST", getBlockModelPath(blockIdentifier + "_post"));
        fenceState = fenceState.replace("SIDE", getBlockModelPath(blockIdentifier + "_side"));
        JsonOverrideRegistry.registerBlockstateOverride(blockIdentifier, fenceState);
    }

    public static void registerFenceGate(Identifier blockIdentifier, Identifier texture) {
        fenceGates.put(blockIdentifier, texture);

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            return;
        }
        
        JsonOverrideRegistry.registerBlockModelOverride(blockIdentifier + "_open", fenceGateOpenJson);
        JsonOverrideRegistry.registerBlockModelTextureOverride(blockIdentifier + "_open", "texture", texture);

        JsonOverrideRegistry.registerBlockModelOverride(blockIdentifier + "_closed", fenceGateCloseJson);
        JsonOverrideRegistry.registerBlockModelTextureOverride(blockIdentifier + "_closed", "texture", texture);

        JsonOverrideRegistry.registerItemModelOverride(blockIdentifier, itemJson.replace("PATH", getBlockModelPath(blockIdentifier + "_closed")));

        String fenceGateState = fenceGateStateJson;
        fenceGateState = fenceGateState.replace("OPEN", getBlockModelPath(blockIdentifier + "_open"));
        fenceGateState = fenceGateState.replace("CLOSED", getBlockModelPath(blockIdentifier + "_closed"));
        JsonOverrideRegistry.registerBlockstateOverride(blockIdentifier, fenceGateState);
    }
    
    public static void registerButton(Identifier blockIdentifier, Identifier texture) {
        buttons.put(blockIdentifier, texture);
        
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            return;
        }
        
        JsonOverrideRegistry.registerBlockModelOverride(blockIdentifier + "_inventory", buttonInventoryJson);
        JsonOverrideRegistry.registerBlockModelTextureOverride(blockIdentifier + "_inventory", "texture", texture);
        
        JsonOverrideRegistry.registerBlockModelOverride(blockIdentifier + "_normal", buttonJson);
        JsonOverrideRegistry.registerBlockModelTextureOverride(blockIdentifier + "_normal", "texture", texture);
        
        JsonOverrideRegistry.registerBlockModelOverride(blockIdentifier + "_pressed", buttonPressedJson);
        JsonOverrideRegistry.registerBlockModelTextureOverride(blockIdentifier + "_pressed", "texture", texture);

        JsonOverrideRegistry.registerItemModelOverride(blockIdentifier, buttonInventoryJson);
        JsonOverrideRegistry.registerItemModelTextureOverride(blockIdentifier, "texture", texture);
        
        String buttonState = buttonStateJson;
        buttonState = buttonState.replace("NORMAL", getBlockModelPath(blockIdentifier + "_normal"));
        buttonState = buttonState.replace("PRESSED", getBlockModelPath(blockIdentifier + "_pressed"));
        JsonOverrideRegistry.registerBlockstateOverride(blockIdentifier, buttonState);
        
    }
    
    public static void registerWall(Identifier blockIdentifier, Identifier texture) {
        walls.put(blockIdentifier, texture);
        
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            return;
        }
        
        JsonOverrideRegistry.registerBlockModelOverride(blockIdentifier + "_inventory", wallInventoryJson);
        JsonOverrideRegistry.registerBlockModelTextureOverride(blockIdentifier + "_inventory", "texture", texture);
        
        JsonOverrideRegistry.registerBlockModelOverride(blockIdentifier + "_post", wallPostJson);
        JsonOverrideRegistry.registerBlockModelTextureOverride(blockIdentifier + "_post", "texture", texture);
        
        JsonOverrideRegistry.registerBlockModelOverride(blockIdentifier + "_side", wallSideJson);
        JsonOverrideRegistry.registerBlockModelTextureOverride(blockIdentifier + "_side", "texture", texture);
        
        JsonOverrideRegistry.registerBlockModelOverride(blockIdentifier + "_side_tall", wallSideTallJson);
        JsonOverrideRegistry.registerBlockModelTextureOverride(blockIdentifier + "_side_tall", "texture", texture);
        
        JsonOverrideRegistry.registerItemModelOverride(blockIdentifier, wallInventoryJson);
        JsonOverrideRegistry.registerItemModelTextureOverride(blockIdentifier, "texture", texture);
        
        String wallState = wallStateJson;
        wallState = wallState.replace("POST", getBlockModelPath(blockIdentifier + "_post"));
        wallState = wallState.replace("SIDE", getBlockModelPath(blockIdentifier + "_side"));
        wallState = wallState.replace("TALL", getBlockModelPath(blockIdentifier + "_side_tall"));
        JsonOverrideRegistry.registerBlockstateOverride(blockIdentifier, wallState);
    }

    public static void registerRotateableBlock(Identifier blockIdentifier, Identifier endTexture, Identifier sideTexture) {
        rotateableBlockTemplate.put(blockIdentifier, new ObjectObjectImmutablePair<>(endTexture, sideTexture));

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            return;
        }
    }

    public static String getBlockModelPath(String blockIdentifier) {
        return getBlockModelPath(Identifier.of(blockIdentifier));
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
    public static final String fenceInventoryJson = ("""
            {
              "parent": "nyalib-base:block/fence_inventory",
              "textures": {
              }
            }
            """
    );

    public static final String fenceSideJson = ("""
            {
              "parent": "nyalib-base:block/fence_side",
              "textures": {
              }
            }"""
    );

    public static final String fencePostJson = ("""
            {
              "parent": "nyalib-base:block/fence_post",
              "textures": {
              }
            }"""
    );

    public static final String fenceStateJson = ("""
            {
              "multipart": [
                {
                  "apply": {
                    "model": "POST"
                  }
                },
                {
                  "apply": {
                    "model": "SIDE",
                    "uvlock": true,
                    "y": 270
                  },
                  "when": {
                    "north": "true"
                  }
                },
                {
                  "apply": {
                    "model": "SIDE",
                    "uvlock": true,
                    "y": 0
                  },
                  "when": {
                    "east": "true"
                  }
                },
                {
                  "apply": {
                    "model": "SIDE",
                    "uvlock": true,
                    "y": 90
                  },
                  "when": {
                    "south": "true"
                  }
                },
                {
                  "apply": {
                    "model": "SIDE",
                    "uvlock": true,
                    "y": 180
                  },
                  "when": {
                    "west": "true"
                  }
                }
              ]
            }"""
    );

    // Fence Gate
    public static final String fenceGateOpenJson = ("""
            {
              "parent": "nyalib-base:block/fence_gate_open",
              "textures": {
              }
            }"""
    );

    public static final String fenceGateCloseJson = ("""
            {
              "parent": "nyalib-base:block/fence_gate_closed",
              "textures": {
              }
            }"""
    );

    public static final String fenceGateStateJson = ("""
            {
              "variants": {
                "facing=south,open=false": {
                  "model": "CLOSED",
                  "uvlock": true,
                  "y": 270
                },
                "facing=west,open=false": {
                  "model": "CLOSED",
                  "uvlock": true,
                  "y": 0
                },
                "facing=north,open=false": {
                  "model": "CLOSED",
                  "uvlock": true,
                  "y": 90
                },
                "facing=east,open=false": {
                  "model": "CLOSED",
                  "uvlock": true,
                  "y": 180
                },
                "facing=south,open=true": {
                  "model": "OPEN",
                  "uvlock": true,
                  "y": 270
                },
                "facing=west,open=true": {
                  "model": "OPEN",
                  "uvlock": true,
                  "y": 0
                },
                "facing=north,open=true": {
                  "model": "OPEN",
                  "uvlock": true,
                  "y": 90
                },
                "facing=east,open=true": {
                  "model": "OPEN",
                  "uvlock": true,
                  "y": 180
                }
              }
            }
            """
    );
    
    // Button
    public static final String buttonInventoryJson = ("""
            {
              "parent": "nyalib-base:block/button_inventory",
              "textures": {
              }
            }"""
    );
    
    public static final String buttonJson = ("""
            {
              "parent": "nyalib-base:block/button",
              "textures": {
              }
            }"""
    );
    
    public static final String buttonPressedJson = ("""
            {
              "parent": "nyalib-base:block/button_pressed",
              "textures": {
              }
            }"""
    );
    
    public static final String buttonStateJson = ("""
            {
              "variants": {
                "type=ceiling,facing=east,powered=false": {
                  "model": "NORMAL",
                  "x": 180,
                  "y": 180
                },
                "type=ceiling,facing=east,powered=true": {
                  "model": "PRESSED",
                  "x": 180,
                  "y": 180
                },
                "type=ceiling,facing=north,powered=false": {
                  "model": "NORMAL",
                  "x": 180,
                  "y": 90
                },
                "type=ceiling,facing=north,powered=true": {
                  "model": "PRESSED",
                  "x": 180,
                  "y": 90
                },
                "type=ceiling,facing=south,powered=false": {
                  "model": "NORMAL",
                  "x": 180,
                  "y": 270
                },
                "type=ceiling,facing=south,powered=true": {
                  "model": "PRESSED",
                  "x": 180,
                  "y": 270
                },
                "type=ceiling,facing=west,powered=false": {
                  "model": "NORMAL",
                  "x": 180,
                  "y": 0
                },
                "type=ceiling,facing=west,powered=true": {
                  "model": "PRESSED",
                  "x": 180,
                  "y": 0
                },
                "type=floor,facing=east,powered=false": {
                  "model": "NORMAL",
                  "y": 0
                },
                "type=floor,facing=east,powered=true": {
                  "model": "PRESSED",
                  "y": 0
                },
                "type=floor,facing=north,powered=false": {
                  "model": "NORMAL",
                  "y": 270
                },
                "type=floor,facing=north,powered=true": {
                  "model": "PRESSED",
                  "y": 270
                },
                "type=floor,facing=south,powered=false": {
                  "model": "NORMAL",
                  "y": 90
                },
                "type=floor,facing=south,powered=true": {
                  "model": "PRESSED",
                  "y": 90
                },
                "type=floor,facing=west,powered=false": {
                  "model": "NORMAL",
                  "y": 180
                },
                "type=floor,facing=west,powered=true": {
                  "model": "PRESSED",
                  "y": 180
                },
                "type=wall,facing=east,powered=false": {
                  "model": "NORMAL",
                  "uvlock": true,
                  "x": 90,
                  "y": 0
                },
                "type=wall,facing=east,powered=true": {
                  "model": "PRESSED",
                  "uvlock": true,
                  "x": 90,
                  "y": 0
                },
                "type=wall,facing=north,powered=false": {
                  "model": "NORMAL",
                  "uvlock": true,
                  "x": 90,
                  "y": 270
                },
                "type=wall,facing=north,powered=true": {
                  "model": "PRESSED",
                  "uvlock": true,
                  "x": 90,
                  "y": 270
                },
                "type=wall,facing=south,powered=false": {
                  "model": "NORMAL",
                  "uvlock": true,
                  "x": 90,
                  "y": 90
                },
                "type=wall,facing=south,powered=true": {
                  "model": "PRESSED",
                  "uvlock": true,
                  "x": 90,
                  "y": 90
                },
                "type=wall,facing=west,powered=false": {
                  "model": "NORMAL",
                  "uvlock": true,
                  "x": 90,
                  "y": 180
                },
                "type=wall,facing=west,powered=true": {
                  "model": "PRESSED",
                  "uvlock": true,
                  "x": 90,
                  "y": 180
                }
              }
            }"""
    );
    
    // Wall
    public static final String wallInventoryJson = ("""
            {
              "parent": "nyalib-base:block/wall_inventory",
              "textures": {
              }
            }"""
    );
    
    public static final String wallPostJson = ("""
            {
              "parent": "nyalib-base:block/wall_post",
              "textures": {
              }
            }"""
    );

    public static final String wallSideJson = ("""
            {
              "parent": "nyalib-base:block/wall_side",
              "textures": {
              }
            }"""
    );

    public static final String wallSideTallJson = ("""
            {
              "parent": "nyalib-base:block/wall_side_tall",
              "textures": {
              }
            }"""
    );
    
    public static final String wallStateJson = ("""
            {
              "multipart": [
                {
                  "apply": {
                    "model": "POST"
                  },
                  "when": {
                    "up": "true"
                  }
                },
                {
                  "apply": {
                    "model": "SIDE",
                    "uvlock": true
                  },
                  "when": {
                    "north": "low"
                  }
                },
                {
                  "apply": {
                    "model": "SIDE",
                    "uvlock": true,
                    "y": 90
                  },
                  "when": {
                    "east": "low"
                  }
                },
                {
                  "apply": {
                    "model": "SIDE",
                    "uvlock": true,
                    "y": 180
                  },
                  "when": {
                    "south": "low"
                  }
                },
                {
                  "apply": {
                    "model": "SIDE",
                    "uvlock": true,
                    "y": 270
                  },
                  "when": {
                    "west": "low"
                  }
                },
                {
                  "apply": {
                    "model": "TALL",
                    "uvlock": true
                  },
                  "when": {
                    "north": "tall"
                  }
                },
                {
                  "apply": {
                    "model": "TALL",
                    "uvlock": true,
                    "y": 90
                  },
                  "when": {
                    "east": "tall"
                  }
                },
                {
                  "apply": {
                    "model": "TALL",
                    "uvlock": true,
                    "y": 180
                  },
                  "when": {
                    "south": "tall"
                  }
                },
                {
                  "apply": {
                    "model": "TALL",
                    "uvlock": true,
                    "y": 270
                  },
                  "when": {
                    "west": "tall"
                  }
                }
              ]
            }"""
    );
}
