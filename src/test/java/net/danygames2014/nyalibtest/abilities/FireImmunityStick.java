package net.danygames2014.nyalibtest.abilities;

import net.danygames2014.nyalib.abilities.ability.AbilityManager;
import net.danygames2014.nyalib.abilities.ability.AbilityProvider;
import net.danygames2014.nyalib.abilities.ability.impl.Abilities;
import net.danygames2014.nyalib.abilities.value.BooleanAbilityValue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class FireImmunityStick extends TemplateItem {
    Identifier providerId;
    
    public FireImmunityStick(Identifier identifier, Identifier providerId) {
        super(identifier);
        this.providerId = providerId;
    }

    @Override
    public ItemStack use(ItemStack stack, World world, PlayerEntity user) {
        AbilityProvider abilityProvider = user.getAbilityProvider(providerId);
        
        if (user.isSneaking()) {
            Boolean managerValue = AbilityManager.getInstance().get(user, Abilities.FIRE_IMMUNITY);
            user.sendMessage("Fire Immunity ability from manager " + managerValue);
        } else {
            BooleanAbilityValue abilityValue = abilityProvider.get(Abilities.FIRE_IMMUNITY);
            boolean currentValue = abilityValue != null && abilityValue.value;
            abilityProvider.set(Abilities.FIRE_IMMUNITY, BooleanAbilityValue.of(!currentValue));
            user.sendMessage("Fire Immunity ability from provider " + providerId.toString() + " is now " + !currentValue);
        }
        
        return stack;
    }
}
