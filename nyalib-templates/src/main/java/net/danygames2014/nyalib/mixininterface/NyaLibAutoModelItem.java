package net.danygames2014.nyalib.mixininterface;

import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.util.Util;

public interface NyaLibAutoModelItem {
    default Item registerAutomaticModel() {
        return Util.assertImpl();
    }
}
