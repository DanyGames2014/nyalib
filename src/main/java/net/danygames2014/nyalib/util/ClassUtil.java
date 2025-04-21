package net.danygames2014.nyalib.util;

import net.danygames2014.nyalib.capability.block.BlockCapability;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ClassUtil {
    public static Class<?> getGenericSuperclass(Object object, Class<?> defaultClass) {
        Class<?> capabilityClass = null;

        Type superclass = object.getClass().getGenericSuperclass();
        if (superclass instanceof ParameterizedType parameterized) {
            Type typeArg = parameterized.getActualTypeArguments()[0];
            if (typeArg instanceof Class<?> capabilityClassArg) {
                capabilityClass = capabilityClassArg;
            }
        }

        return capabilityClass != null ? capabilityClass : defaultClass; 
    }
}
