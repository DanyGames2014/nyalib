package net.danygames2014.nyalib.abilities.ability.value;

import net.minecraft.nbt.NbtCompound;

/**
 * Represents a current value of an ability
 */
public abstract class AbilityValue<T> {
    /**
     * @return The current value
     */
    public abstract T get();

    /**
     * Sets the current value of this ability value
     * @param value The value to set this ability value to
     */
    public abstract void set(T value);

    /**
     * Computes the ability of this values and the {@code previousValue} using an AND operation
     * <p> This is used when the {@link net.danygames2014.nyalib.abilities.ability.AbilityRule#AND} rule is used
     * @return The result of this operation
     */
    public abstract T computeAnd(T previousValue);

    /**
     * Computes the ability of this values and the {@code previousValue} using an OR operation
     * <p> This is used when the {@link net.danygames2014.nyalib.abilities.ability.AbilityRule#OR} rule is used
     * @return The result of this operation
     */
    public abstract T computeOr(T previousValue);

    /**
     * Writes the current value of this ability value to the given {@link NbtCompound}
     */
    public abstract void writeNbt(NbtCompound nbt);

    /**
     * Reads the current value of this ability value from the given {@link NbtCompound}
     */
    public abstract void readNbt(NbtCompound nbt);

    /**
     * Compares this ability value to another object.
     * <p> When the other object is an {@link AbilityValue}, this method will also compare their values
     * @param other the object to compare to
     * @return <code>true</code> if the objects are equal and the values are equal, <code>false</code> otherwise
     */
    @Override
    public abstract boolean equals(Object other);

    /**
     * Computes a hash code for this ability value, this should be same for ability values of the same type that have the same value
     */
    @Override
    public abstract int hashCode();
}
