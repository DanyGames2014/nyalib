package net.danygames2014.nyalib.mixin.item;

import net.danygames2014.nyalib.item.InventoryManager;
import net.danygames2014.nyalib.item.item.ManagedItemHandlerItem;
import net.danygames2014.nyalib.mixininterface.ItemItemSlotTemplateRetriever;
import net.danygames2014.nyalib.mixininterface.ItemStackInventoryManagerRetriever;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.item.StationItemStack;
import net.modificationstation.stationapi.impl.item.StationNBTSetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemStack.class, priority = 1100)
public abstract class ItemStackMixin implements ItemStackInventoryManagerRetriever, StationItemStack, StationNBTSetter {
    @Shadow
    public abstract Item getItem();

    @Unique
    private InventoryManager inventoryManager;

    @Override
    public InventoryManager nyalib$getInventoryManager() {
        return inventoryManager;
    }

    @Override
    public void nyalib$setInventoryManager(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    @Inject(method = "<init>(III)V", at = @At("TAIL"))
    public void initinventoryManager(int id, int count, int damage, CallbackInfo ci) {
        if (this.getItem() instanceof ManagedItemHandlerItem && inventoryManager == null) {
            inventoryManager = new InventoryManager(true);

            ItemItemSlotTemplateRetriever templateEntries = (ItemItemSlotTemplateRetriever) this.getItem();
            for (var entry : templateEntries.nyalib$getTemplateItemSlotEntries()) {
                inventoryManager.addSlot(entry.copy(false));
            }

            if (this.getStationNbt() != null && this.getStationNbt().contains("ManagedInventoryData")) {
                inventoryManager.readNbt(this.getStationNbt().getCompound("ManagedInventoryData"));
            }
        }
    }

    @Inject(
            method = "copy",
            at = @At("RETURN")
    )
    private void copyTransferManagedInventory(CallbackInfoReturnable<ItemStack> cir) {
        if (this.getItem() instanceof ManagedItemHandlerItem && inventoryManager != null) {
            //noinspection DataFlowIssue
            ItemStackInventoryManagerRetriever.class.cast(cir.getReturnValue()).nyalib$setInventoryManager(this.inventoryManager);
        }
    }

    @Inject(
            method = "split",
            at = @At("RETURN")
    )
    private void splitStackTransferManagedInventory(int par1, CallbackInfoReturnable<ItemStack> cir) {
        if (this.getItem() instanceof ManagedItemHandlerItem && inventoryManager != null) {
            //noinspection DataFlowIssue
            ItemStackInventoryManagerRetriever.class.cast(cir.getReturnValue()).nyalib$setInventoryManager(this.inventoryManager);
        }
    }

    @Inject(method = "writeNbt", at = @At(value = "RETURN"))
    public void writeManagedInventoryNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if (this.getItem() instanceof ManagedItemHandlerItem) {
            NbtCompound managedInventoryNbt = new NbtCompound();
            inventoryManager.writeNbt(managedInventoryNbt);
            this.getStationNbt().put("ManagedInventoryData", managedInventoryNbt);
        }
    }

    @Inject(method = "readNbt", at = @At(value = "RETURN"))
    public void readManagedInventoryNbt(NbtCompound nbt, CallbackInfo ci) {
        if (this.getItem() instanceof ManagedItemHandlerItem) {
            NbtCompound managedInventoryNbt = this.getStationNbt().getCompound("ManagedInventoryData");
            inventoryManager = new InventoryManager();

            ItemItemSlotTemplateRetriever templateEntries = (ItemItemSlotTemplateRetriever) this.getItem();
            for (var entry : templateEntries.nyalib$getTemplateItemSlotEntries()) {
                inventoryManager.addSlot(entry.copy(false));
            }

            inventoryManager.readNbt(managedInventoryNbt);
        }
    }
}
