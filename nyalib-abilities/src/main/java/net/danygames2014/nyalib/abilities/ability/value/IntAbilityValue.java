package net.danygames2014.nyalib.abilities.ability.value;

import net.minecraft.nbt.NbtCompound;

/**
 * An ability value which stores an int value
 */
public class IntAbilityValue extends AbilityValue<Integer> {
    public int value;
    
    public IntAbilityValue(int value) {
        this.value = value;
    }
    
    public IntAbilityValue() {
        this(0);
    }

    @Override
    public Integer get() {
        return value;
    }

    /**
     * Get the int value of this AbilityValue without boxing
     */
    public int getInt() {
        return value;
    }

    @Override
    public void set(Integer value) {
        this.value = value;
    }

    /**
     * Set the int value of this AbilityValue without boxing
     */
    public void setInt(int value) {
        this.value = value;
    }

    @Override
    public Integer computeAnd(Integer previousValue) {
        return previousValue + value;
    }

    @Override
    public Integer computeOr(Integer previousValue) {
        return Math.max(previousValue, value);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putInt("value", value);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.value = nbt.getInt("value");
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof IntAbilityValue intAbilityValue) {
            return intAbilityValue.value == this.value;
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        return value;
    }

    /**
     * Create a new IntAbilityValue with the given value
     */
    public static IntAbilityValue of(int value) {
        return new IntAbilityValue(value);
    }
}
