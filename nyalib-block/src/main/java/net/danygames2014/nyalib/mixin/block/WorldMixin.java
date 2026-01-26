package net.danygames2014.nyalib.mixin.block;

import net.danygames2014.nyalib.block.RedstoneLevelProvider;
import net.danygames2014.nyalib.mixininterface.RedstoneLevelWorld;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.world.StationFlatteningWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(World.class)
public abstract class WorldMixin implements RedstoneLevelWorld, StationFlatteningWorld {
    @Shadow
    public abstract int getBlockId(int x, int y, int z);

    @Shadow
    public abstract boolean shouldSuffocate(int x, int y, int z);

    @Override
    public int getStrongPowerLevelOnSide(int x, int y, int z, int side) {
        int blockId = this.getBlockId(x, y, z);

        if (blockId == 0) {
            return 0;
        }

        Block block = Block.BLOCKS[blockId];

        if (block instanceof RedstoneLevelProvider redstoneLevelProvider) {
            return redstoneLevelProvider.getSideStrongPowerLevel(World.class.cast(this), x, y, z, side);
        } else {
            return block.isStrongPoweringSide(World.class.cast(this), x, y, z, side) ? 15 : 0;
        }
    }

    @Inject(method = "isStrongPoweringSide", at = @At(value = "HEAD"), cancellable = true)
    public void adaptIsStrongPoweringSide(int x, int y, int z, int side, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.getStrongPowerLevelOnSide(x, y, z, side) > 0);
    }

    @Override
    public int getStrongPowerLevel(int x, int y, int z) {
        int highestLevel = 0;
        int level;

        level = this.getStrongPowerLevelOnSide(x, y - 1, z, 0);
        if (level > highestLevel) {
            highestLevel = level;
        }

        level = this.getStrongPowerLevelOnSide(x, y + 1, z, 1);
        if (level > highestLevel) {
            highestLevel = level;
        }

        level = this.getStrongPowerLevelOnSide(x, y, z - 1, 2);
        if (level > highestLevel) {
            highestLevel = level;
        }

        level = this.getStrongPowerLevelOnSide(x, y, z + 1, 3);
        if (level > highestLevel) {
            highestLevel = level;
        }

        level = this.getStrongPowerLevelOnSide(x - 1, y, z, 4);
        if (level > highestLevel) {
            highestLevel = level;
        }

        level = this.getStrongPowerLevelOnSide(x + 1, y, z, 5);
        if (level > highestLevel) {
            highestLevel = level;
        }

        return highestLevel;
    }

    @Inject(method = "isStrongPowered", at = @At(value = "HEAD"), cancellable = true)
    public void adaptIsStrongPowered(int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.getStrongPowerLevel(x, y, z) > 0);
    }

    @Override
    public int getPowerLevelOnSide(int x, int y, int z, int side) {
        int powerLevel = 0;

        if (this.shouldSuffocate(x, y, z)) {
            powerLevel = this.getStrongPowerLevel(x, y, z);
        }

        int blockId = this.getBlockId(x, y, z);

        if (blockId == 0) {
            return 0;
        }

        Block block = Block.BLOCKS[blockId];

        if (block instanceof RedstoneLevelProvider redstoneLevelProvider) {
            int level = redstoneLevelProvider.getSidePowerLevel(World.class.cast(this), x, y, z, side);
            if (level > powerLevel) {
                powerLevel = level;
            }
        } else {
            int level = block.isPoweringSide(World.class.cast(this), x, y, z, side) ? 15 : 0;
            if (level > powerLevel) {
                powerLevel = level;
            }
        }
        
        return powerLevel;
    }

    @Inject(method = "isPoweringSide", at = @At(value = "HEAD"), cancellable = true)
    public void adaptIsPoweringSide(int x, int y, int z, int side, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.getPowerLevelOnSide(x, y, z, side) > 0);
    }

    @Override
    public int getPowerLevel(int x, int y, int z) {
        int highestLevel = 0;
        int level;

        level = this.getPowerLevelOnSide(x, y - 1, z, 0);
        if (level > highestLevel) {
            highestLevel = level;
        }

        level = this.getPowerLevelOnSide(x, y + 1, z, 1);
        if (level > highestLevel) {
            highestLevel = level;
        }

        level = this.getPowerLevelOnSide(x, y, z - 1, 2);
        if (level > highestLevel) {
            highestLevel = level;
        }

        level = this.getPowerLevelOnSide(x, y, z + 1, 3);
        if (level > highestLevel) {
            highestLevel = level;
        }

        level = this.getPowerLevelOnSide(x - 1, y, z, 4);
        if (level > highestLevel) {
            highestLevel = level;
        }

        level = this.getPowerLevelOnSide(x + 1, y, z, 5);
        if (level > highestLevel) {
            highestLevel = level;
        }

        return highestLevel;
    }

    @Inject(method = "isPowered", at = @At(value = "HEAD"), cancellable = true)
    public void adaptIsPowered(int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.getPowerLevel(x, y, z) > 0);
    }
}
