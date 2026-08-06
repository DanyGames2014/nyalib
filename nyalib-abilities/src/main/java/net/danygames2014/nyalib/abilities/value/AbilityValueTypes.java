package net.danygames2014.nyalib.abilities.value;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;

public class AbilityValueTypes {
    public static Object2ObjectOpenHashMap<String, AbilityValueFactory<?>> TYPE_TO_FACTORY = new Object2ObjectOpenHashMap<>();
    public static Reference2ObjectOpenHashMap<Class<? extends AbilityValue<?>>, String> CLASS_TO_TYPE = new Reference2ObjectOpenHashMap<>();
    
    static {
        register("integer", IntAbilityValue::new, IntAbilityValue.class);
        register("boolean", BooleanAbilityValue::new, BooleanAbilityValue.class);
    }
    
    public static <T> void register(String type, AbilityValueFactory<T> factory, Class<? extends AbilityValue<?>> valueClass) {
        TYPE_TO_FACTORY.put(type, factory);
        CLASS_TO_TYPE.put(valueClass, type);
    }
}
