package net.danygames2014.nyalib.abilities.value;

public class IntAbilityValue extends AbilityValue<Integer> {
    public int value;
    
    private IntAbilityValue(int value) {
        this.value = value;
    }

    @Override
    public void set(Integer value) {
        this.value = value;
    }
    
    public static IntAbilityValue of(int value) {
        return new IntAbilityValue(value);
    }
}
