package net.danygames2014.nyalib.block;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.modificationstation.stationapi.api.client.texture.SpritesheetHelper;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.util.Identifier;

public class FluidBlockTextureHolder {
    public ImmutableList<Atlas.Sprite> stillSprites;
    public ImmutableList<Atlas.Sprite> flowingSprites;

    public FluidBlockTextureHolder() {
    }

    public int ticksPerStillTexture = 2;
    public int currentStillIndex = 0;
    public int currentStillTick = 2;
    public int currentStillTextureId = 0;
    
    public int getStillTextureId() {
        // Check if enough ticks passed to move onto next animation frame
        if (currentStillTick == ticksPerStillTexture) {
            // Check if we haven't reached the end of the sprites
            if (currentStillIndex >= stillSprites.size()) {
                currentStillIndex = 0;
            }
            
            // Load the current textur id of the sprite into the current variable
            currentStillTextureId = stillSprites.get(currentStillIndex).index;
            
            // Reset the tick counter
            currentStillTick = 0;
            
            // Return here to prevent erroneous counter increase
            return currentStillTextureId;
        }
        
        // Not enough ticks passed, increase the counter and return the current id
        currentStillTick++;
        return currentStillTextureId;
    }

    public void addStillTexture(Identifier identifier) {
        ExpandableAtlas terrainAtlas = Atlases.getTerrain();

        ImmutableList<Atlas.Sprite> sprite = terrainAtlas.addSpritesheet(16, new SpritesheetHelper() {
            final IntIntPair multiplier = new IntIntImmutablePair(1, 1);
            
            @Override
            public Identifier generateIdentifier(int i) {
                return identifier;
            }

            @Override
            public IntIntPair getResolutionMultiplier(int i) {
                return multiplier;
            }
        });
        
        //terrainAtlas.addTexture(fluidBlockEntry.stillTexture).index
    }
    
    public void addFlowingTexture(Identifier identifier) {
        ExpandableAtlas terrainAtlas = Atlases.getTerrain();
    }
}
