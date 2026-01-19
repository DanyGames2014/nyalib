package net.danygames2014.nyalib.mixininterface;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.danygames2014.nyalib.fluid.TankManager;
import net.modificationstation.stationapi.api.util.Util;

public interface ItemFluidSlotTemplateRetriever {
    default ObjectArrayList<TankManager.FluidSlotEntry> nyalib$getTemplateFluidSlotEntries() {
        return Util.assertImpl();
    }
}
