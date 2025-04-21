package net.danygames2014.nyalibtest.block.capability.item;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.capability.entity.EntityCapability;
import net.danygames2014.nyalib.capability.entity.EntityCapabilityRegistry;
import net.danygames2014.nyalib.capability.entity.itemhandler.ItemHandlerEntityCapability;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class YoinkerItem extends TemplateItem {
    public YoinkerItem(Identifier identifier) {
        super(identifier);
    }

    @Override
    public void useOnEntity(ItemStack stack, LivingEntity entity) {
        if(!entity.world.isRemote && entity instanceof PlayerEntity player) {
            EntityCapability cap = EntityCapabilityRegistry.getCapability(entity, NyaLib.NAMESPACE.id("item_handler"));
            if(cap instanceof ItemHandlerEntityCapability itemHandler) {
                ItemStack yoinkedStack = itemHandler.extractItem();
                
                if(yoinkedStack != null) {
                    entity.dropItem(yoinkedStack, 1.0F);
                }
                
                return;
            }
        }
        super.useOnEntity(stack, entity);
    }
}
