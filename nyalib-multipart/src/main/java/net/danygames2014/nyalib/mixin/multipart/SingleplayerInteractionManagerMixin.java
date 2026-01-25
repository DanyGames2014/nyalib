package net.danygames2014.nyalib.mixin.multipart;

import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.danygames2014.nyalib.multipart.MultipartState;
import net.minecraft.client.SingleplayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(SingleplayerInteractionManager.class)
public class SingleplayerInteractionManagerMixin extends InteractionManagerMixin {
    @Override
    public void attackMultipart(ItemStack selectedStack, int x, int y, int z, Vec3d pos, Direction face, MultipartComponent component) {
        
    }

    @Override
    public boolean interactMultipart(ItemStack stack, int x, int y, int z, Vec3d pos, Direction face, MultipartComponent component) {
        return false;
    }

    @Override
    public void processMultipartBreakingAction(int x, int y, int z, Vec3d pos, Direction face, MultipartComponent component) {
        if (this.multipartBreakingDelayTicks > 0) {
            --this.multipartBreakingDelayTicks;
            return;
        }

        if (component == null) {
            cancelMultipartBreaking();
            return;
        }
        
        if (component == currentlyBrokenComponent) {
            this.multipartBreakingProgress += component.getHardness(this.minecraft.player);
            if (this.multipartBreakingSoundDelayTicks % 4.0F == 0.0F) {
                this.minecraft.soundManager.playSound(component.getSoundGroup().getSound(), (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, (component.getSoundGroup().getVolume() + 1.0F) / 8.0F, component.getSoundGroup().getPitch() * 0.5F);
            }

            this.multipartBreakingSoundDelayTicks++;
            if (this.multipartBreakingProgress >= 1.0F) {
                this.breakMultipart(x, y, z, component);
                this.multipartBreakingProgress = 0.0F;
                this.lastMultipartBreakingProgress = 0.0F;
                this.multipartBreakingSoundDelayTicks = 0.0F;
                this.multipartBreakingDelayTicks = 5;
            }
        } else {
            this.multipartBreakingProgress = 0.0F;
            this.lastMultipartBreakingProgress = 0.0F;
            this.multipartBreakingSoundDelayTicks = 0.0F;
            this.currentlyBrokenComponent = component;
        }

        this.minecraft.worldRenderer.miningProgress = this.multipartBreakingProgress;
    }
    
    @Unique
    public void breakMultipart(int x, int y, int z, MultipartComponent component) {
        MultipartState state = this.minecraft.world.getMultipartState(x,y,z);
        if (state != null) {
            state.components.remove(component);
            state.markDirty();
        }
    }

    @Override
    public void cancelMultipartBreaking() {
        multipartBreakingProgress = 0.0F;
        multipartBreakingDelayTicks = 0;
        currentlyBrokenComponent = null;
        this.minecraft.worldRenderer.miningProgress = this.multipartBreakingProgress;
    }
}
