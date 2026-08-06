package net.danygames2014.nyalib.abilities.value;

import net.minecraft.nbt.NbtCompound;

public abstract class AbilityValue<T> {
    public abstract T get();
    
    public abstract void set(T value);
    
    public abstract T computeAnd(T previousValue);
    
    public abstract T computeOr(T previousValue);
    
    public abstract void writeNbt(NbtCompound nbt);
    
    public abstract void readNbt(NbtCompound nbt);
}
