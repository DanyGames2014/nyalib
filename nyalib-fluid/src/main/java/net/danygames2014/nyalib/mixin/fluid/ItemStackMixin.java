package net.danygames2014.nyalib.mixin.fluid;

import net.danygames2014.nyalib.fluid.TankManager;
import net.danygames2014.nyalib.fluid.item.ManagedFluidHandlerItem;
import net.danygames2014.nyalib.mixininterface.ItemFluidSlotTemplateRetriever;
import net.danygames2014.nyalib.mixininterface.ItemStackTankManagerRetriever;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.item.StationItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemStack.class, priority = 1100)
public abstract class ItemStackMixin implements ItemStackTankManagerRetriever, StationItemStack {
    @Shadow
    public abstract Item getItem();

    @Unique
    private TankManager tankManager;

    @Override
    public TankManager nyalib$getTankManager() {
        return tankManager;
    }

    @Override
    public void nyalib$setTankManager(TankManager tankManager) {
        this.tankManager = tankManager;
    }

    @Inject(method = "<init>(III)V", at = @At("TAIL"))
    public void initTankManager(int id, int count, int damage, CallbackInfo ci) {
        if (this.getItem() instanceof ManagedFluidHandlerItem && tankManager == null) {
            tankManager = new TankManager(true);

            ItemFluidSlotTemplateRetriever templateEntries = (ItemFluidSlotTemplateRetriever) this.getItem();
            for (var entry : templateEntries.nyalib$getTemplateFluidSlotEntries()) {
                tankManager.addSlot(entry.copy(false));
            }

            if (this.getStationNbt() != null && this.getStationNbt().contains("ManagedTankData")) {
                tankManager.readNbt(this.getStationNbt().getCompound("ManagedTankData"));
            }
        }
    }

    @Inject(
            method = "copy",
            at = @At("RETURN")
    )
    private void copyManagedTank(CallbackInfoReturnable<ItemStack> cir) {
        if (this.getItem() instanceof ManagedFluidHandlerItem && tankManager != null) {
            //noinspection DataFlowIssue
            ItemStackTankManagerRetriever.class.cast(cir.getReturnValue()).nyalib$setTankManager(this.tankManager);
        }
    }

    @Inject(
            method = "split",
            at = @At("RETURN")
    )
    private void splitStackTransferManagedTank(int par1, CallbackInfoReturnable<ItemStack> cir) {
        if (this.getItem() instanceof ManagedFluidHandlerItem && tankManager != null) {
            //noinspection DataFlowIssue
            ItemStackTankManagerRetriever.class.cast(cir.getReturnValue()).nyalib$setTankManager(this.tankManager);
        }
    }

    @Inject(method = "writeNbt", at = @At(value = "RETURN"))
    public void writeManagedTankNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if (this.getItem() instanceof ManagedFluidHandlerItem) {
            NbtCompound managedTankNbt = new NbtCompound();
            tankManager.writeNbt(managedTankNbt);
            this.getStationNbt().put("ManagedTankData", managedTankNbt);
        }
    }

    @Inject(method = "readNbt", at = @At(value = "RETURN"))
    public void readManagedTankNbt(NbtCompound nbt, CallbackInfo ci) {
        if (this.getItem() instanceof ManagedFluidHandlerItem) {
            NbtCompound managedTankNbt = this.getStationNbt().getCompound("ManagedTankData");
            tankManager = new TankManager();

            ItemFluidSlotTemplateRetriever templateEntries = (ItemFluidSlotTemplateRetriever) this.getItem();
            for (var entry : templateEntries.nyalib$getTemplateFluidSlotEntries()) {
                tankManager.addSlot(entry.copy(false));
            }

            tankManager.readNbt(managedTankNbt);
        }
    }
}
