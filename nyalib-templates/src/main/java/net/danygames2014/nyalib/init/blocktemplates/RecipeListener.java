package net.danygames2014.nyalib.init.blocktemplates;

import net.danygames2014.nyalib.registry.TemplateRecipeRegistry;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.recipe.CraftingRegistry;

public class RecipeListener {
    public boolean registeredCallbacks = false;
    
    @EventListener
    public void registerRecipes(RecipeRegisterEvent event) {
        if (!registeredCallbacks) {
            for (var callback : TemplateRecipeRegistry.recipeCallbacks) {
                callback.run();
            }
            registeredCallbacks = true;
        }
        
        RecipeRegisterEvent.Vanilla type = RecipeRegisterEvent.Vanilla.fromType(event.recipeId);
        
        if (type == RecipeRegisterEvent.Vanilla.CRAFTING_SHAPED) {
            for (var entry : TemplateRecipeRegistry.shapedEntries) {
                CraftingRegistry.addShapedRecipe(entry.output, entry.ingredients);
            }
        }

        if (type == RecipeRegisterEvent.Vanilla.CRAFTING_SHAPELESS) {
            for (var entry : TemplateRecipeRegistry.shapelessEntries) {
                CraftingRegistry.addShapelessRecipe(entry.output, entry.ingredients);
            }
        }
    }
}
