package net.danygames2014.nyalib.abilities.mixininterface;

import net.modificationstation.stationapi.api.util.Util;

public interface NyaLibFlyingPlayer {
    default boolean nyalib$canFly() {
        return Util.assertImpl();
    }

    default boolean nyalib$isFlying() {
        return Util.assertImpl();
    }
    
    default void nyalib$setFlying(boolean flying) {
        Util.assertImpl();
    }
}
