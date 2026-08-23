package net.danygames2014.nyalib.abilities.mixin.impl.fly;

import net.danygames2014.nyalib.abilities.network.PlayerFlyingStateC2SPacket;
import net.minecraft.client.input.Input;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends PlayerEntity {
    @Shadow
    public Input input;

    @Unique
    private int pressedJumpTwiceTimer = 0;
    
    @Unique
    private boolean wasJumping = false;

    public ClientPlayerEntityMixin(World world) {
        super(world);
    }

    @Inject(method = "tickLiving", at = @At("TAIL"))
    public void reducePressedJumpTwiceTimer(CallbackInfo ci) {
        if (this.pressedJumpTwiceTimer > 0) {
            --this.pressedJumpTwiceTimer;
        }
    }
    
    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/input/Input;update(Lnet/minecraft/entity/player/PlayerEntity;)V"))
    public void getPreviousJumpingState(CallbackInfo ci) {
        wasJumping = this.input.jumping;
    }
    
    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;tickMovement()V"))
    public void engageFlight(CallbackInfo ci) {
        if (this.nyalib$canFly() && !wasJumping && this.input.jumping) {
            if (this.pressedJumpTwiceTimer == 0) {
                this.pressedJumpTwiceTimer = 7;
            } else {
                this.nyalib$setFlying(!this.nyalib$isFlying());
                if (world.isRemote) {
                    PacketHelper.send(new PlayerFlyingStateC2SPacket(this.nyalib$isFlying()));
                }
                this.pressedJumpTwiceTimer = 0;
            }
        }

        if (this.nyalib$isFlying()) {
            if (this.input.sneaking) {
                this.velocityY -= 0.15;
            }

            if (this.input.jumping) {
                this.velocityY += 0.15;
            }
        }
    }
}
