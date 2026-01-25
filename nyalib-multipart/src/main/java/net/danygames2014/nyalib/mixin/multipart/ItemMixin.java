package net.danygames2014.nyalib.mixin.multipart;

import net.danygames2014.nyalib.mixininterface.MultipartAwareItem;
import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(Item.class)
public class ItemMixin implements MultipartAwareItem {
    @Override
    public boolean useOnMultipart(ItemStack stack, PlayerEntity player, World world, int x, int y, int z, Direction face, Vec3d hitPos, MultipartComponent component) {
        return false;
    }

    @Override
    public float getMultipartMiningSpeedMultiplier(ItemStack stack, PlayerEntity player, World world, int x, int y, int z, MultipartComponent component) {
        return 1.0F;
    }

    @Override
    public boolean postMineMultipart(ItemStack stack, LivingEntity entity, World world, int x, int y, int z, MultipartComponent component) {
        return false;
    }

    @Override
    public boolean isSuitableForMultipart(ItemStack stack, PlayerEntity player, World world, int x, int y, int z, MultipartComponent component) {
        return false;
    }
}
