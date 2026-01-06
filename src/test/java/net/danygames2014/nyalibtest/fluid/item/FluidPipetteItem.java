package net.danygames2014.nyalibtest.fluid.item;

import net.danygames2014.nyalib.fluid.FluidHandlerItem;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class FluidPipetteItem extends TemplateItem implements FluidHandlerItem, CustomTooltipProvider {
    public FluidPipetteItem(Identifier identifier) {
        super(identifier);
    }
    
    @Override
    public boolean canExtractFluid(ItemStack thiz) {
        return true;
    }

    @Override
    public FluidStack extractFluid(ItemStack thiz, int slot, int amount) {
        FluidStack currentStack = FluidStack.fromNbt(thiz.getStationNbt().getCompound("fluid"));
        
        if (currentStack == null) {
            return null;
        }
        
        FluidStack extractedStack = currentStack.copy();
        extractedStack.amount = Math.min(extractedStack.amount, amount);
        currentStack.amount -= extractedStack.amount;
        
        if (currentStack.amount <= 0) {
            thiz.getStationNbt().put("fluid", new NbtCompound());
        } else {
            thiz.getStationNbt().put("fluid", currentStack.writeNbt(new NbtCompound()));
        }
        
        return extractedStack;
    }

    @Override
    public boolean canInsertFluid(ItemStack thiz) {
        return true;
    }

    @Override
    public FluidStack insertFluid(ItemStack thiz, FluidStack stack, int slot) {
        FluidStack currentStack = FluidStack.fromNbt(thiz.getStationNbt().getCompound("fluid"));
        int remaining = stack.amount;

        if (currentStack == null) {
            currentStack = new FluidStack(stack.fluid, 0);
        }

        int addedAmount = Math.min(remaining, getFluidCapacity(thiz, slot) - currentStack.amount);
        currentStack.amount += addedAmount;
        remaining -= addedAmount;

        thiz.getStationNbt().put("fluid", currentStack.writeNbt(new NbtCompound()));

        if (remaining > 0) {
            stack.amount -= remaining;
            return new FluidStack(stack.fluid, remaining);
        }

        return null;
    }

    @Override
    public FluidStack insertFluid(ItemStack thiz, FluidStack stack) {
        return insertFluid(thiz, stack, 0);
    }

    @Override
    public FluidStack getFluid(ItemStack thiz, int slot) {
        return FluidStack.fromNbt(thiz.getStationNbt().getCompound("fluid"));
    }

    @Override
    public boolean setFluid(ItemStack thiz, int slot, FluidStack stack) {
        thiz.getStationNbt().put("fluid", stack.writeNbt(new NbtCompound()));
        return true;
    }

    @Override
    public int getFluidSlots(ItemStack thiz) {
        return 1;
    }

    @Override
    public int getFluidCapacity(ItemStack thiz, int slot) {
        return 5000;
    }

    @Override
    public FluidStack[] getFluids(ItemStack thiz) {
        if (thiz.getStationNbt().contains("fluid")) {
            return new FluidStack[] {
                    FluidStack.fromNbt(thiz.getStationNbt().getCompound("fluid"))
            };
        } else {
            return new FluidStack[1];
        }
    }

    @Override
    public @NotNull String[] getTooltip(ItemStack stack, String originalTooltip) {
        FluidStack fluidStack = FluidStack.fromNbt(stack.getStationNbt().getCompound("fluid"));
        
        if (fluidStack == null) {
            return new String[] { 
                    originalTooltip 
            };
        }
        
        return new String[] {
                originalTooltip,
                "Fluid: " + fluidStack.fluid.getTranslatedName(),
                "Amount: " + fluidStack.amount
        };
    }
}
