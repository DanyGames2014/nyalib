package net.danygames2014.nyalibtest.screen.handler;

import net.danygames2014.nyalib.fluid.FluidSlot;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.danygames2014.nyalibtest.fluid.entity.FluidTankBlockEntity;
import net.danygames2014.nyalibtest.screen.slot.CustomFluidSlotWithRendering;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class FluidTankScreenHandler extends ScreenHandler {
    public PlayerEntity player;
    public Inventory playerInventory;

    public FluidTankBlockEntity fluidTank;

    public FluidTankScreenHandler(PlayerEntity player, FluidTankBlockEntity fluidTank) {
        this.player = player;
        this.playerInventory = player.inventory;
        this.fluidTank = fluidTank;

        int playerInventoryVerticalOffset = 168 / 2;
        int playerInventoryHorizontalOffset = 0;

        int row;
        int column;

        // Player Inventory
        for (row = 0; row < 3; row++) {
            for (column = 0; column < 9; column++) {
                this.addSlot(new Slot(playerInventory,
                                column + (row * 9) + 9,
                                playerInventoryHorizontalOffset + 8 + (column * 18),
                                playerInventoryVerticalOffset + (row * 18)
                        )
                );
            }
        }

        // Player Hotbar
        for (row = 0; row < 9; row++) {
            this.addSlot(new Slot(playerInventory,
                            row,
                            playerInventoryHorizontalOffset + 8 + (row * 18),
                            playerInventoryVerticalOffset + 58
                    )
            );
        }

        this.addFluidSlot(new CustomFluidSlotWithRendering(fluidTank, 0, 56, 17));
        this.addFluidSlot(new FluidSlot(fluidTank, 1, 56, 35, 48, 16));
        this.addFluidSlot(new FluidSlot(fluidTank, 2, 56, 53));
        
        //this.getFluidSlots().get(1).enabled = false;
    }

    @Override
    public FluidStack onFluidSlotClick(int index, int button, boolean shift, PlayerEntity player, ItemStack cursorStack) {
        FluidSlot slot = getFluidSlot(index);

        if (cursorStack == null) {
            if (slot != null && slot.hasStack()) {
                if (button == 0) {
                    slot.setFluidAmount(slot.getFluidAmount() + 500);
                    return slot.getStack();
                } else if (button == 1) {
                    slot.setFluidAmount(slot.getFluidAmount() - 500);
                    return slot.getStack();
                }
            }
        }

        return super.onFluidSlotClick(index, button, shift, player, cursorStack);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
