package net.danygames2014.nyalibtest.init;

import net.danygames2014.nyalib.block.JsonOverrideRegistry;
import net.danygames2014.nyalibtest.fluid.item.FluidCellItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.color.item.ItemColorProvider;
import net.modificationstation.stationapi.api.client.event.color.item.ItemColorsRegisterEvent;
import net.modificationstation.stationapi.api.util.Identifier;

public class ClientListener {
    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerItemColor(ItemColorsRegisterEvent event) {
        for (FluidCellItem fluidCell : FluidCellItem.FLUID_CELL_ITEMS.values()) {
            event.itemColors.register(new ItemColorProvider() {
                @Override
                public int getColor(ItemStack stack, int tintIndex) {
                    if (stack.getItem() instanceof FluidCellItem fluidCellItem && fluidCell.fluid != null) {
                        return fluidCellItem.getFluid().getColor();
                    }
                    
                    return 0xAAFFFFFF;
                }
            }, fluidCell);
        }
    }
    
    public static void registerCellTexture(Identifier identifier) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            return;
        }
        
        JsonOverrideRegistry.registerItemModelOverride(identifier, cellJson);
    }
    
    public static String cellJson = """
            {
              "parent": "nyalibtest:item/fluid_cell"
            }""";
}
