package net.danygames2014.nyalibtest.capability.item;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.capability.CapabilityHelper;
import net.danygames2014.nyalib.capability.block.itemhandler.ItemHandlerBlockCapability;
import net.danygames2014.nyalib.capability.entity.EntityCapabilityRegistry;
import net.danygames2014.nyalib.capability.entity.itemhandler.ItemHandlerEntityCapability;
import net.danygames2014.nyalibtest.capability.meow.MeowBlockCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;

public class YoinkerItem extends TemplateItem {
    public YoinkerItem(Identifier identifier) {
        super(identifier);
    }

    @Override
    public int getAttackDamage(Entity attackedEntity) {
        entityYoink(attackedEntity);
        return 0;
    }

    @Override
    public void useOnEntity(ItemStack stack, LivingEntity entity) {
        entityYoink(entity);
    }

    public void entityYoink(Entity entity) {
        if (!entity.world.isRemote) {
            ItemHandlerEntityCapability itemHandler = EntityCapabilityRegistry.getCapability(entity, NyaLib.NAMESPACE.id("item_handler"));

            if (itemHandler != null) {
                ItemStack yoinkedStack = itemHandler.extractItem();

                if (yoinkedStack != null) {
                    entity.dropItem(yoinkedStack, 1.0F);
                }
            }
        }
    }

    @Override
    public boolean useOnBlock(ItemStack stack, PlayerEntity user, World world, int x, int y, int z, int side) {
        blockYoink(world, x, y, z, side);
        return true;
    }

    public void blockYoink(World world, int x, int y, int z, int side) {
        if (!world.isRemote) {
            ItemHandlerBlockCapability itemHandler = CapabilityHelper.getCapability(world, x, y, z, ItemHandlerBlockCapability.class);

            if (itemHandler != null) {
                ItemStack yoinkedStack = itemHandler.extractItem(Direction.byId(side));

                if (yoinkedStack != null) {
                    ItemEntity itemEntity = new ItemEntity(world, x, y, z, yoinkedStack);
                    world.spawnEntity(itemEntity);
                }
            } else {
                MeowBlockCapability meowCap = CapabilityHelper.getCapability(world, x, y, z, MeowBlockCapability.class);
                
                if (meowCap != null) {
                    meowCap.meow();
                }
            }
        }
    }
}
