package net.danygames2014.nyalib.abilities.ability.value;

import net.minecraft.nbt.NbtCompound;

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
    
    public boolean getBoolean() {
        return value;
    }

    @Override
    public void set(Boolean value) {
        this.value = value;
    }
    
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

    public static BooleanAbilityValue of(boolean value) {
        return new BooleanAbilityValue(value);
    }
}
