package net.danygames2014.nyalib.mixin.item;

import net.danygames2014.nyalib.item.InventoryManager;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public class BlockEntityMixin {
    @Unique
    private InventoryManager inventoryManager;
    
    @SuppressWarnings("MissingUnique")
    public InventoryManager getInventoryManager() {
        if (inventoryManager == null) {
            inventoryManager = new InventoryManager();
        }
        
        return inventoryManager;
    }

    @Inject(method = "writeNbt", at = @At(value = "TAIL"))
    public void writeManagedInventoryNbt(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound managedInventoryNbt = new NbtCompound();
        getInventoryManager().writeNbt(managedInventoryNbt);
        nbt.put("ManagedInventoryData", managedInventoryNbt);
    }

    @Inject(method = "readNbt", at = @At(value = "TAIL"))
    public void readManagedInventoryNbt(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound managedTankNbt = nbt.getCompound("ManagedInventoryData");
        getInventoryManager().readNbt(managedTankNbt);
    }
}
