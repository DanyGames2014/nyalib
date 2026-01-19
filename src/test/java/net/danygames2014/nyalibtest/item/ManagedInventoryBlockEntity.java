package net.danygames2014.nyalibtest.item;

import net.danygames2014.nyalib.item.block.ManagedItemHandlerWithInventory;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;

public class ManagedInventoryBlockEntity extends BlockEntity implements ManagedItemHandlerWithInventory {
    public ManagedInventoryBlockEntity() {
        for (int i = 0; i < 27; i++) {
            this.addItemSlot();
        }
    }

    @Override
    public String getName() {
        return "Managed Inventory";
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }
}
