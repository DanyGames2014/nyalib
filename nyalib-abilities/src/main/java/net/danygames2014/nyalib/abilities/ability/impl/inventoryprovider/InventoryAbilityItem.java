package net.danygames2014.nyalib.abilities.ability.impl.inventoryprovider;

import net.danygames2014.nyalib.abilities.ability.Ability;
import net.danygames2014.nyalib.abilities.ability.value.AbilityValue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

public interface InventoryAbilityItem {
    Ability<?, ?>[] getProvidedAbilities(PlayerEntity player, InventoryAbilityItemSlot slotType);

    AbilityValue<?> getAbilityValue(Ability<?, ?> ability, PlayerEntity player, PlayerInventory playerInventory, ItemStack stack, InventoryAbilityItemSlot slotType, int slot);
}
