package net.danygames2014.nyalib.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.EnumProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.StringIdentifiable;

public class WallBlockTemplate extends TemplateBlock {
    public static final EnumProperty<WallType> EAST = EnumProperty.of("east", WallType.class);
    public static final EnumProperty<WallType> WEST = EnumProperty.of("west", WallType.class);
    public static final EnumProperty<WallType> NORTH = EnumProperty.of("north", WallType.class);
    public static final EnumProperty<WallType> SOUTH = EnumProperty.of("south", WallType.class);
    public static final BooleanProperty UP = BooleanProperty.of("up");

    public WallBlockTemplate(Identifier identifier, Block baseBlock, Material material, Identifier texture) {
        super(identifier, material);
        if (texture != null) {
            TemplateBlockRegistry.registerWall(identifier, texture);
        }
    }
    
    public WallBlockTemplate(Identifier identifier, Block baseBlock, Identifier texture) {
        this(identifier, baseBlock, baseBlock.material, texture);
    }
    
    public WallBlockTemplate(Identifier identifier, Block baseBlock) {
        this(identifier, baseBlock, null);
    }
    
    

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(EAST, WEST, NORTH, SOUTH, UP);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return super.getPlacementState(context)
                .with(EAST, WallType.NONE)
                .with(WEST, WallType.NONE)
                .with(NORTH, WallType.NONE)
                .with(SOUTH, WallType.NONE)
                .with(UP, true);
    }

    public void updateConnections(World world, int x, int y, int z) {
        
    }

    public enum WallType implements StringIdentifiable {
        NONE("none"),
        LOW("low"),
        HIGH("high");

        public final String id;

        WallType(String id) {
            this.id = id;
        }

        @Override
        public String asString() {
            return id;
        }
    }
}
