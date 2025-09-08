package net.danygames2014.nyalib.fluid;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.block.FluidBlockManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.awt.*;


public final class Fluid {
    /**
     * The unique identifier of the fluid
     */
    private final Identifier identifier;
    /**
     * The in-world block representing the fluid in its still state
     */
    private Block still;
    /**
     * The in-world block representing the fluid in its flowing state
     */
    private Block flowing;
    /**
     * This determines how much of a fluid in mB does a bucket hold.
     * This will be used when a fluid is placed into the world or taken from it.
     */
    private int bucketSize = 1000;

    /**
     * This is the item that will be returned when the fluid is taken from world
     * If it is null, the fluid won't be taken from world
     */
    private Item bucketItem;
    
    /**
     * Determines if a bucket can place this fluid in world
     */
    private boolean isPlaceableInWorld = true;

    /**
     * Determines whether a bucket will be automatically registered for the fluid
     */
    private boolean automaticBucketRegistration = true;
    
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
    

    // Base constructor
    public Fluid(Identifier identifier, Block still, Block flowing) {
        this.identifier = identifier;
        this.still = still;
        this.flowing = flowing;
    }

    // Manual Block Constructors
    public Fluid(Identifier identifier, Block still, Block flowing, int color) {
        this(identifier, still, flowing);
        this.setColor(color);
    }

    public Fluid(Identifier identifier, Block still, Block flowing, Color color) {
        this(identifier, still, flowing);
        this.setColor(color);
    }

    // Automatic Block Constructors
    public Fluid(Identifier identifier, Identifier stillTexture, Identifier flowingTexture, int color) {
        this(identifier, null, null);
        this.setColor(color);
        FluidBlockManager.requestBlock(this, stillTexture, flowingTexture);
    }

    public Fluid(Identifier identifier, Identifier stillTexture, Identifier flowingTexture, Color color) {
        this(identifier, null, null);
        this.setColor(color);
        FluidBlockManager.requestBlock(this, stillTexture, flowingTexture);
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

    public @Nullable Item getBucketItem() {
        return bucketItem;
    }

    public void setBucketItem(Item bucketItem) {
        this.bucketItem = bucketItem;
    }

    public Block getBucketFluid() {
        return flowing;
    }
    
    public boolean automaticBucketRegistrationEnabled() {
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

    public void setStillBlock(Block still) {
        if (this.still != null) {
            NyaLib.LOGGER.warn("Tried to set a still block to the fluid " + this.getIdentifier() + ", but it was already set!");
            return;
        }
        
        this.still = still;
    }

    public Block getFlowingBlock() {
        return flowing;
    }

    public void setFlowing(Block flowing) {
        if (this.flowing != null) {
            NyaLib.LOGGER.warn("Tried to set a flowing block to the fluid " + this.getIdentifier() + ", but it was already set!");
            return;
        }
        
        this.flowing = flowing;
    }

    // Localization
    public String getTranslationKey() {
        return "fluid." + identifier.namespace + "." + identifier.path + ".name";
    }
    
    public String getTranslatedName() {
        return I18n.getTranslation(getTranslationKey());
    }

    // Fluidstack specific localization
    public String getTranslationKey(FluidStack fluidStack) {
        return getTranslationKey();
    }
    
    public String getTranslatedName(FluidStack fluidStack) {
        return getTranslatedName();
    }
}
