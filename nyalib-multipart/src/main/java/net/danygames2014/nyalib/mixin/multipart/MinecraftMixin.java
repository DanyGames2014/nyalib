package net.danygames2014.nyalib.mixin.multipart;

import net.danygames2014.nyalib.mixininterface.MultipartMinecraft;
import net.danygames2014.nyalib.multipart.MultipartHitResult;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Minecraft.class)
public class MinecraftMixin implements MultipartMinecraft {
    @Unique
    MultipartHitResult multipartCrosshairTarget;

    @Override
    public MultipartHitResult getMultipartCrosshairTarget() {
        return multipartCrosshairTarget;
    }

    @Override
    public void setMultipartCrosshairTarget(MultipartHitResult hitResult) {
        this.multipartCrosshairTarget = hitResult;
    }
}
