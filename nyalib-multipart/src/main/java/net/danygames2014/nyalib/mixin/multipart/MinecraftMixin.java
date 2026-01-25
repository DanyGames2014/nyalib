package net.danygames2014.nyalib.mixin.multipart;

import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.danygames2014.nyalib.multipart.MultipartHitResult;
import net.minecraft.client.InteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
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

    @Inject(method = "handleMouseClick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;crosshairTarget:Lnet/minecraft/util/hit/HitResult;", ordinal = 0, opcode = Opcodes.GETFIELD), cancellable = true)
    public void handleMultipartMouseClick(int button, CallbackInfo ci) {
        MultipartHitResult hitResult = Minecraft.INSTANCE.getMultipartCrosshairTarget();
        if (hitResult == null) {
            return;
        }

        int blockX = hitResult.blockX;
        int blockY = hitResult.blockY;
        int blockZ = hitResult.blockZ;
        Vec3d pos = hitResult.pos;
        Direction face = hitResult.face;
        MultipartComponent component = hitResult.component;
        
        if (button == 0) {
            this.interactionManager.attackMultipart(blockX, blockY, blockZ, pos, face, component);
        } else {
            ItemStack selectedStack = this.player.inventory.getSelectedItem();
            int lastCount = selectedStack != null ? selectedStack.count : 0;
            if (this.interactionManager.interactMultipart(selectedStack, blockX, blockY, blockZ, pos, face, component)) {
                this.player.swingHand();
            }

            if (selectedStack == null) {
                return;
            }

            if (selectedStack.count == 0) {
                this.player.inventory.main[this.player.inventory.selectedSlot] = null;
            } else if (selectedStack.count != lastCount) {
                this.gameRenderer.heldItemRenderer.place();
            }
        }

        ci.cancel();
    }

    @Inject(method = "handleMouseDown", at = @At(value = "HEAD"))
    public void handleMultipartMouseDown(int button, boolean holdingAttack, CallbackInfo ci) {

    }
}
