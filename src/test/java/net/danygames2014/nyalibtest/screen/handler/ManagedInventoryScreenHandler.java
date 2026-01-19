package net.danygames2014.nyalibtest.screen.handler;

import net.danygames2014.nyalibtest.item.ManagedInventoryBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class ManagedInventoryScreenHandler extends ScreenHandler {
    public ManagedInventoryBlockEntity blockEntity;
    public PlayerEntity player;
    public Inventory playerInventory;
    
    int builderStatus = 0;
    int remainingBlocks = 0;

    public ManagedInventoryScreenHandler(PlayerEntity player, ManagedInventoryBlockEntity blockEntity) {
        this.player = player;
        this.playerInventory = player.inventory;
        this.blockEntity = blockEntity;

        int playerInventoryVerticalOffset = 140;
        int playerInventoryHorizontalOffset = 0;

        // Player Inventory
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new Slot(playerInventory,
                                column + (row * 9) + 9,
                                playerInventoryHorizontalOffset + 8 + (column * 18),
                                playerInventoryVerticalOffset + (row * 18)
                        )
                );
            }
        }

        // Player Hotbar
        for (int column = 0; column < 9; column++) {
            this.addSlot(new Slot(playerInventory,
                            column,
                            playerInventoryHorizontalOffset + 8 + (column * 18),
                            playerInventoryVerticalOffset + 58
                    )
            );
        }
        
        this.addSlot(new Slot(blockEntity, 0, 80, 27));
        
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new Slot(blockEntity, 
                        column + (row * 9) + 1, 
                        8 + column * 18, 
                        72 + row * 18
                ));
            }
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
