package net.danygames2014.nyalibtest.abilities;

import net.danygames2014.nyalib.abilities.ability.AbilityManager;
import net.danygames2014.nyalib.abilities.ability.AbilityProvider;
import net.danygames2014.nyalib.abilities.ability.impl.Abilities;
import net.danygames2014.nyalib.abilities.ability.value.BooleanAbilityValue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class InvincibilityStick extends TemplateItem {
    Identifier providerId;
    
    public InvincibilityStick(Identifier identifier, Identifier providerId) {
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
            Boolean managerValue = AbilityManager.getInstance().get(user, Abilities.INVINCIBILITY);
            user.sendMessage("Invincibility ability from manager " + managerValue);
        } else {
            BooleanAbilityValue abilityValue = abilityProvider.get(Abilities.INVINCIBILITY);
            boolean currentValue = abilityValue != null && abilityValue.value;
            abilityProvider.set(Abilities.INVINCIBILITY, BooleanAbilityValue.of(!currentValue));
            user.sendMessage("Invincibility ability from provider " + providerId.toString() + " is now " + !currentValue);
        }
        
        return stack;
    }
}
