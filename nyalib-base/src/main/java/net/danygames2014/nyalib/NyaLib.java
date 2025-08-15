package net.danygames2014.nyalib;

import com.mojang.datafixers.util.Either;
import net.danygames2014.nyalib.block.JsonOverrideRegistry;
import net.danygames2014.nyalib.config.Config;
import net.danygames2014.nyalib.mixin.block.JsonUnbakedModelAccessor;
import net.glasslauncher.mods.gcapi3.api.ConfigRoot;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.render.model.PreLoadUnbakedModelEvent;
import net.modificationstation.stationapi.api.client.render.model.json.JsonUnbakedModel;
import net.modificationstation.stationapi.api.client.texture.SpriteIdentifier;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class NyaLib {
    @Entrypoint.Logger
    public static Logger LOGGER;

    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @ConfigRoot(value = "item", visibleName = "Item API")
    public static final Config.ItemConfig ITEM_CONFIG = new Config.ItemConfig();

    @EventListener
    public void painAndSuffering(PreLoadUnbakedModelEvent event) {
        BlockRegistry.INSTANCE.get(event.identifier);
        
        if (event.identifier.toString().contains("tnt_stairs")) {
            System.err.println(event.identifier);
        }
        
        if (JsonOverrideRegistry.modelOverrides.containsKey(event.identifier)) {
            JsonUnbakedModel model = JsonUnbakedModel.deserialize(JsonOverrideRegistry.modelOverrides.get(event.identifier));

            System.out.println("Loaded model override for " + event.identifier);
            
            if (JsonOverrideRegistry.modelTextureOverrides.containsKey(event.identifier)) {
                Map<String, Either<SpriteIdentifier, String>> textureMap = ((JsonUnbakedModelAccessor) (Object) model).getTextureMap();

                for (Map.Entry<String, Identifier> entry : JsonOverrideRegistry.modelTextureOverrides.get(event.identifier).entrySet()) {
                    textureMap.put(entry.getKey(), Either.left(SpriteIdentifier.of(Atlases.GAME_ATLAS_TEXTURE, entry.getValue())));
                    System.out.println("Loaded texture override for " + event.identifier + "(" + entry.getKey() + " -> " + entry.getValue() + ")");
                }
            }

            event.loader = identifier -> model;
        }
    }
}
