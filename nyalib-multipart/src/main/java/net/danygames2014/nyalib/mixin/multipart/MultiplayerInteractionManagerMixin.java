package net.danygames2014.nyalib.mixin.multipart;

import net.danygames2014.nyalib.mixininterface.MultipartInteractionManager;
import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.minecraft.client.InteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MultiplayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MultiplayerInteractionManager.class)
public class MultiplayerInteractionManagerMixin extends InteractionManager implements MultipartInteractionManager {
    public MultiplayerInteractionManagerMixin(Minecraft minecraft) {
        super(minecraft);
    }

    @Override
    public void attackMultipart(int blockX, int blockY, int blockZ, Vec3d pos, Direction face, MultipartComponent component) {
        
    }

    @Override
    public boolean interactMultipart(ItemStack stack, int blockX, int blockY, int blockZ, Vec3d pos, Direction face, MultipartComponent component) {
        return false;
    }
}
