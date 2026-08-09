package net.danygames2014.nyalib.abilities.ability;

public enum AbilityRule {
    /**
     * On boolean abilities requires all {@link AbilityProvider}s to be true
     * <p>On numeric abilities, takes the sum of all the values from all {@link AbilityProvider}s
     */
    AND,
    /**
     * On boolean abilities requires atleast one {@link AbilityProvider}s to be true
     * <p>On numeric abilities, takes the highest value of all the provided values from all {@link AbilityProvider}s
     */
    OR
}
