package net.danygames2014.nyalib.mixin.multipart;

import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.danygames2014.nyalib.multipart.MultipartHitResult;
import net.minecraft.client.InteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow
    private int attackCooldown;

    @Shadow
    public HitResult crosshairTarget;

    @Shadow
    public ClientPlayerEntity player;

    @Shadow
    public InteractionManager interactionManager;

    @Shadow
    public World world;

    @Shadow
    public GameRenderer gameRenderer;

    @Shadow
    public ParticleManager particleManager;

    @Shadow
    public WorldRenderer worldRenderer;

    @Inject(method = "handleMouseClick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;crosshairTarget:Lnet/minecraft/util/hit/HitResult;", ordinal = 0, opcode = Opcodes.GETFIELD), cancellable = true)
    public void handleMultipartMouseClick(int button, CallbackInfo ci) {
        // Check if there is a multipart hit result
        MultipartHitResult hitResult = Minecraft.INSTANCE.getMultipartCrosshairTarget();
        if (hitResult == null) {
            return;
        }

        // Get the hit result information
        int blockX = hitResult.blockX;
        int blockY = hitResult.blockY;
        int blockZ = hitResult.blockZ;
        Vec3d pos = hitResult.pos;
        Direction face = hitResult.face;
        MultipartComponent component = hitResult.component;

        // Get the selected stack
        ItemStack selectedStack = this.player.inventory.getSelectedItem();
        int lastCount = selectedStack != null ? selectedStack.count : 0;
        
        // Process the action
        if (button == 0) {
            this.interactionManager.attackMultipart(selectedStack, blockX, blockY, blockZ, pos, face, component);
        } else {
            if (this.interactionManager.interactMultipart(selectedStack, blockX, blockY, blockZ, pos, face, component)) {
                this.player.swingHand();
            }
        }

        // Check the changes on the selected stack
        if (selectedStack != null) {
            if (selectedStack.count == 0) {
                this.player.inventory.main[this.player.inventory.selectedSlot] = null;
            } else if (selectedStack.count != lastCount) {
                this.gameRenderer.heldItemRenderer.place();
            }
        }

        // Prevent the vanilla code from running
        ci.cancel();
    }

    @Inject(method = "handleMouseDown", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;crosshairTarget:Lnet/minecraft/util/hit/HitResult;", ordinal = 0, opcode = Opcodes.GETFIELD), cancellable = true)
    public void handleMultipartMouseDown(int button, boolean holdingAttack, CallbackInfo ci) {
        // Check if there is a multipart hit result
        MultipartHitResult hitResult = Minecraft.INSTANCE.getMultipartCrosshairTarget();
        if (hitResult == null) {
            return;
        }

        // Get the hit result information
        int blockX = hitResult.blockX;
        int blockY = hitResult.blockY;
        int blockZ = hitResult.blockZ;
        Vec3d pos = hitResult.pos;
        Direction face = hitResult.face;
        MultipartComponent component = hitResult.component;

        // Process the action
        if (holdingAttack && button == 0) {
            this.interactionManager.processMultipartBreakingAction(blockX, blockY, blockZ, pos, face, component);
            // TODO: multipart breaking particles
            //this.particleManager.addBlockBreakingParticles(blockX, blockY, blockZ, face.getId());
        } else {
            this.interactionManager.cancelMultipartBreaking(true);
        }

        // Prevent the vanilla code from running
        ci.cancel();
    }
}
