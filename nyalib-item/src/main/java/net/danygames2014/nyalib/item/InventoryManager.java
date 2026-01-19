package net.danygames2014.nyalib.item;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class InventoryManager {
    private ItemSlotEntry[] itemSlotEntries;
    private final boolean forbidSideChecks;
    
    public InventoryManager(boolean forbidSideChecks) {
        itemSlotEntries = new ItemSlotEntry[0];
        this.forbidSideChecks = forbidSideChecks;
    }
    
    public InventoryManager() {
        this(false);
    }
    
    public ItemSlotEntry addSlot(int stackSize) {
        ItemSlotEntry entry = new ItemSlotEntry(stackSize);
        entry.forbidSideChecks = forbidSideChecks;
        
        itemSlotEntries = Arrays.copyOf(itemSlotEntries, itemSlotEntries.length + 1);
        itemSlotEntries[itemSlotEntries.length - 1] = entry;
        
        return entry;
    }
    
    public void addSlot(ItemSlotEntry entry) {
        itemSlotEntries = Arrays.copyOf(itemSlotEntries, itemSlotEntries.length + 1);
        itemSlotEntries[itemSlotEntries.length - 1] = entry;
    }
    
    public ItemSlotEntry getSlot(int slot) {
        return getSlot(slot, null);
    }
    
    public ItemSlotEntry getSlot(int slot, @Nullable Direction side) {
        if (slot > itemSlotEntries.length - 1 || slot < 0) {
            return null;
        }
        
        if (!itemSlotEntries[slot].isSideAllowed(side)) {
            return null;
        }
        
        return itemSlotEntries[slot];
    }
    
    // ItemHandler Methods
    public ItemStack getItem(int slot, @Nullable Direction side) {
        ItemSlotEntry entry = getSlot(slot, side);

        if (entry == null) {
            return null;
        }
        
        return entry.stack;
    }
    
    public boolean setItem(ItemStack stack, int slot, @Nullable Direction side) {
        ItemSlotEntry entry = getSlot(slot, side);
        
        if (entry == null) {
            return false;
        }
        
        entry.stack = stack;
        return true;
    }
    
    public int getItemSlots(@Nullable Direction side) {
        int slotCount = 0;
        
        for (ItemSlotEntry entry : itemSlotEntries) {
            if (entry.isSideAllowed(side)) {
                slotCount += entry.maxStackSize;
            }
        }
        
        return slotCount;
    }
    
    public ItemStack[] getInventory(@Nullable Direction side) {
        ObjectArrayList<ItemStack> stacks = new ObjectArrayList<>();
        
        for (ItemSlotEntry entry : itemSlotEntries) {
            if (entry.isSideAllowed(side)) {
                stacks.add(entry.stack);
            } else {
                stacks.add(null);
            }
        }
        
        return stacks.toArray(ItemStack[]::new);
    }

    // NBT
    public void writeNbt(NbtCompound nbt) {
        NbtList entriesList = new NbtList();

        for (int slot = 0; slot < itemSlotEntries.length; slot++) {
            NbtCompound entryNbt = new NbtCompound();
            entryNbt.putInt("Slot", slot);
            itemSlotEntries[slot].writeNbt(entryNbt);
            entriesList.add(entryNbt);
        }

        nbt.put("ItemSlots", entriesList);
    }

    public void readNbt(NbtCompound nbt) {
        NbtList entriesList = nbt.getList("ItemSlots");

        for (int i = 0; i < entriesList.size(); i++) {
            NbtCompound entryNbt = (NbtCompound) entriesList.get(i);
            if (entryNbt.getInt("Slot") < itemSlotEntries.length) {
                itemSlotEntries[entryNbt.getInt("Slot")].readNbt(entryNbt);
            }
        }
    }
    
    public static class ItemSlotEntry {
        public ObjectArrayList<Item> allowedItems;
        public ObjectArrayList<Direction> allowedSides;
        public int maxStackSize;
        
        public ItemStack stack;
        
        public boolean forbidSideChecks = false;
     
        public ItemSlotEntry(int maxStackSize) {
            this(maxStackSize, new Item[0]);
        }
        
        public ItemSlotEntry(int maxStackSize, Item... allowedItems) {
            this.maxStackSize = maxStackSize;
            if (allowedItems.length > 0) {
                this.allowedItems = new ObjectArrayList<>(allowedItems);
            }
        }
        
        public ItemSlotEntry setAllowedSides(Direction... allowedSides) {
            if (forbidSideChecks) {
                throw new UnsupportedOperationException("Setting allowed sides is not applicable for this ItemSlotEntry.");
            }
            
            this.allowedSides = new ObjectArrayList<>(allowedSides);
            return this;
        }
        
        public ItemSlotEntry setAllowedItems(Item... allowedItems) {
            this.allowedItems = new ObjectArrayList<>(allowedItems);
            return this;
        }
        
        // Allow checks
        public boolean isItemAllowed(Item item) {
            return allowedItems == null || allowedItems.contains(item);
        }
        
        public boolean isSideAllowed(@Nullable Direction side) {
            return side == null || allowedSides == null || allowedSides.contains(side);
        }
        
        // NBT
        public void writeNbt(NbtCompound nbt) {
            if (stack != null) {
                nbt.put("ItemStack", stack.writeNbt(new NbtCompound()));
            }
        }
        
        public void readNbt(NbtCompound nbt) {
            if (nbt.contains("ItemStack")) {
                stack = new ItemStack(nbt.getCompound("ItemStack"));
                stack.count = Math.min(stack.count, maxStackSize);
            }
        }
        
        public ItemSlotEntry copy(boolean withStack) {
            ItemSlotEntry entry = new ItemSlotEntry(maxStackSize);
            entry.allowedItems = allowedItems == null ? null : new ObjectArrayList<>(allowedItems);
            entry.allowedSides = allowedSides == null ? null : new ObjectArrayList<>(allowedSides);
            
            if (withStack) {
                entry.stack = stack == null ? null : stack.copy();
            }
            
            return entry;
        }
    }
}
