package net.danygames2014.nyalib.abilities.ability.value;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;

public class AbilityValueTypeRegistry {
    public static Object2ObjectOpenHashMap<String, AbilityValueFactory<?>> TYPE_TO_FACTORY = new Object2ObjectOpenHashMap<>();
    public static Reference2ObjectOpenHashMap<Class<? extends AbilityValue<?>>, String> CLASS_TO_TYPE = new Reference2ObjectOpenHashMap<>();
    public static Reference2ObjectOpenHashMap<Class<?>, String> VALUE_TYPE_CLASS_TO_TYPE = new Reference2ObjectOpenHashMap<>();
    public static Reference2ObjectOpenHashMap<Class<?>, AbilityValueFactory<?>> VALUE_TYPE_CLASS_TO_FACTORY = new Reference2ObjectOpenHashMap<>();
    
    public static <T> void register(String type, AbilityValueFactory<T> factory, Class<? extends AbilityValue<?>> valueClass, Class<?> valueTypeClass) {
        TYPE_TO_FACTORY.put(type, factory);
        CLASS_TO_TYPE.put(valueClass, type);
        VALUE_TYPE_CLASS_TO_TYPE.put(valueTypeClass, type);
        VALUE_TYPE_CLASS_TO_FACTORY.put(valueTypeClass, factory);
    }
    
    public static AbilityValueFactory<?> getFactory(String type) {
        return TYPE_TO_FACTORY.getOrDefault(type, null);
    }
    
    public static AbilityValueFactory<?> getFactory(Class<?> valueTypeClass) {
        return VALUE_TYPE_CLASS_TO_FACTORY.getOrDefault(valueTypeClass, null);
    }
    
    @Nullable
    public static String getId(Class<?> abilityValueClass) {
        return CLASS_TO_TYPE.getOrDefault(abilityValueClass, null);
    }
    
    public static String getIdFromValueType(Class<?> valueTypeClass) {
        return VALUE_TYPE_CLASS_TO_TYPE.getOrDefault(valueTypeClass, null);
    }
}
