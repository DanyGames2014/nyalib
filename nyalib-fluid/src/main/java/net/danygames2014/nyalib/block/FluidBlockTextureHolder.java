package net.danygames2014.nyalib.block;

import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.util.Identifier;

public class FluidBlockTextureHolder {
    public int stillSpriteId;
    
    public int flowingSpriteId;
    
    public int overlaySpriteId = -1;
    public int overlayTextureWidth = 16;
    public int overlayTextureHeight = 16;

    public FluidBlockTextureHolder() {
    }

    public int getStillTextureId() {
        return stillSpriteId;
    }

    public void addStillTexture(Identifier identifier) {
        ExpandableAtlas terrainAtlas = Atlases.getTerrain();

        stillSpriteId = terrainAtlas.addTexture(identifier).index;
    }

    public void addFlowingTexture(Identifier identifier) {
        ExpandableAtlas terrainAtlas = Atlases.getTerrain();

        flowingSpriteId = terrainAtlas.addTexture(identifier).index;
    }

    public int getFlowingTextureId() {
        return flowingSpriteId;
    }

    public void addOverlayTexture(Identifier identifier, int textureWidth, int textureHeight) {
        overlaySpriteId = Minecraft.INSTANCE.textureManager.getTextureId("/assets/" + identifier.namespace + "/stationapi/textures/" + identifier.path + ".png");
        overlayTextureWidth = textureWidth;
        overlayTextureHeight = textureHeight;
    }
    
    public int getOverlayTextureId() {
        return overlaySpriteId;
    }
}
