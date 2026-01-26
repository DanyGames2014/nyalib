package net.danygames2014.nyalib.mixin.multipart;

import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.danygames2014.nyalib.multipart.MultipartState;
import net.danygames2014.nyalib.packet.AttackMultipartC2SPacket;
import net.danygames2014.nyalib.packet.BreakMultipartC2SPacket;
import net.minecraft.client.MultiplayerInteractionManager;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(MultiplayerInteractionManager.class)
public abstract class MultiplayerInteractionManagerMixin extends InteractionManagerMixin {
    @Shadow
    protected abstract void updateSelectedSlot();

    @Shadow
    private float blockBreakingProgress;
    @Shadow
    private float lastBlockBreakingProgress;

    @Shadow
    public abstract void update(float f);

    @Shadow
    private int breakingDelayTicks;
    @Shadow
    private float breakingSoundDelayTicks;
    @Shadow
    private ClientNetworkHandler networkHandler;
    @Shadow
    private int breakingPosX;
    @Shadow
    private int breakingPosY;
    @Shadow
    private int breakingPosZ;
    @Unique
    private boolean breakingMultipart = false;

    @Override
    public void attackMultipart(ItemStack selectedStack, int x, int y, int z, Vec3d pos, Direction face, MultipartComponent component) {
        MultipartState state = this.minecraft.world.getMultipartState(x, y, z);

        if (state == null) {
            return;
        }

        if (!this.breakingMultipart || component != this.currentlyBrokenComponent) {
            this.networkHandler.sendPacket(new AttackMultipartC2SPacket(x, y, z, component));
            if (this.blockBreakingProgress == 0.0F) {
                component.onBreakStart();
            }

            if (component.getHardness(this.minecraft.player) >= 1.0F) {
                this.breakMultipart(x, y, z, component);
            } else {
                this.breakingMultipart = true;
                this.currentlyBrokenComponent = component;
                this.blockBreakingProgress = 0.0F;
                this.lastBlockBreakingProgress = 0.0F;
                this.breakingSoundDelayTicks = 0.0F;
            }
        }
    }

    @Override
    public boolean interactMultipart(ItemStack stack, int x, int y, int z, Vec3d pos, Direction face, MultipartComponent component) {
        this.updateSelectedSlot();
        // TODO: Reimplement PlayerInteractBlockC2SPacket for multiparts 😭😭😭😭😭😭😭😭😭😭😭😭😭😭😭😭
        //this.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(x, y, z, side, player.inventory.getSelectedItem()));
        return super.interactMultipart(stack, x, y, z, pos, face, component);
    }

    @Override
    public void processMultipartBreakingAction(int x, int y, int z, Vec3d pos, Direction face, MultipartComponent component) {
        if (!this.breakingMultipart) {
            return;
        }

        this.updateSelectedSlot();
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

        if (component == currentlyBrokenComponent) {
            MultipartState state = this.minecraft.world.getMultipartState(x, y, z);
            if (state == null) {
                this.breakingMultipart = false;
                return;
            }

            this.blockBreakingProgress += component.getHardness(this.minecraft.player);
            if (this.breakingSoundDelayTicks % 4.0F == 0.0F) {
                this.minecraft.soundManager.playSound(component.getSoundGroup().getSound(), (float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F, (component.getSoundGroup().getVolume() + 1.0F) / 8.0F, component.getSoundGroup().getPitch() * 0.5F);
            }

            this.breakingSoundDelayTicks++;
            if (this.blockBreakingProgress >= 1.0F) {
                this.breakingMultipart = false;
                this.networkHandler.sendPacket(new BreakMultipartC2SPacket(x, y, z, component));
                this.breakMultipart(x, y, z, component);
                this.blockBreakingProgress = 0.0F;
                this.lastBlockBreakingProgress = 0.0F;
                this.breakingSoundDelayTicks = 0.0F;
                this.breakingDelayTicks = 5;
            }
        } else {
            this.attackMultipart(this.minecraft.player.getHand(), x, y, z, pos, face, component);
        }
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
