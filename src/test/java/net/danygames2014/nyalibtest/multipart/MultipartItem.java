package net.danygames2014.nyalibtest.multipart;

import net.danygames2014.nyalib.mixininterface.MultipartWorld;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;

public class MultipartItem extends TemplateItem {
    public MultipartItem(Identifier identifier) {
        super(identifier);
    }

    @Override
    public boolean useOnBlock(ItemStack stack, PlayerEntity user, World world, int x, int y, int z, int side) {
        Direction dir = Direction.byId(side);
        if (world instanceof MultipartWorld multipartWorld) {
            if (user.isSneaking()) {
                System.out.println(FabricLoader.getInstance().getEnvironmentType() + ":" + multipartWorld.getMultipartState(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ()));
            } else {
                if (!world.isRemote) {
                    multipartWorld.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new TestMultipartComponent());
                }
            }
            return true;
        }

        return super.useOnBlock(stack, user, world, x, y, z, side);
    }
}
