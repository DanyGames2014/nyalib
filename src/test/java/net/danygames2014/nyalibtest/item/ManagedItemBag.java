package net.danygames2014.nyalibtest.item;

import net.danygames2014.nyalib.item.item.ManagedItemHandlerItem;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class ManagedItemBag extends TemplateItem implements ManagedItemHandlerItem, CustomTooltipProvider {
    public ManagedItemBag(Identifier identifier) {
        super(identifier);
        this.addItemSlot();
        this.addItemSlot();
        this.addItemSlot();
    }

    @Override
    public ItemStack use(ItemStack stack, World world, PlayerEntity player) {
        if (player.isSneaking()) {
            ItemStack extractedStack = this.extractItem(stack);
            if (extractedStack != null) {
                ItemEntity itemEntity = new ItemEntity(world, player.x, player.y, player.z, extractedStack);
                world.spawnEntity(itemEntity);
            }
        } else {
            this.insertItem(stack, new ItemStack(new Random().nextInt(1, 50), 1, 0));
        }
        return super.use(stack, world, player);
    }


    @Override
    public @NotNull String[] getTooltip(ItemStack stack, String originalTooltip) {
        ArrayList<String> tooltip = new ArrayList<>();
        
        tooltip.add(originalTooltip);
        for (var item : this.getInventory(stack)) {
            if (item == null) {
                tooltip.add("Empty");
            } else {
                tooltip.add(item.toString());
            }
        }
        
        return tooltip.toArray(new String[0]);
    }
}
