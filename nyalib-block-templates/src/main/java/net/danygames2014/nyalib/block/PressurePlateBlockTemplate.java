package net.danygames2014.nyalib.block;

import net.danygames2014.nyalib.sound.SoundHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.PressurePlateActivationRule;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.Properties;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.world.BlockStateView;

import java.util.List;
import java.util.Random;

public class PressurePlateBlockTemplate extends TemplateBlock {
    public final PressurePlateActivationRule activationRule;

    public PressurePlateBlockTemplate(Identifier identifier, Block baseBlock, PressurePlateActivationRule activationRule, Material material, Identifier texture) {
        super(identifier, material);
        this.activationRule = activationRule;
        this.setTickRandomly(true);
        this.setBoundingBox(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.03125F, 0.9375F);

        if (texture != null) {
            TemplateBlockRegistry.registerPressurePlate(identifier, texture);
        }
    }

    public PressurePlateBlockTemplate(Identifier identifier, Block baseBlock, PressurePlateActivationRule activationRule, Identifier texture) {
        this(identifier, baseBlock, activationRule, baseBlock.material, texture);
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(Properties.POWERED);
    }

    public int getTickRate() {
        return 20;
    }

    public int getPistonBehavior() {
        return 1;
    }

    // Placement
    public boolean canPlaceAt(World world, int x, int y, int z) {
        return world.shouldSuffocate(x, y - 1, z);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return super.getPlacementState(context).with(Properties.POWERED, false);
    }

    // LogIC
    public void onPlaced(World world, int x, int y, int z) {
    }

    public void neighborUpdate(World world, int x, int y, int z, int id) {
        if (!world.shouldSuffocate(x, y - 1, z)) {
            this.dropStacks(world, x, y, z, world.getBlockMeta(x, y, z));
            world.setBlockState(x, y, z, States.AIR.get());
        }
    }

    public void onTick(World world, int x, int y, int z, Random random) {
        if (!world.isRemote) {
            if (isPressed(world, x, y, z)) {
                this.updatePlateState(world, x, y, z);
            }
        }
    }

    public void onBreak(World world, int x, int y, int z) {
        if (isPressed(world, x, y, z)) {
            world.notifyNeighbors(x, y, z, this.id);
            world.notifyNeighbors(x, y - 1, z, this.id);
        }

        super.onBreak(world, x, y, z);
    }

    public void onEntityCollision(World world, int x, int y, int z, Entity entity) {
        if (!world.isRemote) {
            if (!isPressed(world, x, y, z)) {
                this.updatePlateState(world, x, y, z);
            }
        }
    }

    private void updatePlateState(World world, int x, int y, int z) {
        @SuppressWarnings("rawtypes")
        List foundEntities = null;

        switch (this.activationRule) {
            case EVERYTHING -> {
                foundEntities = world.getEntities(null, Box.createCached((float) x + 0.125F, y, (float) z + 0.125F, (float) (x + 1) - 0.125F, y + 0.25F, (float) (z + 1) - 0.125F));
            }
            case MOBS -> {
                foundEntities = world.collectEntitiesByClass(LivingEntity.class, Box.createCached((float) x + 0.125F, y, (float) z + 0.125F, (float) (x + 1) - 0.125F, y + 0.25F, (float) (z + 1) - 0.125F));
            }
            case PLAYERS -> {
                foundEntities = world.collectEntitiesByClass(PlayerEntity.class, Box.createCached((float) x + 0.125F, y, (float) z + 0.125F, (float) (x + 1) - 0.125F, y + 0.25F, (float) (z + 1) - 0.125F));
            }
        }

        boolean pressed = isPressed(world, x, y, z);
        boolean foundEntity = !foundEntities.isEmpty();
        BlockState state = world.getBlockState(x, y, z);

        if (foundEntity && !pressed) {
            world.setBlockStateWithNotify(x, y, z, state.with(Properties.POWERED, true));
            world.notifyNeighbors(x, y, z, this.id);
            world.notifyNeighbors(x, y - 1, z, this.id);
            SoundHelper.playSound(world, x + 0.5D, y + 0.1D, z + 0.5D, "random.click", 0.3F, 0.6F);
        }

        if (!foundEntity && pressed) {
            world.setBlockStateWithNotify(x, y, z, state.with(Properties.POWERED, false));
            world.notifyNeighbors(x, y, z, this.id);
            world.notifyNeighbors(x, y - 1, z, this.id);
            SoundHelper.playSound(world, x + 0.5D, y + 0.1D, z + 0.5D, "random.click", 0.3F, 0.5F);
        }

        if (foundEntity) {
            world.scheduleBlockUpdate(x, y, z, this.id, this.getTickRate());
        }

    }

    // Collision & Bounding Box
    public Box getCollisionShape(World world, int x, int y, int z) {
        return null;
    }

    public void updateBoundingBox(BlockView blockView, int x, int y, int z) {
        if (isPressed(blockView, x, y, z)) {
            this.setBoundingBox(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.03125F, 0.9375F);
        } else {
            this.setBoundingBox(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.0625F, 0.9375F);
        }
    }

    // Redstone Behavior
    public boolean isEmittingRedstonePowerInDirection(BlockView blockView, int x, int y, int z, int direction) {
        return isPressed(blockView, x, y, z);
    }

    public boolean canTransferPowerInDirection(World world, int x, int y, int z, int direction) {
        return isPressed(world, x, y, z) && direction == 1;
    }

    public boolean canEmitRedstonePower() {
        return true;
    }

    // Rendering
    public boolean isOpaque() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    @Environment(EnvType.CLIENT)
    public void setupRenderBoundingBox() {
        float var1 = 0.5F;
        float var2 = 0.125F;
        float var3 = 0.5F;
        this.setBoundingBox(0.5F - var1, 0.5F - var2, 0.5F - var3, 0.5F + var1, 0.5F + var2, 0.5F + var3);
    }

    // Utility
    public boolean isPressed(BlockView blockView, int x, int y, int z) {
        if (blockView instanceof BlockStateView stateView) {
            BlockState state = stateView.getBlockState(x, y, z);

            if (!(state.getBlock() instanceof PressurePlateBlockTemplate)) {
                return false;
            }

            return state.get(Properties.POWERED);
        }

        return false;
    }
}
