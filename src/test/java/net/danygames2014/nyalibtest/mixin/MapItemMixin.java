package net.danygames2014.nyalibtest.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.item.Item;
import net.minecraft.item.MapItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MapItem.class)
public class MapItemMixin extends Item {
    public MapItemMixin(int id) {
        super(id);
    }
    
    @ModifyExpressionValue(method = "onCraft", at = @At(value = "CONSTANT", args = "intValue=3"))
    public int changeScaleOnCraft(int original) {
        return 1;
    }
    
    @ModifyExpressionValue(method = "getSavedMapState", at = @At(value = "CONSTANT", args = "intValue=3"))
    public int changeScaleOnGetSavedMapState(int original) {
        return 1;
    }
}
