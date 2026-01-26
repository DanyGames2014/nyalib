package net.danygames2014.nyalibtest.multipart;

import net.danygames2014.nyalib.item.EnhancedPlacementContextItem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;

public class CoverMultipartItem extends TemplateItem implements EnhancedPlacementContextItem {
    public Block block;

    public CoverMultipartItem(Identifier identifier, Block block) {
        super(identifier);
        this.block = block;
    }

    @Override
    public boolean useOnBlock(ItemStack stack, PlayerEntity player, World world, int x, int y, int z, int side, Vec3d hitVec) {
        System.out.println(FabricLoader.getInstance().getEnvironmentType() + ": stack = " + stack + ", player = " + player + ", world = " + world + ", x = " + x + ", y = " + y + ", z = " + z + ", side = " + side + ", hitVec = " + hitVec);

        Direction dir = Direction.byId(side);
        if (player.isSneaking()) {
            System.out.println(FabricLoader.getInstance().getEnvironmentType() + ":" + world.getMultipartState(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ()));
        } else {
            if (!world.isRemote) {
                System.out.println(side);
                BlockPos blockPos = new BlockPos(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ());
                Vec3d relativeHit = Vec3d.create(hitVec.x - blockPos.getX(), hitVec.y - blockPos.getY(), hitVec.z - blockPos.getZ());
                System.out.println(relativeHit.x + " " + relativeHit.y + " " + relativeHit.z);
                switch (side){
                    case 0 -> {
                        if(relativeHit.x > 0.20 && relativeHit.x < 0.80 && relativeHit.z > 0.20 && relativeHit.z < 0.80){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.DOWN));
                            return true;
                        }
                        if(relativeHit.z < 0.20){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.SOUTH));
                            return true;
                        }
                        if(relativeHit.x < 0.20){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.EAST));
                            return true;
                        }
                        if(relativeHit.z > 0.80){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.NORTH));
                            return true;
                        }
                        if(relativeHit.x > 0.80){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.WEST));
                            return true;
                        }
                    }

                    case 1 -> {
                        if(relativeHit.x > 0.20 && relativeHit.x < 0.80 && relativeHit.z > 0.20 && relativeHit.z < 0.80){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.UP));
                            return true;
                        }
                        if(relativeHit.z < 0.20){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.SOUTH));
                            return true;
                        }
                        if(relativeHit.x < 0.20){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.EAST));
                            return true;
                        }
                        if(relativeHit.z > 0.80){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.NORTH));
                            return true;
                        }
                        if(relativeHit.x > 0.80){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.WEST));
                            return true;
                        }
                    }
                    case 2 -> {
                        if(relativeHit.x > 0.20 && relativeHit.x < 0.80 && relativeHit.y > 0.20 && relativeHit.y < 0.80){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.NORTH));
                            return true;
                        }
                        if(relativeHit.y < 0.20){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.UP));
                            return true;
                        }
                        if(relativeHit.x < 0.20){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.EAST));
                            return true;
                        }
                        if(relativeHit.y > 0.80){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.DOWN));
                            return true;
                        }
                        if(relativeHit.x > 0.80){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.WEST));
                            return true;
                        }
                    }
                    case 3 -> {
                        if(relativeHit.x > 0.20 && relativeHit.x < 0.80 && relativeHit.y > 0.20 && relativeHit.y < 0.80){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.SOUTH));
                            return true;
                        }
                        if(relativeHit.y < 0.20){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.UP));
                            return true;
                        }
                        if(relativeHit.x < 0.20){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.EAST));
                            return true;
                        }
                        if(relativeHit.y > 0.80){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.DOWN));
                            return true;
                        }
                        if(relativeHit.x > 0.80){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.WEST));
                            return true;
                        }
                    }
                    case 4 -> {
                        if(relativeHit.z > 0.20 && relativeHit.z < 0.80 && relativeHit.y > 0.20 && relativeHit.y < 0.80){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.WEST));
                            return true;
                        }
                        if(relativeHit.y < 0.20){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.UP));
                            return true;
                        }
                        if(relativeHit.z < 0.20){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.SOUTH));
                            return true;
                        }
                        if(relativeHit.y > 0.80){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.DOWN));
                            return true;
                        }
                        if(relativeHit.z > 0.80){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.NORTH));
                            return true;
                        }
                    }
                    case 5 -> {
                        if(relativeHit.z > 0.20 && relativeHit.z < 0.80 && relativeHit.y > 0.20 && relativeHit.y < 0.80){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.EAST));
                            return true;
                        }
                        if(relativeHit.y < 0.20){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.UP));
                            return true;
                        }
                        if(relativeHit.z < 0.20){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.SOUTH));
                            return true;
                        }
                        if(relativeHit.y > 0.80){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.DOWN));
                            return true;
                        }
                        if(relativeHit.z > 0.80){
                            world.addMultipartComponent(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), new CoverMultipartComponent(block, Direction.NORTH));
                            return true;
                        }
                    }
                }
            }
        }

        return true;
    }
}
