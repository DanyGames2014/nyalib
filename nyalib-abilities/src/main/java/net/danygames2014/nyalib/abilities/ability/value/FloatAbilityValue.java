package net.danygames2014.nyalib.abilities.ability.value;

import net.minecraft.nbt.NbtCompound;

public class FloatAbilityValue extends AbilityValue<Float> {
    public float value;

    public FloatAbilityValue(float value) {
        this.value = value;
    }

    public FloatAbilityValue() {
        this(0f);
    }

    @Override
    public Float get() {
        return value;
    }
    
    public float getFloat() {
        return value;
    }

    @Override
    public void set(Float value) {
        this.value = value;
    }
    
    public void setFloat(float value) {
        this.value = value;
    }

    @Override
    public Float computeAnd(Float previousValue) {
        return previousValue + value;
    }

    @Override
    public Float computeOr(Float previousValue) {
        return Math.max(previousValue, value);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putFloat("value", value);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.value = nbt.getFloat("value");
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof FloatAbilityValue floatAbilityValue) {
            return floatAbilityValue.value == this.value;
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(value);
    }

    public static FloatAbilityValue of(float value) {
        return new FloatAbilityValue(value);
    }
}
