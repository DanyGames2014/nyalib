package net.danygames2014.nyalib.item.multipart;

import net.danygames2014.nyalib.multipart.MultipartHitResult;
import net.minecraft.entity.player.PlayerEntity;

public interface CustomMultipartOutlineRenderer {
    boolean renderOutline(PlayerEntity player, MultipartHitResult hit, float tickDelta);
}
