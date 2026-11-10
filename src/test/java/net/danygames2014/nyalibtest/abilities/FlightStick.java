package net.danygames2014.nyalibtest.abilities;

import net.danygames2014.nyalib.abilities.ability.Ability;
import net.danygames2014.nyalib.abilities.ability.AbilityManager;
import net.danygames2014.nyalib.abilities.ability.AbilityProvider;
import net.danygames2014.nyalib.abilities.ability.impl.Abilities;
import net.danygames2014.nyalib.abilities.ability.impl.inventoryprovider.InventoryAbilityItem;
import net.danygames2014.nyalib.abilities.ability.impl.inventoryprovider.InventoryAbilityItemSlot;
import net.danygames2014.nyalib.abilities.ability.value.AbilityValue;
import net.danygames2014.nyalib.abilities.ability.value.BooleanAbilityValue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class FlightStick extends TemplateItem implements InventoryAbilityItem {
    Identifier providerId;
    
    public FlightStick(Identifier identifier, Identifier providerId) {
        super(identifier);
        this.providerId = providerId;
    }

    @Override
    public ItemStack use(ItemStack stack, World world, PlayerEntity user) {
        if (world.isRemote) {
            return stack;
        }
        
        AbilityProvider abilityProvider = user.getAbilityProvider(providerId);
        
        if (user.isSneaking()) {
            Boolean managerValue = AbilityManager.getInstance().get(user, Abilities.FLIGHT);
            user.sendMessage("Flight ability from manager " + managerValue);
        } else {
            BooleanAbilityValue abilityValue = abilityProvider.get(Abilities.FLIGHT);
            boolean currentValue = abilityValue != null && abilityValue.value;
            abilityProvider.set(Abilities.FLIGHT, BooleanAbilityValue.of(!currentValue));
            user.sendMessage("Flight ability from provider " + providerId.toString() + " is now " + !currentValue);
        }
        
        return stack;
    }

    @Override
    public Ability<?, ?>[] getProvidedAbilities(PlayerEntity player, InventoryAbilityItemSlot slotType) {
        return new Ability[] {
                Abilities.FLIGHT
        };
    }

    @Override
    public AbilityValue<?> getAbilityValue(Ability<?, ?> ability, PlayerEntity player, PlayerInventory playerInventory, ItemStack stack, InventoryAbilityItemSlot slotType, int slot) {
        if (ability == Abilities.FLIGHT && slotType == InventoryAbilityItemSlot.HOTBAR && playerInventory.selectedSlot == slot) {
            return BooleanAbilityValue.of(true);
        }
        
        return BooleanAbilityValue.of(false);
    }
}
