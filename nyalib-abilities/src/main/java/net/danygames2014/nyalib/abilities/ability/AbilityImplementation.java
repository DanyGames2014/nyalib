package net.danygames2014.nyalib.abilities.ability;

public class AbilityImplementation <T extends Ability<?, ?>> {
    // hasOwnership
    // Track ownership in AbilityRegistry or AbilityManager
    // When registering, include a priority and sort each time to pick the one with latest priority
    // Allow to renounce ownership and pick the next highest priority in line
}
