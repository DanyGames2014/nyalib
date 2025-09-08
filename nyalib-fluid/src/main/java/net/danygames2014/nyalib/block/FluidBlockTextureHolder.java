package net.danygames2014.nyalib.block;

import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.util.Identifier;

public class FluidBlockTextureHolder {
    public int stillSpriteId;
    public int flowingSpriteId;

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
}
