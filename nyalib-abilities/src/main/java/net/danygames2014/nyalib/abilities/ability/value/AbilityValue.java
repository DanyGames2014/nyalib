package net.danygames2014.nyalib.abilities.ability.value;

import net.minecraft.nbt.NbtCompound;

/**
 * Represents a current value of an ability
 */
public abstract class AbilityValue<T> {
    public abstract T get();
    
    public abstract void set(T value);
    
    public abstract T computeAnd(T previousValue);
    
    public abstract T computeOr(T previousValue);
    
    public abstract void writeNbt(NbtCompound nbt);
    
    public abstract void readNbt(NbtCompound nbt);
}
