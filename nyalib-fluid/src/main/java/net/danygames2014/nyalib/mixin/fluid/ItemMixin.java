package net.danygames2014.nyalib.mixin.fluid;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.danygames2014.nyalib.fluid.TankManager;
import net.danygames2014.nyalib.mixininterface.ItemFluidSlotTemplateRetriever;
import net.danygames2014.nyalib.mixininterface.ItemStackTankManagerRetriever;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Item.class)
public class ItemMixin implements ItemFluidSlotTemplateRetriever {
    @Unique
    private ObjectArrayList<TankManager.FluidSlotEntry> templateSlotEntries;

    @Override
    public ObjectArrayList<TankManager.FluidSlotEntry> nyalib$getTemplateFluidSlotEntries() {
        return templateSlotEntries;
    }

    @SuppressWarnings("MissingUnique")
    public TankManager getTankManager(ItemStack thiz) {
        //noinspection DataFlowIssue
        return ItemStackTankManagerRetriever.class.cast(thiz).nyalib$getTankManager();
    }

    @SuppressWarnings("MissingUnique")
    public TankManager.FluidSlotEntry addFluidSlot(int capacity) {
        if (templateSlotEntries == null) {
            templateSlotEntries = new ObjectArrayList<>();
        }

        TankManager.FluidSlotEntry entry = new TankManager.FluidSlotEntry(capacity);
        templateSlotEntries.add(entry);
        return entry;
    }
}
