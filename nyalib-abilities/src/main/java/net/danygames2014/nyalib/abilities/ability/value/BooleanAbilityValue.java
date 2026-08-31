package net.danygames2014.nyalib.abilities.ability.value;

import net.minecraft.nbt.NbtCompound;

/**
 * An ability value which stores a boolean value
 */
public class BooleanAbilityValue extends AbilityValue<Boolean>{
    public boolean value;

    public BooleanAbilityValue(boolean value) {
        this.value = value;
    }
    
    public BooleanAbilityValue() {
        this(false);
    }

    @Override
    public Boolean get() {
        return value;
    }

    /**
     * Get the boolean value of this AbilityValue without boxing
     */
    public boolean getBoolean() {
        return value;
    }

    @Override
    public void set(Boolean value) {
        this.value = value;
    }

    /**
     * Set the boolean value of this AbilityValue without boxing
     */
    public void setBoolean(boolean value) {
        this.value = value;
    }

    @Override
    public Boolean computeAnd(Boolean previousValue) {
        return previousValue && value;
    }

    @Override
    public Boolean computeOr(Boolean previousValue) {
        return previousValue || value;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putBoolean("value", value);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.value = nbt.getBoolean("value");
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof BooleanAbilityValue booleanAbilityValue) {
            return booleanAbilityValue.value == this.value;
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(value);
    }

    /**
     * Create a new BooleanAbilityValue with the given value
     */
    public static BooleanAbilityValue of(boolean value) {
        return new BooleanAbilityValue(value);
    }
}
