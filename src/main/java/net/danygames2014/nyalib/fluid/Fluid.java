package net.danygames2014.nyalib.fluid;

import net.minecraft.block.Block;
import net.minecraft.client.resource.language.I18n;
import net.modificationstation.stationapi.api.util.Identifier;


public final class Fluid {
    private final Identifier identifier;
    private final Block still;
    private final Block flowing;
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
