package net.danygames2014.nyalib.fluid;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.block.FluidBlockManager;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Map;


@SuppressWarnings("UnusedReturnValue")
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

    /**
     * The tick rate governs how fast the fluid will spread.
     * It is the interval in ticks between each spread.
     * <p>
     * <p>Water has a tick rate of 5
     * <p>Lava has a tick rate of 30
     */
    private int tickRate = 5;

    /**
     * Determines the default behavior of if entities can swim in this method
     */
    private boolean canSwimIn = true;

    /**
     * The default swimspeed multiplier when swimming in this fluid.
     * <p> Won't matter if {@link #canSwim(Entity)} returns false
     */
    private double swimSpeedMultiplier = 1.0F;

    // TODO: Custom fluid interactions
    // TODO: Track down where everywhere material for fluids is compared
    // TODO: World.updateMovementInFluid
    // TODO: Custom overlay texture
    // TODO: Entity.isWet
    // TODO: Entity.isTouchingLava
    // TODO: LiquidBlock.randomDisplayTick
    // TODO: LiquidBlock.checkBlockCollisions
    // TODO: LiquidBlock.fizz?
    // TODO: OwO WhatsThis integration graphic improvement https://www.curseforge.com/minecraft/mc-mods/top-addons
    // TODO: OwO WhatsThis use fluid colors
    // TODO: Automatic block using only color multiplier?
    // TODO: Color Multiplier
    // TODO: Properly implement fluids which cannot be placed into world
    // TODO: Luminosity
    // TODO: Density?
    // TODO: Temperature? Per Stack (pain^2) ? Per Fluid?
    // TODO: Viscosity?
    // TODO: Gaseous Fluids?
    // TODO: Fluids without a block implementation?
    // TODO: @Nullable overlay parameter
    // TODO: Interactions -> Vaporization in Nether
    // TODO: Methods with world/stack contexts
    // TODO: Use fluid to register a MapColor for the fluid
    // TODO: Entity.isInFluid(Fluid) when https://github.com/FabricMC/fabric-loom/issues/1334 is fixed

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
    // Full Constructor with int color
    public Fluid(Identifier identifier, Identifier stillTexture, Identifier flowingTexture, Identifier overlayTexture, MapColor mapColor, int color) {
        this(identifier, null, null);
        this.setColor(color);
        FluidBlockManager.requestBlock(this, stillTexture, flowingTexture, overlayTexture, mapColor);
    }
    
    // Default map color
    public Fluid(Identifier identifier, Identifier stillTexture, Identifier flowingTexture, Identifier overlayTexture, int color) {
        this(identifier, stillTexture, flowingTexture, overlayTexture, Fluids.DEFAULT_FLUID_MATERIAL.mapColor, color);
    }

    // Default overlay texture
    public Fluid(Identifier identifier, Identifier stillTexture, Identifier flowingTexture, MapColor mapColor, int color) {
        this(identifier, stillTexture, flowingTexture, stillTexture, mapColor, color);
    }

    // Default overlay texture and map color
    public Fluid(Identifier identifier, Identifier stillTexture, Identifier flowingTexture, int color) {
        this(identifier, stillTexture, flowingTexture, stillTexture, Fluids.DEFAULT_FLUID_MATERIAL.mapColor, color);
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

    public Fluid setBucketItem(Item bucketItem) {
        this.bucketItem = bucketItem;
        return this;
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

    public Fluid setStillBlock(Block still) {
        if (this.still != null) {
            NyaLib.LOGGER.warn("Tried to set a still block to the fluid " + this.getIdentifier() + ", but it was already set!");
            return null;
        }

        this.still = still;
        return this;
    }

    public Block getFlowingBlock() {
        return flowing;
    }

    public Fluid setFlowingBlock(Block flowing) {
        if (this.flowing != null) {
            NyaLib.LOGGER.warn("Tried to set a flowing block to the fluid " + this.getIdentifier() + ", but it was already set!");
            return null;
        }

        this.flowing = flowing;
        return this;
    }

    public Material getMaterial() {
        if (flowing != null) {
            return flowing.material;
        }

        if (still != null) {
            return still.material;
        }

        return Fluids.DEFAULT_FLUID_MATERIAL;
    }

    // Fluid Properties
    public int getTickRate() {
        return tickRate;
    }

    public Fluid setTickRate(int tickRate) {
        if (tickRate < 0) {
            tickRate = 0;
        }

        this.tickRate = tickRate;
        return this;
    }

    // Swimming properties
    public boolean canSwim(Entity entity) {
        return this.canSwimIn;
    }

    public Fluid setCanSwimIn(boolean canSwimIn) {
        this.canSwimIn = canSwimIn;
        return this;
    }

    public double getMovementSpeedMultiplier(LivingEntity entity) {
        return this.swimSpeedMultiplier;
    }

    public Fluid setMovementSpeedMultiplier(double movementSpeedMultiplier) {
        this.swimSpeedMultiplier = movementSpeedMultiplier;
        return this;
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
