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

public class NyaLibInventoryAbilityProvider {
    private final PlayerEntity player;
    private final PlayerInventory playerInventory;
    private final MultipleValueAbilityProvider abilityProvider;

    private final ObjectArrayList<TrackedItemEntry> trackedItems = new ObjectArrayList<>();
    private int lastInventoryHash = -1;
    private int lastArmorHash = -1;

    public NyaLibInventoryAbilityProvider(PlayerEntity player) {
        this.player = player;
        this.playerInventory = player.inventory;
        this.abilityProvider = (MultipleValueAbilityProvider) player.getAbilityProvider(NyaLib.NAMESPACE.id("inventory"));
    }

    public void tick() {
        long startTime = System.nanoTime();

        if (checkInventoryChanged()) {
            System.err.println("Inventory changed!");
            rebuildTrackedItems();
        }

        queryAbilities();
        long endTime = System.nanoTime();
        System.err.println("Time: " + (endTime - startTime) + "ns");
    }
    
    private final Reference2ObjectOpenHashMap<Ability<?, ?>, ObjectArrayList<AbilityValue<?>>> abilities = new Reference2ObjectOpenHashMap<>();
    
    public void queryAbilities() {
        boolean changed = false;
        
        for (TrackedItemEntry trackedItem : trackedItems) {
            changed |= trackedItem.queryAbilities(player, playerInventory);
        }
        
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
    }
    
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
