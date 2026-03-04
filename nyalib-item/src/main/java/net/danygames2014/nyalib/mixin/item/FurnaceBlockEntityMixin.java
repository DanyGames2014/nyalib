package net.danygames2014.nyalib.mixin.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.danygames2014.nyalib.item.HasSmeltingReturnStack;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FurnaceBlockEntity.class)
public class FurnaceBlockEntityMixin {
    @Shadow
    private ItemStack[] inventory;

    @WrapOperation(method = "craftRecipe", at = @At(value = "FIELD", target = "Lnet/minecraft/item/ItemStack;count:I", opcode = Opcodes.PUTFIELD, ordinal = 1))
    public void injectReturnStack(ItemStack stack, int value, Operation<Void> original) {
        if (stack.getItem() instanceof HasSmeltingReturnStack hasSmeltReturnItem) {
            ItemStack returnStack = hasSmeltReturnItem.getSmeltingReturnStack(stack);
            
            if (stack != returnStack) {
                this.inventory[0] = returnStack;
                return;
            }
        }
        
        original.call(stack, value);
    }
}
