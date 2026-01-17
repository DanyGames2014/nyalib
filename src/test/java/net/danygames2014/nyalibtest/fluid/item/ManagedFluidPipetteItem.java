package net.danygames2014.nyalibtest.fluid.item;

import net.danygames2014.nyalib.fluid.FluidStack;
import net.danygames2014.nyalib.fluid.Fluids;
import net.danygames2014.nyalib.fluid.item.ManagedFluidHandlerItem;
import net.danygames2014.nyalibtest.NyaLibTest;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ManagedFluidPipetteItem extends TemplateItem implements ManagedFluidHandlerItem, CustomTooltipProvider {
    public ManagedFluidPipetteItem(Identifier identifier) {
        super(identifier);
        this.addSlot(2000).setAllowedFluids(Fluids.WATER);
        this.addSlot(1877).setAllowedFluids(NyaLibTest.fuelFluid);
        this.addSlot(9000);
    }

    @Override
    public @NotNull String[] getTooltip(ItemStack stack, String originalTooltip) {
        ArrayList<String> tooltip = new ArrayList<>();
        tooltip.add(originalTooltip);
        
        for (FluidStack fluidStack : this.getFluids(stack)) {
            if (fluidStack == null) {
                continue;
            }
            
            tooltip.add(fluidStack.toString());
        }
        
        return tooltip.toArray(new String[0]);
    }
}
