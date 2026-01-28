package net.danygames2014.nyalib.mixin.multipart;

import net.danygames2014.nyalib.item.EnhancedPlacementContextItem;
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
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(Item.class)
public abstract class ItemMixin implements MultipartAwareItem {
    @Shadow
    public abstract boolean useOnBlock(ItemStack stack, PlayerEntity user, World world, int x, int y, int z, int side);

    @Override
    public boolean useOnMultipart(ItemStack stack, PlayerEntity player, World world, int x, int y, int z, Direction face, Vec3d hitPos, MultipartComponent component) {
        if (Item.class.cast(this) instanceof EnhancedPlacementContextItem enhancedPlacementContextItem) {
            return enhancedPlacementContextItem.useOnBlock(stack, player, world, x, y, z, face.getId(), hitPos);
        }
        return useOnBlock(stack, player, world, x, y, z, face.getId());
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
