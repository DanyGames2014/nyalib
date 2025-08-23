package net.danygames2014.nyalib.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.EnumProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;

public class RotateableBlockTemplate extends TemplateBlock {
    public static final EnumProperty<Direction.Axis> AXIS = EnumProperty.of("axis", Direction.Axis.class);

    public RotateableBlockTemplate(Identifier identifier, Material material) {
        this(identifier, material, null, null);
    }
    
    public RotateableBlockTemplate(Identifier identifier, Material material, Identifier endTexture, Identifier sideTexture) {
        super(identifier, material);
        setDefaultState(getDefaultState().with(AXIS, Direction.Axis.Y));
        if (endTexture != null && sideTexture != null) {
            TemplateBlockRegistry.registerRotateableBlock(identifier, endTexture, sideTexture);
        }
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(AXIS);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return getDefaultState().with(AXIS, context.getSide().getAxis());
    }
}
