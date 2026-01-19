package net.danygames2014.nyalib.mixininterface;

import net.danygames2014.nyalib.item.InventoryManager;
import net.modificationstation.stationapi.api.util.Util;

/**
 * DO NOT implement this class on anything!
 */
public interface ItemStackInventoryManagerRetriever {
    default InventoryManager nyalib$getInventoryManager() {
        return Util.assertImpl();
    }

    default void nyalib$setInventoryManager(InventoryManager inventoryManager) {
        Util.assertImpl();
    }
}
