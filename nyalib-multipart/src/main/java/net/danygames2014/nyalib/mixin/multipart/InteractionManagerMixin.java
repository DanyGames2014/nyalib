package net.danygames2014.nyalib.mixin.multipart;

import net.danygames2014.nyalib.mixininterface.MultipartInteractionManager;
import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.minecraft.client.InteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(InteractionManager.class)
public class InteractionManagerMixin implements MultipartInteractionManager {
    @Shadow
    @Final
    protected Minecraft minecraft;
    @Unique
    protected MultipartComponent currentlyBrokenComponent;
    @Unique
    protected float multipartBreakingProgress = 0.0F;
    @Unique
    protected float lastMultipartBreakingProgress = 0.0F;
    @Unique
    protected float multipartBreakingSoundDelayTicks = 0.0F;
    @Unique
    protected int multipartBreakingDelayTicks = 0;
    
    @Override
    public void attackMultipart(ItemStack selectedStack, int x, int y, int z, Vec3d pos, Direction face, MultipartComponent component) {
    }

    @Override
    public boolean interactMultipart(ItemStack stack, int x, int y, int z, Vec3d pos, Direction face, MultipartComponent component) {
        return false;
    }

    @Override
    public void processMultipartBreakingAction(int x, int y, int z, Vec3d pos, Direction face, MultipartComponent component) {
        
    }

    @Override
    public void cancelMultipartBreaking() {
        
    }
}
