package net.danygames2014.nyalibtest.block.capability.item;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.capability.entity.EntityCapability;
import net.danygames2014.nyalib.capability.entity.EntityCapabilityRegistry;
import net.danygames2014.nyalib.capability.entity.itemhandler.ItemHandlerEntityCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class YoinkerItem extends TemplateItem {
    public YoinkerItem(Identifier identifier) {
        super(identifier);
    }

    @Override
    public int getAttackDamage(Entity attackedEntity) {
        yoink(attackedEntity);
        return 0;
    }

    @Override
    public void useOnEntity(ItemStack stack, LivingEntity entity) {
        yoink(entity);
    }

    public void yoink(Entity entity) {
        if (!entity.world.isRemote) {
            EntityCapability capability = EntityCapabilityRegistry.getCapability(entity, NyaLib.NAMESPACE.id("item_handler"));
            if (capability instanceof ItemHandlerEntityCapability itemHandler) {
                ItemStack yoinkedStack = itemHandler.extractItem();

                if (yoinkedStack != null) {
                    entity.dropItem(yoinkedStack, 1.0F);
                }
            }
        }
    }
}
