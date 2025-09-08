package net.danygames2014.nyalib.init.fluid;

import net.danygames2014.nyalib.block.FluidBlockManager;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.event.resource.language.TranslationInvalidationEvent;

public class TranslationListener {
    @EventListener
    public void addFluidTranslations(TranslationInvalidationEvent event) {
        FluidBlockManager.registerTranslations();
        ItemListener.registerTranslations();
    }
}
