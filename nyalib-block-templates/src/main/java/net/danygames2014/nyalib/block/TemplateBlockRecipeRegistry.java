package net.danygames2014.nyalib.block;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class TemplateBlockRecipeRegistry {
    public static final ArrayList<Entry> shapedEntries = new ArrayList<>();
    public static final ArrayList<Entry> shapelessEntries = new ArrayList<>();
    public static final ArrayList<Runnable> recipeCallbacks = new ArrayList<>();
    
    public static void registerRecipeCallback(Runnable runnable) {
        recipeCallbacks.add(runnable);
    }
    
    public static void registerShaped(ItemStack output, Object... input) {
        shapedEntries.add(new Entry(output, input));
    }
    
    public static void registerShapeless(ItemStack output, Object... ingredients) {
        shapelessEntries.add(new Entry(output, ingredients));
    }
    
    @SuppressWarnings("ClassCanBeRecord")
    public static class Entry {
        public final ItemStack output;
        public final Object[] ingredients;

        public Entry(ItemStack output, Object... ingredients) {
            this.output = output;
            this.ingredients = ingredients;
        }
    }
}

