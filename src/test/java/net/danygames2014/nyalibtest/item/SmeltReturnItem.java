package net.danygames2014.nyalibtest.item;

import net.danygames2014.nyalib.item.HasSmeltingReturnStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class SmeltReturnItem extends TemplateItem implements HasSmeltingReturnStack {
    public SmeltReturnItem(Identifier identifier) {
        super(identifier);
    }

    @Override
    public ItemStack getSmeltingReturnStack(ItemStack stack) {
        return new ItemStack(Item.ARROW, 54);
    }
}
