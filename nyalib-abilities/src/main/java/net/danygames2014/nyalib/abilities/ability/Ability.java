package net.danygames2014.nyalib.abilities.ability;

import net.danygames2014.nyalib.abilities.ability.value.AbilityValue;
import net.minecraft.entity.Entity;
import net.modificationstation.stationapi.api.util.Identifier;

/**
 * Represents an ability that can be applied to an entity
 * <p> The value of this ability is determined by the {@link AbilityProvider}s and is computed according to the {@link AbilityRule} in the {@link AbilityManager}
 * @param <T> The type of entity this ability can be applied to
 * @param <V> The type of value this ability provides. For example {@link net.danygames2014.nyalib.abilities.ability.value.BooleanAbilityValue}
 */
public class Ability<T extends Entity, V extends AbilityValue<?>> {
    /**
     * The identifier of this ability
     */
    public final Identifier identifier;
    /**
     * The rule according to which the value of this ability is computed if multiple {@link AbilityProvider}s contribute to it
     */
    public final AbilityRule abilityRule;
    /**
     * Determines how this ability is synced to clients
     * <p> See {@link AbilitySyncType} for the possible values
     */
    public AbilitySyncType syncType = AbilitySyncType.NONE;
    /**
     * If this is <code>true</code>, the ability will be instantly computed and synced to clients after it is marked dirty
     */
    public boolean syncInstantly = false;
    /**
     * The value that will be returned by the {@link AbilityManager#get(Entity, Ability)} if no {@link AbilityProvider} provides a value for this ability
     */
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
