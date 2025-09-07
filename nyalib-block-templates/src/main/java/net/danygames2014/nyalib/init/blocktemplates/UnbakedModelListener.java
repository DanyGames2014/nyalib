package net.danygames2014.nyalib.init.blocktemplates;

import com.mojang.datafixers.util.Either;
import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.block.JsonOverrideRegistry;
import net.danygames2014.nyalib.mixin.blocktemplates.JsonUnbakedModelAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.render.model.PreLoadUnbakedModelEvent;
import net.modificationstation.stationapi.api.client.render.model.json.JsonUnbakedModel;
import net.modificationstation.stationapi.api.client.texture.SpriteIdentifier;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class UnbakedModelListener {
    @EventListener
    public void painAndSuffering(PreLoadUnbakedModelEvent event) {
        BlockRegistry.INSTANCE.get(event.identifier);

        if (JsonOverrideRegistry.modelOverrides.containsKey(event.identifier)) {
            JsonUnbakedModel model = JsonUnbakedModel.deserialize(JsonOverrideRegistry.modelOverrides.get(event.identifier));

            NyaLib.LOGGER.debug("Loaded model override for " + event.identifier);

            if (JsonOverrideRegistry.modelTextureOverrides.containsKey(event.identifier)) {
                Map<String, Either<SpriteIdentifier, String>> textureMap = ((JsonUnbakedModelAccessor) (Object) model).getTextureMap();

                for (Map.Entry<String, Identifier> entry : JsonOverrideRegistry.modelTextureOverrides.get(event.identifier).entrySet()) {
                    textureMap.put(entry.getKey(), Either.left(SpriteIdentifier.of(Atlases.GAME_ATLAS_TEXTURE, entry.getValue())));
                    NyaLib.LOGGER.debug("Loaded texture override for " + event.identifier + "(" + entry.getKey() + " -> " + entry.getValue() + ")");
                }
            }

            event.loader = identifier -> model;
        }
    }
}
