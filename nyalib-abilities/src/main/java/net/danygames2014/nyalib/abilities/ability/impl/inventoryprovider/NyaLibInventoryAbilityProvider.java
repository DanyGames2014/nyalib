package net.danygames2014.nyalib.abilities.ability.impl.inventoryprovider;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.abilities.ability.Ability;
import net.danygames2014.nyalib.abilities.ability.MultipleValueAbilityProvider;
import net.danygames2014.nyalib.abilities.ability.value.AbilityValue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Map;

/**
 * A wrapper for {@link net.danygames2014.nyalib.abilities.ability.AbilityProvider} which provides abilities from {@link InventoryAbilityItem}s
 * This is used internally by NyaLib to service the {@link InventoryAbilityItem}s
 */
public class NyaLibInventoryAbilityProvider {
    private final PlayerEntity player;
    private final PlayerInventory playerInventory;
    private final MultipleValueAbilityProvider abilityProvider;

    /**
     * Holds all of the {@link InventoryAbilityItem}s that are currently being tracked by this provider
     */
    private final ObjectArrayList<TrackedItemEntry> trackedItems = new ObjectArrayList<>();
    private int lastInventoryHash = -1;
    private int lastArmorHash = -1;

    public NyaLibInventoryAbilityProvider(PlayerEntity player) {
        this.player = player;
        this.playerInventory = player.inventory;
        this.abilityProvider = (MultipleValueAbilityProvider) player.getAbilityProvider(NyaLib.NAMESPACE.id("inventory"));
    }

    /**
     * Called during the {@link PlayerEntity#tick()} method
     */
    public void tick() {
        // Check if the inventory has changed
        if (checkInventoryChanged()) {
            // If it has changed, rebuild the list of tracked items
            rebuildTrackedItems();
        }

        // Query the abilities from the tracked items
        queryAbilities();
    }

    /**
     * A reused object to keep track of the ability values supplied by the {@link InventoryAbilityItem}s
     */
    private final Reference2ObjectOpenHashMap<Ability<?, ?>, ObjectArrayList<AbilityValue<?>>> abilities = new Reference2ObjectOpenHashMap<>();

    /**
     * Queries the abilities from all of the currently tracked {@link InventoryAbilityItem}s
     */
    public void queryAbilities() {
        boolean changed = false;

        // Loops thru all of the tracked items and makes them query their ability values and if those have changed since the last tick
        for (TrackedItemEntry trackedItem : trackedItems) {
            changed |= trackedItem.queryAbilities(player, playerInventory);
        }

        // If any of the values have changed, then the ability values in the provider are updated
        if (changed) {
            abilities.clear();

            for (TrackedItemEntry trackedItem : trackedItems) {
                for (Map.Entry<Ability<?, ?>, AbilityValue<?>> entry : trackedItem.abilityValues.entrySet()) {
                    abilities.computeIfAbsent(entry.getKey(), k -> new ObjectArrayList<>()).add(entry.getValue());
                }
            }

            for (Map.Entry<Ability<?, ?>, ObjectArrayList<AbilityValue<?>>> entry : abilities.entrySet()) {
                abilityProvider.setMultiple(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * @return {@code true} if the inventory has changed since the last tick, {@code false} otherwise
     */
    public boolean checkInventoryChanged() {
        int inventoryHash = Arrays.hashCode(playerInventory.main);
        int armorHash = Arrays.hashCode(playerInventory.armor);

        if (inventoryHash != lastInventoryHash || armorHash != lastArmorHash) {
            lastInventoryHash = inventoryHash;
            lastArmorHash = armorHash;
            return true;
        }

        return false;
    }

    /**
     * Queries the player's inventory to collect all of the items to be tracked by this provider
     */
    public void rebuildTrackedItems() {
        abilityProvider.clear();
        trackedItems.clear();

        // Main Inventory
        ItemStack[] main = playerInventory.main;
        for (int slot = 0, mainLength = main.length; slot < mainLength; slot++) {
            ItemStack stack = main[slot];
            if (stack != null && stack.getItem() instanceof InventoryAbilityItem inventoryAbilityItem) {
                InventoryAbilityItemSlot slotType = slot < 9 ? InventoryAbilityItemSlot.HOTBAR : InventoryAbilityItemSlot.INVENTORY;

                Ability<?, ?>[] providedAbilities = inventoryAbilityItem.getProvidedAbilities(player, slotType);
                if (providedAbilities.length > 0) {
                    trackedItems.add(new TrackedItemEntry(stack, inventoryAbilityItem, providedAbilities, slotType, slot));
                }
            }
        }

        // Armor
        ItemStack[] armor = playerInventory.armor;
        for (int slot = 0, armorLength = armor.length; slot < armorLength; slot++) {
            ItemStack stack = armor[slot];
            if (stack != null && stack.getItem() instanceof InventoryAbilityItem inventoryAbilityItem) {
                InventoryAbilityItemSlot slotType = InventoryAbilityItemSlot.ARMOR;

                Ability<?, ?>[] providedAbilities = inventoryAbilityItem.getProvidedAbilities(player, slotType);
                if (providedAbilities.length > 0) {
                    trackedItems.add(new TrackedItemEntry(stack, inventoryAbilityItem, providedAbilities, slotType, slot));
                }
            }
        }
        
        // Notify all the tracked items
        for (TrackedItemEntry trackedItem : trackedItems) {
            trackedItem.inventoryAbility.onInventoryChanged(player, playerInventory, trackedItem.stack, trackedItem.slotType, trackedItem.slot);
        }
    }

    /**
     * Represents a currently tracked item in the inventory
     */
    public static class TrackedItemEntry {
        public final ItemStack stack;
        public final Item item;
        public final InventoryAbilityItem inventoryAbility;
        public final Ability<?, ?>[] providedAbilities;
        public final InventoryAbilityItemSlot slotType;
        public final int slot;

        public Reference2ObjectOpenHashMap<Ability<?, ?>, AbilityValue<?>> abilityValues = new Reference2ObjectOpenHashMap<>();

        public TrackedItemEntry(ItemStack stack, InventoryAbilityItem inventoryAbility, Ability<?, ?>[] providedAbilities, InventoryAbilityItemSlot slotType, int slot) {
            this.stack = stack;
            this.item = stack.getItem();
            this.inventoryAbility = inventoryAbility;
            this.providedAbilities = providedAbilities;
            this.slotType = slotType;
            this.slot = slot;
        }

        /**
         * Queries the ability values for the provided abilities and returns {@code true} if any of the values have changed compared to their last values
         */
        public boolean queryAbilities(PlayerEntity player, PlayerInventory playerInventory) {
            for (Ability<?, ?> ability : providedAbilities) {
                AbilityValue<?> value = inventoryAbility.getAbilityValue(ability, player, playerInventory, stack, slotType, slot);
                AbilityValue<?> oldValue = abilityValues.put(ability, value);
                if (!value.equals(oldValue)) {
                    return true;
                }
            }

            return false;
        }
    }
}
