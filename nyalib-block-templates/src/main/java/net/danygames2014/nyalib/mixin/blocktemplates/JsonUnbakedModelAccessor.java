package net.danygames2014.nyalib.mixin.blocktemplates;

import com.mojang.datafixers.util.Either;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.render.model.json.JsonUnbakedModel;
import net.modificationstation.stationapi.api.client.texture.SpriteIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(value = JsonUnbakedModel.class, remap = false)
public interface JsonUnbakedModelAccessor {
    @Accessor("textureMap")
    Map<String, Either<SpriteIdentifier, String>> getTextureMap(); 
}
