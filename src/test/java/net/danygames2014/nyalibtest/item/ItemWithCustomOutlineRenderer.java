package net.danygames2014.nyalibtest.item;

import net.danygames2014.nyalib.item.multipart.CustomMultipartOutlineRenderer;
import net.danygames2014.nyalib.multipart.MultipartHitResult;
import net.minecraft.entity.player.PlayerEntity;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class ItemWithCustomOutlineRenderer extends TemplateItem implements CustomMultipartOutlineRenderer {
    public ItemWithCustomOutlineRenderer(Identifier identifier) {
        super(identifier);
    }

    @Override
    public boolean renderOutline(PlayerEntity player, MultipartHitResult hit, float tickDelta) {
        return true;
    }
}
