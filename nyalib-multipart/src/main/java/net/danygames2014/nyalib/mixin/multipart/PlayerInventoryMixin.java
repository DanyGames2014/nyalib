package net.danygames2014.nyalib.mixin.multipart;

import net.danygames2014.nyalib.mixininterface.MultipartPlayerInventory;
import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin implements MultipartPlayerInventory {
    @Shadow
    public ItemStack[] main;

    @Shadow
    public int selectedSlot;

    @Shadow
    public PlayerEntity player;

    @Shadow
    public abstract ItemStack getStack(int slot);

    @Override
    public float getStrengthOnMultipart(int x, int y, int z, MultipartComponent component) {
        float strength = 1.0F;
        if (this.main[this.selectedSlot] != null) {
            strength *= this.main[this.selectedSlot].getMultipartMiningSpeedMultiplier(this.player, this.player.world, x, y, z, component);
        }

        return strength;
    }

    @Override
    public boolean isUsingEffectiveToolOnMultipart(int x, int y, int z, MultipartComponent component) {
        if (component.isHandHarvestable()) {
            return true;
        } else {
            ItemStack selectedStack = this.getStack(this.selectedSlot);
            return selectedStack != null && selectedStack.isSuitableForMultipart(this.player, this.player.world, x, y, z, component);
        }
    }
}
