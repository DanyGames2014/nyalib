package net.danygames2014.nyalib.fluid;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.block.FluidBlockManager;
import net.danygames2014.nyalib.util.MapColorUtil;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Identifier;

import java.awt.*;

@SuppressWarnings("UnusedReturnValue")
public class FluidBuilder {
    // Mandatory: Identifier
    private final Identifier identifier;

    // Manual: Blocks
    private final Block stillBlock;
    private final Block flowingBlock;

    // Automatic: Textures
    private final Identifier stillTexture;
    private final Identifier flowingTexture;
    private Identifier overlayTexture = null;

    // Fluid Parameters
    private Integer bucketSize = null;
    private Item bucketItem = null;
    private Boolean placeableInWorld = null;
    private Boolean automaticBucketRegistration = null;
    private String fillSound = null;
    private String emptySound = null;
    private Integer color = null;
    private MapColor mapColor = null;
    private Integer tickRate = null;
    private Boolean canSwimIn = null;
    private Double movementSpeedMultiplier = null;
    private Boolean willDrown = null;
    private Integer lightLevel = null;
    private Integer colorMultiplier = null;

    /**
     * Start a builder for a fluid using the fluid textures
     * Block implementations and textures will be automatically registered
     *
     * @param identifier     The {@link Identifier} of the fluid
     * @param stillTexture   The texture for the still state of the fluid
     * @param flowingTexture The texture for the flowing state of the fluid
     */
    public FluidBuilder(Identifier identifier, Identifier stillTexture, Identifier flowingTexture) {
        this.identifier = identifier;
        this.stillBlock = null;
        this.flowingBlock = null;
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
    }

    /**
     * <bold>NOT RECOMMENDED</bold>
     * <p>Start a builder for a fluid using a custom block implementations
     * <p>Please note that when this is used, many of the properties of the Fluid won't have an effect
     *
     * @param identifier   The {@link Identifier} of the fluid
     * @param stillBlock   The block that represents the still state of the fluid
     * @param flowingBlock The block that represents the flowing state of the fluid
     */
    public FluidBuilder(Identifier identifier, Block stillBlock, Block flowingBlock) {
        this.identifier = identifier;
        this.stillBlock = stillBlock;
        this.flowingBlock = flowingBlock;
        this.stillTexture = null;
        this.flowingTexture = null;
    }

    /**
     * <p> Set the texture that will be overlayed on the screen when the player is submerged in this fluid.
     * <p> If none is specified, the still texture will be used.
     *
     * @param overlayTexture The identifier for the texture. For example <code>NAMESPACE.id("block/myfluid_overlay")</code>
     * @return The Fluid Builder for chaining
     */
    public FluidBuilder overlayTexture(Identifier overlayTexture) {
        this.overlayTexture = overlayTexture;
        return this;
    }

    /**
     * <p> Set the color that this block will have on a map
     * <p> If this is not set, a map color will be automatically generated from the provided color in {@link #color(int)} or {@link #color(Color)}
     *
     * @param mapColor The map color to use on a map
     * @return The Fluid Builder for chaining
     */
    public FluidBuilder mapColor(MapColor mapColor) {
        this.mapColor = mapColor;
        return this;
    }

    /**
     * <p> Set the color that represents this fluid.
     * <p> This can also be written as a hex value in the format <code>0xAARRGGBB</code>, for example <code>0xFFCCAA00</code>
     * <p> This will be used for the bucket, WhatsThis and if you don't set one manually, the map color
     *
     * @param color The color to use
     * @return The Fluid Builder for chaining
     */
    public FluidBuilder color(int color) {
        this.color = color;
        return this;
    }

    /**
     * <p> Set the color that represents this fluid.
     * <p> This will be used for the bucket, WhatsThis and if you don't set one manually, the map color
     *
     * @param color The color to use
     * @return The Fluid Builder for chaining
     */
    public FluidBuilder color(Color color) {
        this.color = color.getRGB();
        return this;
    }

    /**
     * <p> Set the amount of fluid in mB that a bucket of this fluid will represent
     * <p> If this is not set, the default value will be used
     *
     * @param bucketSize The amount of fluid in mB
     * @return The Fluid Builder for chaining
     */
    public FluidBuilder bucketSize(int bucketSize) {
        this.bucketSize = bucketSize;
        return this;
    }

    /**
     * <p> Set the {@link Item} that will represent a bucket of this fluid
     * <p> This will mean that the only function that will be handled automatically is picking up fluid from world. The rest is on you
     * <p>
     * <p> If this is not set, a bucket will be automatically generated using the color provided in {@link #color(int)} or {@link #color(Color)}
     *
     * @param bucketItem The {@link Item} that represents a bucket of this fluid
     * @return The Fluid Builder for chaining
     */
    public FluidBuilder bucketItem(Item bucketItem) {
        this.bucketItem = bucketItem;
        return this;
    }


    /**
     * <p> Set if a bucket can place this fluid into the world
     * <p> If this is not set, the placement is allowed
     *
     * @param placeableInWorld If a bucket can place this fluid into world
     * @return The Fluid Builder for chaining
     */
    public FluidBuilder placeableInWorld(boolean placeableInWorld) {
        this.placeableInWorld = placeableInWorld;
        return this;
    }


    /**
     * Disabled the automatic registration of a bucket for this fluid
     *
     * @return The Fluid Builder for chaining
     */
    public FluidBuilder disableAutomaticBucketRegistration() {
        this.automaticBucketRegistration = false;
        return this;
    }

    /**
     * Sets the sound played when a bucket of this fluid is filled
     *
     * @param fillSound The path to the sound. For example <code>nyalib-fluid:item.bucket.fill</code>
     * @return The Fluid Builder for chaining
     */
    public FluidBuilder fillSound(String fillSound) {
        this.fillSound = fillSound;
        return this;
    }

    /**
     * <p> Sets the sound played when a bucket of this fluid is filled to a generic fill sound.
     * <p> This is the sound used for water buckets since Release 1.9
     *
     * @return The Fluid Builder for chaining
     */
    public FluidBuilder genericFillSound() {
        this.fillSound = "nyalib-fluid:item.bucket.fill";
        return this;
    }

    /**
     * <p> Sets the sound played when a bucket of this fluid is filled to a lava fill sound.
     * <p> This is the sound used for lava buckets since Release 1.9
     *
     * @return The Fluid Builder for chaining
     */
    public FluidBuilder lavaFillSound() {
        this.fillSound = "nyalib-fluid:item.bucket.fill_lava";
        return this;
    }

    /**
     * Sets the sound played when a bucket of this fluid is emptied
     *
     * @param emptySound The path to the sound. For example <code>nyalib-fluid:item.bucket.empty</code>
     * @return The Fluid Builder for chaining
     */
    public FluidBuilder emptySound(String emptySound) {
        this.emptySound = emptySound;
        return this;
    }

    /**
     * <p> Sets the sound played when a bucket of this fluid is emptied to a generic empty sound.
     * <p> This is the sound used for water buckets since Release 1.9
     *
     * @return The Fluid Builder for chaining
     */
    public FluidBuilder genericEmptySound() {
        this.emptySound = "nyalib-fluid:item.bucket.empty";
        return this;
    }

    /**
     * <p> Sets the sound played when a bucket of this fluid is emptied to a lava empty sound.
     * <p> This is the sound used for lava buckets since Release 1.9
     *
     * @return The Fluid Builder for chaining
     */
    public FluidBuilder lavaEmptySound() {
        this.emptySound = "nyalib-fluid:item.bucket.empty_lava";
        return this;
    }

    /**
     * <p> Sets the tick rate of this fluid. This determines how quickly it spreads.
     * <p> If this is not set, the fluid will default to the tick rate of Water
     * <p>
     * <p> Water has a tick rate of 5
     * <p> Lava has a tick rate of 30
     *
     * @param tickRate The amount of ticks between each spread
     * @return The Fluid Builder for chaining
     */
    public FluidBuilder tickRate(int tickRate) {
        this.tickRate = tickRate;
        return this;
    }


    /**
     * <p> Sets the default beahvior of if entities can swim in this fluid.
     * <p> This won't have any effect if you override {@link Fluid#canSwim(Entity)} in the Fluid class
     * <p> If this is not set, entities will be able to swim in this fluid
     *
     * @param canSwimIn The default value for if entities can swim in the fluid
     * @return The Fluid Builder for chaining
     */
    public FluidBuilder canSwimIn(boolean canSwimIn) {
        this.canSwimIn = canSwimIn;
        return this;
    }

    /**
     * <p> Sets the default movement speed multiplier of this fluid
     * <p> This won't have any effect if you override {@link Fluid#getMovementSpeedMultiplier(LivingEntity)} in the Fluid class
     * <p> If this is not set, the default of 1.0 will apply.
     * <p> Keep in mind that this is separate from the effects of fluid resistance and flow on the entity
     *
     * @param movementSpeedMultiplier The movement speed multiplier
     * @return The Fluid Builder for chaining
     */
    public FluidBuilder movementSpeedMultiplier(double movementSpeedMultiplier) {
        this.movementSpeedMultiplier = movementSpeedMultiplier;
        return this;
    }

    /**
     * <p> Sets the default behavior of if living entities will drown in this fluid.
     * <p> This won't have any effect if you override {@link Fluid#willDrown(LivingEntity)} in the Fluid class
     * <p> The default behavior is set to <code>true</code> and also checks {@link LivingEntity#canBreatheInWater()}
     *
     * @param willDrown If entities will drown in this fluid
     * @return The Fluid Builder for chaining
     */
    public FluidBuilder willDrown(boolean willDrown) {
        this.willDrown = willDrown;
        return this;
    }

    /**
     * <p> Sets the default light level of the fluid
     * <p> This won't have any effect if you override {@link Fluid#getLightLevel(BlockState)} in the Fluid class
     * <p> If this is not set, the fluid won't emit any light
     *
     * @param lightLevel The light level in the range of 0-15 to set the fluid to
     * @return The Fluid Builder for chaining
     */
    public FluidBuilder lightLevel(int lightLevel) {
        this.lightLevel = lightLevel;
        return this;
    }

    /**
     * <p> Sets the default color multiplier of the fluid used for fluid blocks in world
     * <p> This can also be written as a hex value in the format <code>0xRRGGBB</code>, for example <code>0xCCAA00</code>
     * <p> This won't have any effect if you override {@link Fluid#getColorMultiplier(BlockView, int, int, int)} in the Fluid class
     * <p> If this is not set, no color multiplier will be applied
     * 
     * @param colorMultiplier The color multiplier
     * @return The Fluid Builder for chaining
     */
    public FluidBuilder colorMultiplier(int colorMultiplier) {
        this.colorMultiplier = colorMultiplier;
        return this;
    }

    public Fluid build() {
        return this.build(Fluid::new);
    }

    /**
     * Builds the {@link Fluid}
     *
     * @return The built {@link Fluid}
     */
    public Fluid build(FluidFactory factory) {
        Fluid fluid;

        if (stillBlock != null && flowingBlock != null) {
            fluid = factory.create(identifier, stillBlock, flowingBlock);
        } else if (stillTexture != null && flowingTexture != null) {
            fluid = factory.create(identifier, null, null);

            // If no overlay texture was specified, use the still texture
            if (overlayTexture == null) {
                this.overlayTexture = stillTexture;
            }

            // If map color was not specified, use the default one
            if (mapColor == null) {
                this.mapColor = MapColorUtil.addMapColor(color);
            }

            FluidBlockManager.requestBlock(fluid, stillTexture, flowingTexture, overlayTexture, mapColor);
        } else {
            throw new IllegalStateException("Tried to build a Fluid that has not been initialized with blocks or textures");
        }

        // Fluid Color
        if (color != null) {
            fluid.setColor(color);
        } else {
            NyaLib.LOGGER.warn("Fluid " + identifier + " has no color set!");
        }

        // Bucket Size
        if (bucketSize != null) {
            fluid.setBucketSize(bucketSize);
        }

        // Bucket Item
        if (bucketItem != null) {
            fluid.setBucketItem(bucketItem);
        }

        // Placeable in World
        if (placeableInWorld != null) {
            fluid.setPlaceableInWorld(placeableInWorld);
        }

        // Automatic Bucket Registration
        if (automaticBucketRegistration == Boolean.FALSE) {
            fluid.disableAutomaticBucketRegistration();
        }

        // Fill Sound
        if (fillSound != null) {
            fluid.setFillSound(fillSound);
        }

        // Empty Sound
        if (emptySound != null) {
            fluid.setEmptySound(emptySound);
        }

        // Tick Rate
        if (tickRate != null) {
            fluid.setTickRate(tickRate);
        }

        // Can Swim In
        if (canSwimIn != null) {
            fluid.setCanSwimIn(canSwimIn);
        }

        // Movement Speed Multiplier
        if (movementSpeedMultiplier != null) {
            fluid.setMovementSpeedMultiplier(movementSpeedMultiplier);
        }

        // Will Drown
        if (willDrown != null) {
            fluid.setWillDrown(willDrown);
        }

        // Light Level
        if (lightLevel != null) {
            fluid.setLightLevel(lightLevel);
        }
        
        // Color Multiplier
        if (colorMultiplier != null) {
            fluid.setColorMultiplier(colorMultiplier);
        }

        return fluid;
    }

    public interface FluidFactory {
        Fluid create(Identifier identifier, Block still, Block flowing);
    }
}
