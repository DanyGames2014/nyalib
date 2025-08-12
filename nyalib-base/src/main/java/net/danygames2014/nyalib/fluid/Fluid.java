package net.danygames2014.nyalib.fluid;

import net.minecraft.block.Block;
import net.minecraft.client.resource.language.I18n;
import net.modificationstation.stationapi.api.util.Identifier;


public final class Fluid {
    /**
     * The unique identifier of the fluid
     */
    private final Identifier identifier;
    /**
     * The in-world block representing the fluid in its still state
     */
    private final Block still;
    /**
     * The in-world block representing the fluid in its flowing state
     */
    private final Block flowing;
    /**
     * This determines how much of a fluid in mB does a bucket hold.
     * This will be used when a fluid is placed into the world or taken from it.
     */
    private int bucketSize = 1000;

    public Fluid(Identifier identifier, Block still, Block flowing) {
        this.identifier = identifier;
        this.still = still;
        this.flowing = flowing;
    }

    // Identifier
    public Identifier getIdentifier() {
        return identifier;
    }
    
    // Buckets
    public Fluid setBucketSize(int bucketSize) {
        this.bucketSize = bucketSize;
        return this;
    }
    
    public int getBucketSize() {
        return bucketSize;
    }
    
    public Block getBucketFluid(){
        return flowing;
    }
    
    // Blocks
    public Block getStillBlock(){
        return still;
    }
    
    public Block getFlowingBlock(){
        return flowing;
    }
    
    // Localization
    public String getTranslationKey() {
        return "fluid." + identifier.namespace + "." + identifier.path + ".name";
    }

    public String getTranslatedName() {
        return I18n.getTranslation(getTranslationKey());
    }
}
