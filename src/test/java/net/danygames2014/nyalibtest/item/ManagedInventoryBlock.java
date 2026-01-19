package net.danygames2014.nyalibtest.item;

import net.danygames2014.nyalibtest.NyaLibTest;
import net.danygames2014.nyalibtest.screen.handler.ManagedInventoryScreenHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;

public class ManagedInventoryBlock extends TemplateBlockWithEntity {
    public ManagedInventoryBlock(Identifier identifier, Material material) {
        super(identifier, material);
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new ManagedInventoryBlockEntity();
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        if (world.getBlockEntity(x, y, z) instanceof ManagedInventoryBlockEntity inventory) {
            GuiHelper.openGUI(player, NyaLibTest.NAMESPACE.id( "managed_inventory"), inventory, new ManagedInventoryScreenHandler(player, inventory));
        }

        return false;
    }
}
