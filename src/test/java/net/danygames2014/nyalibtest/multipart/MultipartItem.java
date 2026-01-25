package net.danygames2014.nyalibtest.multipart;

import net.danygames2014.nyalib.item.EnhancedPlacementContextItem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;

public class MultipartItem extends TemplateItem implements EnhancedPlacementContextItem {
    public MultipartItem(Identifier identifier) {
        super(identifier);
    }

    @Override
    public boolean useOnBlock(ItemStack stack, PlayerEntity player, World world, int x, int y, int z, int side, Vec3d hitVec) {
        System.out.println(FabricLoader.getInstance().getEnvironmentType() + ": stack = " + stack + ", player = " + player + ", world = " + world + ", x = " + x + ", y = " + y + ", z = " + z + ", side = " + side + ", hitVec = " + hitVec);
        
        Direction dir = Direction.byId(side);
        if (player.isSneaking()) {
            System.out.println(FabricLoader.getInstance().getEnvironmentType() + ":" + world.getMultipartState(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ()));
        } else {
            if (!world.isRemote) {
                world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new TestMultipartComponent());
            }
        }

        return true;
    }
}
