package net.danygames2014.nyalib.abilities.value;

import net.minecraft.nbt.NbtCompound;

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

    @Override
    public void set(Integer value) {
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

    public static IntAbilityValue of(int value) {
        return new IntAbilityValue(value);
    }
}
