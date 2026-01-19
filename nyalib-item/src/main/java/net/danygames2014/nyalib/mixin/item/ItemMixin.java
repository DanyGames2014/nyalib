package net.danygames2014.nyalib.mixin.item;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.danygames2014.nyalib.item.InventoryManager;
import net.danygames2014.nyalib.mixininterface.ItemItemSlotTemplateRetriever;
import net.danygames2014.nyalib.mixininterface.ItemStackInventoryManagerRetriever;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Item.class)
public class ItemMixin implements ItemItemSlotTemplateRetriever {
    @Unique
    private ObjectArrayList<InventoryManager.ItemSlotEntry> templateSlotEntries;

    @Override
    public ObjectArrayList<InventoryManager.ItemSlotEntry> nyalib$getTemplateItemSlotEntries() {
        return templateSlotEntries;
    }
    
    @SuppressWarnings("MissingUnique")
    public InventoryManager getInventoryManager(ItemStack thiz) {
        //noinspection DataFlowIssue
        return ItemStackInventoryManagerRetriever.class.cast(thiz).nyalib$getInventoryManager();
    }
    
    @SuppressWarnings("MissingUnique")
    public InventoryManager.ItemSlotEntry addItemSlot(int maxStackSize) {
        if (templateSlotEntries == null) {
            templateSlotEntries = new ObjectArrayList<>();
        }
        
        InventoryManager.ItemSlotEntry entry = new InventoryManager.ItemSlotEntry(maxStackSize);
        templateSlotEntries.add(entry);
        return entry;
    }
}
