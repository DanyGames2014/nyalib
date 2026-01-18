package net.danygames2014.nyalib.mixin.item;

import net.danygames2014.nyalib.inventory.slot.LockableSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Slot.class)
public abstract class SlotMixin implements LockableSlot {
    @Shadow
    @Final
    private int index;

    @Shadow
    public abstract ItemStack getStack();

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public boolean isLocked(int index, ItemStack stackInSlot, PlayerEntity player) {
        return false;
    }
}
