package net.danygames2014.nyalib.abilities.ability;

import net.danygames2014.nyalib.abilities.value.AbilityValue;
import net.minecraft.entity.Entity;
import net.modificationstation.stationapi.api.util.Identifier;

public class Ability<T extends Entity, V extends AbilityValue<?>> {
    public final Identifier identifier;
    public final AbilityRule abilityRule;
    public AbilitySyncType syncType = AbilitySyncType.NONE;
    public boolean syncInstantly = false;
    public final V defaultValue;

    public Ability(Identifier identifier, V defaultValue) {
        this(identifier, defaultValue, AbilityRule.OR);
    }

    public Ability(Identifier identifier, V defaultValue, AbilityRule abilityRule) {
        this.identifier = identifier;
        this.abilityRule = abilityRule;
        this.defaultValue = defaultValue;
    }

    /**
     * Sets the sync type of this ability
     * This only matters in multiplayer
     */
    public Ability<T, V> setSyncType(AbilitySyncType syncType) {
        this.syncType = syncType;
        return this;
    }

    /**
     * Sets whether this ability should be synced instantly after being marked dirty or not
     * This only matters in multiplayer
     */
    public Ability<T, V> setSyncInstantly(boolean syncInstantly) {
        this.syncInstantly = syncInstantly;
        return this;
    }
}
