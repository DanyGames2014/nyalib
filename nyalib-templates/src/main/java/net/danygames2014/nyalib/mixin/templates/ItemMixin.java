package net.danygames2014.nyalib.mixin.templates;

import net.danygames2014.nyalib.mixininterface.NyaLibAutoModelItem;
import net.danygames2014.nyalib.registry.TemplateModelRegistry;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.item.StationFlatteningItem;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public class ItemMixin implements NyaLibAutoModelItem, StationFlatteningItem {
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public Item registerAutomaticModel() {
        RegistryEntry.Reference<Item> registryEntry = this.getRegistryEntry();
        Identifier identifier = registryEntry.registryKey().getValue();
        TemplateModelRegistry.registerGeneratedItem(identifier, Identifier.of(identifier.getNamespace(), "item/" + identifier.getPath()));
        return Item.class.cast(this);
    }
}
