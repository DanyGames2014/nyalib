package net.danygames2014.nyalib.mixin.fluid;

import net.danygames2014.nyalib.fluid.TankManager;
import net.danygames2014.nyalib.fluid.item.ManagedFluidHandlerItem;
import net.danygames2014.nyalib.mixininterface.ItemStackTankManagerRetriever;
import net.danygames2014.nyalib.mixininterface.ItemTemplateSlotRetriever;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackTankManagerRetriever {
    @Shadow
    public abstract Item getItem();

    @Unique
    private TankManager tankManager;

    @Override
    public TankManager nyalib$getTankManager() {
        return tankManager;
    }

    @Inject(method = "<init>(III)V", at = @At("TAIL"))
    public void initTankManager(int id, int count, int damage, CallbackInfo ci) {
        if (this.getItem() instanceof ManagedFluidHandlerItem) {
            tankManager = new TankManager(true);

            ItemTemplateSlotRetriever templateEntries = (ItemTemplateSlotRetriever) this.getItem();
            for (var entry : templateEntries.nyalib$getTemplateSlotEntries()) {
                tankManager.addSlot(entry.copy());
            }
        }
    }
    
    @Inject(method = "writeNbt", at = @At(value = "RETURN"))
    public void writeManagedTankNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if (this.getItem() instanceof ManagedFluidHandlerItem) {
            NbtCompound managedTankNbt = new NbtCompound();
            tankManager.writeNbt(managedTankNbt);
            nbt.put("ManagedTankData", managedTankNbt);
        }
    }

    @Inject(method = "readNbt", at = @At(value = "RETURN"))
    public void readManagedTankNbt(NbtCompound nbt, CallbackInfo ci) {
        if (this.getItem() instanceof ManagedFluidHandlerItem) {
            NbtCompound managedTankNbt = nbt.getCompound("ManagedTankData");
            tankManager = new TankManager();

            ItemTemplateSlotRetriever templateEntries = (ItemTemplateSlotRetriever) this.getItem();
            for (var entry : templateEntries.nyalib$getTemplateSlotEntries()) {
                tankManager.addSlot(entry);
            }
            
            tankManager.readNbt(managedTankNbt);
        }
    }
    
    // TODO: mix into comaprison methods
}
