package net.danygames2014.nyalib.mixin.multipart;

import net.danygames2014.nyalib.mixininterface.MultipartInteractionManager;
import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.minecraft.client.InteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.SingleplayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SingleplayerInteractionManager.class)
public class SingleplayerInteractionManagerMixin extends InteractionManager implements MultipartInteractionManager {
    public SingleplayerInteractionManagerMixin(Minecraft minecraft) {
        super(minecraft);
    }

    @Override
    public void attackMultipart(int blockX, int blockY, int blockZ, Vec3d pos, Direction face, MultipartComponent component) {
        component.state.components.remove(component);
        component.state.markDirty();
    }

    @Override
    public boolean interactMultipart(ItemStack stack, int blockX, int blockY, int blockZ, Vec3d pos, Direction face, MultipartComponent component) {
        return false;
    }
}
