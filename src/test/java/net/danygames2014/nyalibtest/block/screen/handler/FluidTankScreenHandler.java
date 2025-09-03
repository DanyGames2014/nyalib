package net.danygames2014.nyalibtest.block.screen.handler;

import net.danygames2014.nyalib.fluid.FluidSlot;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.danygames2014.nyalibtest.block.fluid.entity.FluidTankBlockEntity;
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

        this.addFluidSlot(new FluidSlot(fluidTank, 0, 56, 17));
        this.addFluidSlot(new FluidSlot(fluidTank, 1, 56, 30));
        this.addFluidSlot(new FluidSlot(fluidTank, 2, 56, 45));
    }

    @Override
    public FluidStack onFluidSlotClick(int index, int button, boolean shift, PlayerEntity player, ItemStack cursorStack) {
        if (getFluidSlot(index).hasStack()) {
            if (button == 0) {
                getFluidSlot(index).getStack().amount += 500;
                return getFluidSlot(index).getStack();
            } else if (button == 1) {
                getFluidSlot(index).getStack().amount -= 500;
                return getFluidSlot(index).getStack();
            }
        }

        return super.onFluidSlotClick(index, button, shift, player, cursorStack);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
