package net.danygames2014.nyalib.mixin.multipart;

import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.danygames2014.nyalib.multipart.MultipartState;
import net.minecraft.client.SingleplayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(SingleplayerInteractionManager.class)
public abstract class SingleplayerInteractionManagerMixin extends InteractionManagerMixin {
    @Shadow
    public abstract void update(float f);

    @Shadow
    private float blockBreakingProgress;

    @Shadow
    private float lastBlockBreakingProgress;

    @Shadow
    private int breakingDelayTicks;

    @Shadow
    private float breakingSoundDelayTicks;

    @Shadow
    private int breakingPosX;

    @Shadow
    private int breakingPosY;

    @Shadow
    private int breakingPosZ;

    @Override
    public void attackMultipart(ItemStack selectedStack, int x, int y, int z, Vec3d pos, Direction face, MultipartComponent component) {
        MultipartState state = this.minecraft.world.getMultipartState(x,y,z);
        
        if (state == null) {
            return;
        }
        
        if (this.blockBreakingProgress == 0.0F) {
            component.onBreakStart();
        }

        if (component.getHardness(this.minecraft.player) >= 1.0F) {
            this.breakMultipart(x, y, z, component);
        }
    }

    @Override
    public boolean interactMultipart(ItemStack stack, int x, int y, int z, Vec3d pos, Direction face, MultipartComponent component) {
        return false;
    }

    @Override
    public void processMultipartBreakingAction(int x, int y, int z, Vec3d pos, Direction face, MultipartComponent component) {
        if (this.breakingDelayTicks > 0) {
            this.breakingDelayTicks--;
            return;
        }

        if (x != this.breakingPosX || y != this.breakingPosY || z != this.breakingPosZ) {
            this.breakingPosX = x;
            this.breakingPosY = y;
            this.breakingPosZ = z;
            cancelMultipartBreaking(false);
        }

        if (component == null) {
            cancelMultipartBreaking(true);
            return;
        }
        
        if (component == currentlyBrokenComponent) {
            this.blockBreakingProgress += component.getHardness(this.minecraft.player);
            if (this.breakingSoundDelayTicks % 4.0F == 0.0F) {
                this.minecraft.soundManager.playSound(component.getSoundGroup().getSound(), (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, (component.getSoundGroup().getVolume() + 1.0F) / 8.0F, component.getSoundGroup().getPitch() * 0.5F);
            }

            this.breakingSoundDelayTicks++;
            if (this.blockBreakingProgress >= 1.0F) {
                this.breakMultipart(x, y, z, component);
                this.blockBreakingProgress = 0.0F;
                this.lastBlockBreakingProgress = 0.0F;
                this.breakingSoundDelayTicks = 0.0F;
                this.breakingDelayTicks = 5;
            }
        } else {
            this.blockBreakingProgress = 0.0F;
            this.lastBlockBreakingProgress = 0.0F;
            this.breakingSoundDelayTicks = 0.0F;
            this.currentlyBrokenComponent = component;
        }

        update(this.blockBreakingProgress);
    }
    
    @Override
    public void cancelMultipartBreaking(boolean resetComponent) {
        blockBreakingProgress = 0.0F;
        lastBlockBreakingProgress = 0.0F;
        breakingDelayTicks = 0;
        if (resetComponent) {
            currentlyBrokenComponent = null;
        }
        update(blockBreakingProgress);
    }
}
