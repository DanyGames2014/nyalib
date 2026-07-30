package net.danygames2014.nyalibtest.item;

import net.danygames2014.nyalib.capability.CapabilityHelper;
import net.danygames2014.nyalibtest.capability.improper.ImproperBlockCapability;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class ImproperCapabilityTesterItem extends TemplateItem {
    public ImproperCapabilityTesterItem(Identifier identifier) {
        super(identifier);
    }

    @Override
    public boolean useOnBlock(ItemStack stack, PlayerEntity user, World world, int x, int y, int z, int side) {
        ImproperBlockCapability capability = CapabilityHelper.getCapability(world, x, y, z, ImproperBlockCapability.class);
        if (capability != null) {
            capability.test();
            return true;
        }
        
        return false;
    }
}
