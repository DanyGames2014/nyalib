package net.danygames2014.nyalib.mixininterface;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.danygames2014.nyalib.item.InventoryManager;
import net.modificationstation.stationapi.api.util.Util;

public interface ItemItemSlotTemplateRetriever {
    default ObjectArrayList<InventoryManager.ItemSlotEntry> nyalib$getTemplateItemSlotEntries() {
        return Util.assertImpl();
    }
}
