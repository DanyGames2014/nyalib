package net.danygames2014.nyalib.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import net.danygames2014.nyalib.block.JsonOverrideRegistry;
import net.modificationstation.stationapi.api.client.render.model.BakedModelManager;
import net.modificationstation.stationapi.api.client.render.model.ModelLoader;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.LOGGER;

@Mixin(value = BakedModelManager.class, remap = false)
public class BakedModelManagerMixin {
    @Inject(method = "lambda$reloadBlockStates$15(Ljava/util/concurrent/Executor;Ljava/util/Map;)Ljava/util/concurrent/CompletionStage;", at = @At(value = "INVOKE", target = "Ljava/util/Map;entrySet()Ljava/util/Set;"))
    private static void evenMorePain(Executor executor, Map blockStates2, CallbackInfoReturnable<CompletionStage> cir, @Local List<CompletableFuture<Pair<Identifier, List<ModelLoader.SourceTrackedData>>>> states){
        for (Map.Entry<Identifier, ArrayList<String>> pedroEntry : JsonOverrideRegistry.blockstateOverrides.entrySet()) {
            states.add(CompletableFuture.supplyAsync(() -> {
                List<String> jsonStrings = pedroEntry.getValue();
                List<ModelLoader.SourceTrackedData> statesList = new ArrayList<>(jsonStrings.size());
                for (String jsonString : jsonStrings)
                    try {
                        statesList.add(new ModelLoader.SourceTrackedData("Default", JsonHelper.deserialize(jsonString)));
                    } catch (Exception exception) {
                        LOGGER.error("Failed to load blockstate {} from pack {}", pedroEntry.getKey(), "Default", exception);
                    }
                return Pair.of(pedroEntry.getKey(), statesList);
            }, executor));
        }
    }
}
