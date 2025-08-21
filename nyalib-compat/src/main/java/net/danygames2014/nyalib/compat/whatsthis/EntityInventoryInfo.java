package net.danygames2014.nyalib.compat.whatsthis;

import net.danygames2014.nyalib.capability.CapabilityHelper;
import net.danygames2014.nyalib.capability.entity.itemhandler.ItemHandlerEntityCapability;
import net.danygames2014.whatsthis.Util;
import net.danygames2014.whatsthis.api.*;
import net.danygames2014.whatsthis.apiimpl.styles.ItemStyle;
import net.danygames2014.whatsthis.apiimpl.styles.LayoutStyle;
import net.danygames2014.whatsthis.config.Config;
import net.danygames2014.whatsthis.config.ConfigSetup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.danygames2014.whatsthis.api.TextStyleClass.INFO;

public class EntityInventoryInfo {

    static void showInventoryInfo(ProbeMode mode, IProbeInfo probeInfo, World world, Entity entity, IProbeConfig config) {
        IProbeConfig.ConfigMode chestMode = config.getShowChestContents();
        List<ItemStack> stacks = null;

        if (chestMode == IProbeConfig.ConfigMode.EXTENDED && (Config.PROBE_CONFIG.showSmallChestContentsWithoutSneaking > 0 || !ConfigSetup.getInventoriesToShow().isEmpty())) {
            if (ConfigSetup.getInventoriesToShow().contains(Identifier.of(EntityRegistry.getId(entity)))) {
                chestMode = IProbeConfig.ConfigMode.NORMAL;
            } else if (Config.PROBE_CONFIG.showSmallChestContentsWithoutSneaking > 0) {
                stacks = new ArrayList<>();
                int slots = getInventoryContents(world, entity, stacks);
                if (slots <= Config.PROBE_CONFIG.showSmallChestContentsWithoutSneaking) {
                    chestMode = IProbeConfig.ConfigMode.NORMAL;
                }
            }
        } else if (chestMode == IProbeConfig.ConfigMode.NORMAL && !ConfigSetup.getInventoriesToNotShow().isEmpty()) {
            if (ConfigSetup.getInventoriesToNotShow().contains(Identifier.of(EntityRegistry.getId(entity)))) {
                chestMode = IProbeConfig.ConfigMode.EXTENDED;
            }
        }

        if (Util.show(mode, chestMode)) {
            if (stacks == null) {
                stacks = new ArrayList<>();
                getInventoryContents(world, entity, stacks);
            }

            if (!stacks.isEmpty()) {
                boolean showDetailed = Util.show(mode, config.getShowChestContentsDetailed()) && stacks.size() <= Config.PROBE_CONFIG.showItemDetailThreshold;
                showInventoryContents(probeInfo, world, entity, stacks, showDetailed);
            }
        }
    }

    private static void showInventoryContents(IProbeInfo probeInfo, World world, Entity entity, List<ItemStack> stacks, boolean detailed) {
        IProbeInfo vertical = null;
        IProbeInfo horizontal = null;

        int rows = 0;
        int idx = 0;

        vertical = probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(Config.parseColor(Config.CLIENT_CONFIG.chestContentsBorderColor)).spacing(0));

        if (detailed) {
            for (ItemStack stackInSlot : stacks) {
                horizontal = vertical.horizontal(new LayoutStyle().spacing(10).alignment(ElementAlignment.ALIGN_CENTER));
                horizontal.item(stackInSlot, new ItemStyle().width(16).height(16))
                        .text(INFO + stackInSlot.getItem().getTranslatedName());
            }
        } else {
            for (ItemStack stackInSlot : stacks) {
                if (idx % 10 == 0) {
                    horizontal = vertical.horizontal(new LayoutStyle().spacing(0));
                    rows++;
                    if (rows > 4) {
                        break;
                    }
                }
                horizontal.item(stackInSlot);
                idx++;
            }
        }
    }

    /**
     * Gets the inventory contents and adds them to the stack list provided
     *
     * @param world  The world the inventory is in
     * @param stacks The List of stacks to add the stacks into
     * @return The size of the inventory in slots
     */
    private static int getInventoryContents(World world, Entity entity, List<ItemStack> stacks) {
        // If we want to compact equal stacks, we initialize the HashSet. If not, we leave it null
        Set<Item> foundItems = Config.CLIENT_CONFIG.compactEqualStacks ? new HashSet<>() : null;

        int inventorySize = 0;

        try {
            ItemHandlerEntityCapability itemHandler = CapabilityHelper.getCapability(entity, ItemHandlerEntityCapability.class);
            if (itemHandler != null) {
                inventorySize = itemHandler.getItemSlots();
                for (int i = 0; i < inventorySize; i++) {
                    addItemStack(stacks, foundItems, itemHandler.getItem(i));
                }
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Getting the contents of a " + EntityRegistry.getId(entity) + " (" + entity.getClass().getName() + ")", e);
        }

        return inventorySize;
    }

    /**
     * Adds the given stack to the List of stacks provided.
     * <p>If the given List of foundItems is not null, it will try to match the stack with an already existing stack and if that fails, it will append a new one
     *
     * @param stacks     The List of stacks to add the stack to
     * @param foundItems The list of already found items
     * @param stack      The stack to add to the list of stacks
     */
    private static void addItemStack(List<ItemStack> stacks, Set<Item> foundItems, ItemStack stack) {
        // Check the validity of the stack
        if (stack == null || stack.getItem() == null || stack.count <= 0) {
            return;
        }

        // If foundItems isn't null, check if there exists a stack to group it with
        if (foundItems != null && foundItems.contains(stack.getItem())) {
            // Loop thru the found stacks to find a matching one
            for (ItemStack s : stacks) {
                // If the stacks match, add them together
                if (s.isItemEqual(stack)) {
                    s.count += stack.count;
                    return;
                }
            }
        }

        // If we arrive here, it means that we either haven't found a matching stack, or we don't want to compact them
        // either way we just append the stack to the list
        stacks.add(stack.copy());
        if (foundItems != null) {
            foundItems.add(stack.getItem());
        }
    }
}
