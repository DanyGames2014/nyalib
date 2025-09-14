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
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.awt.*;


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
     * The default light level this fluid emits
     */
    private int lightLevel = 0;
    
    /**
     * The tick rate governs how fast the fluid will spread.
     * It is the interval in ticks between each spread.
     * <p>
     * <p>Water has a tick rate of 5
     * <p>Lava has a tick rate of 30
     */
    private int tickRate = 5;

    /**
     * Determines the default behavior of if entities can swim in this fluid
     */
    private boolean canSwimIn = true;

    /**
     * The default movement speed multiplier when in this fluid.
     * <p> Won't matter if {@link #canSwim(Entity)} returns false
     */
    private double movementSpeedMultiplier = 1.0F;

    /**
     * Determines the default behavior of if entities will drown in this fluid.
     */
    private boolean willDrown = true;

    /**
     * The default color multiplier for this fluid.
     */
    private int colorMultiplier = 0xFFFFFF;

    // TODO: LiquidBlock.randomDisplayTick
    // TODO: Entity.isWet
    // TODO: Entity.isTouchingLava
    // TODO: LiquidBlock.checkBlockCollisions
    // TODO: LiquidBlock.fizz?
    // TODO: Custom fluid interactions
    // TODO: Automatic block using only color multiplier?
    // TODO: Track down where everywhere material for fluids is compared
    // TODO: Properly implement fluids which cannot be placed into world
    // TODO: Density?
    // TODO: Temperature? Per Stack (pain^2) ? Per Fluid?
    // TODO: Viscosity?
    // TODO: Gaseous Fluids?
    // TODO: Fluids without a block implementation?
    // TODO: Interactions -> Vaporization in Nether
    // TODO: Methods with world/stack contexts
    // TODO: Entity.isInFluid(Fluid) when https://github.com/FabricMC/fabric-loom/issues/1334 is fixed
    // TODO: When maps are fixed. Make it so the map color of the fluid is reduced to either 16-bit or 8-bit color.
    // TODO: Also make it so after the reduction the method first tries to find an existing map color that matches (maybe in range)

    // Base constructor
    public Fluid(Identifier identifier, Block still, Block flowing) {
        this.identifier = identifier;
        this.still = still;
        this.flowing = flowing;
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
//        if (fillSound == null) {
//            if (getBucketFluid() != null && getBucketFluid().material == Material.LAVA) {
//                return "nyalib-fluid:item.bucket.fill_lava";
//            } else {
//                return "nyalib-fluid:item.bucket.fill";
//            }
//        }

        return fillSound;
    }

    public Fluid setFillSound(String fillSound) {
        this.fillSound = fillSound;
        return this;
    }

    public String getEmptySound() {
//        if (emptySound == null) {
//            if (getBucketFluid() != null && getBucketFluid().material == Material.LAVA) {
//                return "nyalib-fluid:item.bucket.empty_lava";
//            } else {
//                return "nyalib-fluid:item.bucket.empty";
//            }
//        }

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
            return this;
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
            return this;
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
    public int getLightLevel(BlockState state) {
        return lightLevel;
    }

    public Fluid setLightLevel(int lightLevel) {
        if (lightLevel < 0) {
            this.lightLevel = 0;
            return this;
        }
        
        if  (lightLevel > 15) {
            this.lightLevel = 15;
            return this;
        }
        
        this.lightLevel = lightLevel;
        return this;
    }

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
        return this.movementSpeedMultiplier;
    }

    public Fluid setMovementSpeedMultiplier(double movementSpeedMultiplier) {
        this.movementSpeedMultiplier = movementSpeedMultiplier;
        return this;
    }
    
    public boolean willDrown(LivingEntity livingEntity) {
        return willDrown && !livingEntity.canBreatheInWater();
    }
    
    public Fluid setWillDrown(boolean willDrown) {
        this.willDrown = willDrown;
        return this;
    }
    
    // Rendering
    public int getColorMultiplier(BlockView blockView, int x, int y, int z) {
        return colorMultiplier;
    }
    
    public int setColorMultiplier(int colorMultiplier) {
        this.colorMultiplier = colorMultiplier;
        return this.colorMultiplier;
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
