package net.danygames2014.nyalib.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.EnumProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.ArrayList;

public class StairsBlockTemplate extends TemplateBlock {

    public static final EnumProperty<Direction> FACING = EnumProperty.of("facing", Direction.class, dir -> dir.getAxis().isHorizontal());

    Block baseBlock;

    public StairsBlockTemplate(Identifier identifier, Block baseBlock) {
        this(identifier, baseBlock, null);
    }

    public StairsBlockTemplate(Identifier identifier, Block baseBlock, Identifier texture) {
        super(identifier, baseBlock.material);
        this.baseBlock = baseBlock;
        if (texture != null) {
            TemplateBlockRegistry.registerStairs(identifier, texture);
        }
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        PlayerEntity player = context.getPlayer();
        BlockState state = getDefaultState();

        Direction facing = context.getSide();

//        System.out.println("Facing : " + facing + " | Horizontal : " + facing.getAxis().isHorizontal() + " | Vertical : " + facing.getAxis().isVertical());

//        if (facing.getAxis().isHorizontal() && false) {
//            facing = Direction.fromRotation(player == null ? 0 : (player.yaw - 45.0F));
//        } else {
        facing = Direction.fromRotation(player == null ? 0 : player.yaw);
//        }

        return state.with(FACING, facing);
    }

    @Override
    public void addIntersectingBoundingBox(World world, int x, int y, int z, Box box, ArrayList boxes) {
        BlockState state = world.getBlockState(x, y, z);
        
        if (!state.isOf(this)) { 
            return;
        }
        
        Direction facing = state.get(FACING);
        if (facing == Direction.SOUTH) {
            this.setBoundingBox(0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F);
            super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
            this.setBoundingBox(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
        } else if (facing == Direction.NORTH) {
            this.setBoundingBox(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
            super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
            this.setBoundingBox(0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
            super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
        } else if (facing == Direction.WEST) {
            this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F);
            super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
            this.setBoundingBox(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
            super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
        } else if (facing == Direction.EAST) {
            this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
            super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
            this.setBoundingBox(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
            super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
        }

        this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isOpaque() {
        return false;
    }
}
