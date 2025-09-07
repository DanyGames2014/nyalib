package net.danygames2014.nyalib.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.resource.language.I18n;
import net.modificationstation.stationapi.api.util.Identifier;

import java.awt.*;


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

    /**
     * Determines if the fluid is placeable in world
     */
    private boolean isPlaceableInWorld = true;

    /**
     * The sound that will be played when the bucket is filled
     */
    private String fillSound = null;

    /**
     * The sound the bucket will make when a bucket of this fluid is emptied
     */
    private String emptySound = null;

    /**
     * The color of the fluid, used for the bucket for the fluid.
     * This represents an ARGB color
     */
    private int color = 0xFFFFFFFF;
    
    private boolean automaticBucketRegistration = true;

    public Fluid(Identifier identifier, Block still, Block flowing) {
        this.identifier = identifier;
        this.still = still;
        this.flowing = flowing;
    }

    public Fluid(Identifier identifier, Block still, Block flowing, int color) {
        this(identifier, still, flowing);
        this.color = color;
    }

    public Fluid(Identifier identifier, Block still, Block flowing, Color color) {
        this(identifier, still, flowing);
        this.color = color.getRGB();
    }

    // Identifier
    public Identifier getIdentifier() {
        return identifier;
    }

    // Fluid Color
    public int getColor() {
        return color;
    }

    public Fluid setColor(int color) {
        this.color = color;
        return this;
    }

    public Fluid setColor(Color color) {
        this.color = color.getRGB();
        return this;
    }

    // Buckets
    public int getBucketSize() {
        return bucketSize;
    }

    public Fluid setBucketSize(int bucketSize) {
        this.bucketSize = bucketSize;
        return this;
    }

    public Block getBucketFluid() {
        return flowing;
    }
    
    public boolean automaticBucketRegistration() {
        return automaticBucketRegistration;
    }
    
    public Fluid disableAutomaticBucketRegistration() {
        this.automaticBucketRegistration = false;
        return this;
    }

    // Bucket Behavior
    public boolean isPlaceableInWorld() {
        return isPlaceableInWorld;
    }

    public Fluid setPlaceableInWorld(boolean placeableInWorld) {
        this.isPlaceableInWorld = placeableInWorld;
        return this;
    }

    // Bucket Sound
    public String getFillSound() {
        if (fillSound == null) {
            if (getBucketFluid() != null && getBucketFluid().material == Material.LAVA) {
                return "nyalib-fluid:item.bucket.fill_lava";
            } else {
                return "nyalib-fluid:item.bucket.fill";
            }
        }

        return fillSound;
    }

    public Fluid setFillSound(String fillSound) {
        this.fillSound = fillSound;
        return this;
    }

    public String getEmptySound() {
        if (emptySound == null) {
            if (getBucketFluid() != null && getBucketFluid().material == Material.LAVA) {
                return "nyalib-fluid:item.bucket.empty_lava";
            } else {
                return "nyalib-fluid:item.bucket.empty";
            }
        }

        return emptySound;
    }

    public Fluid setEmptySound(String emptySound) {
        this.emptySound = emptySound;
        return this;
    }

    // Blocks
    public Block getStillBlock() {
        return still;
    }

    public Block getFlowingBlock() {
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
