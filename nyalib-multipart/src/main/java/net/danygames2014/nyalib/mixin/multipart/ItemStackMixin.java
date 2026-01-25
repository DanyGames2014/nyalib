package net.danygames2014.nyalib.mixin.multipart;

import net.danygames2014.nyalib.mixininterface.MultipartAwareItemStack;
import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements MultipartAwareItemStack {
    @Shadow
    public abstract Item getItem();

    @Shadow
    public int itemId;

    @Override
    public boolean useOnMultipart(PlayerEntity player, World world, int x, int y, int z, Direction face, Vec3d hitPos, MultipartComponent component) {
        boolean result = this.getItem().useOnMultipart(ItemStack.class.cast(this), player, world, x, y, z, face, hitPos, component);

        if (result) {
            player.increaseStat(Stats.USED[this.itemId], 1);
        }

        return result;
    }

    @Override
    public float getMultipartMiningSpeedMultiplier(PlayerEntity player, World world, int x, int y, int z, MultipartComponent component) {
        return this.getItem().getMultipartMiningSpeedMultiplier(ItemStack.class.cast(this), player, world, x, y, z, component);
    }

    @Override
    public void postMineMultipart(LivingEntity entity, World world, int x, int y, int z, MultipartComponent component) {
        boolean result = this.getItem().postMineMultipart(ItemStack.class.cast(this), entity, world, x, y, z, component);

        if (result && entity instanceof PlayerEntity player) {
            player.increaseStat(Stats.USED[this.itemId], 1);
        }
    }

    @Override
    public boolean isSuitableForMultipart(PlayerEntity player, World world, int x, int y, int z, MultipartComponent component) {
        return this.getItem().isSuitableForMultipart(ItemStack.class.cast(this), player, world, x, y, z, component);
    }
}
