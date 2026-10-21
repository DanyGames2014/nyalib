package net.danygames2014.nyalibtest.abilities;

import net.danygames2014.nyalib.abilities.ability.Ability;
import net.danygames2014.nyalib.abilities.ability.AbilityManager;
import net.danygames2014.nyalib.abilities.ability.impl.Abilities;
import net.danygames2014.nyalib.abilities.ability.impl.inventoryprovider.InventoryAbilityItem;
import net.danygames2014.nyalib.abilities.ability.impl.inventoryprovider.InventoryAbilityItemSlot;
import net.danygames2014.nyalib.abilities.ability.value.AbilityValue;
import net.danygames2014.nyalib.abilities.ability.value.IntAbilityValue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class ExtraHeartsStick extends TemplateItem implements InventoryAbilityItem {
    Identifier providerId;
    
    public ExtraHeartsStick(Identifier identifier, Identifier providerId) {
        super(identifier);
        this.providerId = providerId;
    }

    @Override
    public ItemStack use(ItemStack stack, World world, PlayerEntity user) {
        if (world.isRemote) {
            return stack;
        }
        
        if (user.isSneaking()) {
            Integer managerValue = AbilityManager.getInstance().get(user, Abilities.EXTRA_HEARTS);
            user.sendMessage("Extra hearts ability from manager " + managerValue);
        } else {
            user.sendMessage("Health: " + user.health + "/" + user.maxHealth);
        }
        
        return stack;
    }

    @Override
    public Ability<?, ?>[] getProvidedAbilities(PlayerEntity player, InventoryAbilityItemSlot slotType) {
        return new Ability[] {
                Abilities.EXTRA_HEARTS
        };
    }

    @Override
    public AbilityValue<?> getAbilityValue(Ability<?, ?> ability, PlayerEntity player, PlayerInventory playerInventory, ItemStack stack, InventoryAbilityItemSlot slotType, int slot) {
        if (ability == Abilities.EXTRA_HEARTS) {
            return IntAbilityValue.of(20);
        }
        
        return IntAbilityValue.of(0);
    }
}
