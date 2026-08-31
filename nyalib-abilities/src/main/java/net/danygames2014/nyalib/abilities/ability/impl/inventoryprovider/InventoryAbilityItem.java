package net.danygames2014.nyalib.abilities.ability.impl.inventoryprovider;

import net.danygames2014.nyalib.abilities.ability.Ability;
import net.danygames2014.nyalib.abilities.ability.value.AbilityValue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

/**
 * An {@link net.minecraft.item.Item} which if in a player inventory can provide abilities to the player
 */
public interface InventoryAbilityItem {
    /**
     * Called when the player inventory contents change to query which abilities this item can provide
     *
     * @param player   The player which has the item in their inventory
     * @param slotType The slot type the item is currently in
     * @return The abilities this item can provide
     */
    Ability<?, ?>[] getProvidedAbilities(PlayerEntity player, InventoryAbilityItemSlot slotType);

    /**
     * Called on every tick to query the current value of every ability this item provides
     *
     * @param ability         The ability currently being queried
     * @param player          The player which has the item in their inventory
     * @param playerInventory The player inventory
     * @param stack           The {@link net.minecraft.item.ItemStack} of this {@link net.minecraft.item.Item}
     * @param slotType        The slot type the item is currently in
     * @param slot            The slot index this item is in
     * @return The value of the ability to provide
     */
    AbilityValue<?> getAbilityValue(Ability<?, ?> ability, PlayerEntity player, PlayerInventory playerInventory, ItemStack stack, InventoryAbilityItemSlot slotType, int slot);
}
