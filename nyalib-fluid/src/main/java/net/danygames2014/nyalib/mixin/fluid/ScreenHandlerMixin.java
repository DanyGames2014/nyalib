package net.danygames2014.nyalib.mixin.fluid;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.capability.CapabilityHelper;
import net.danygames2014.nyalib.capability.item.fluidhandler.FluidHandlerItemCapability;
import net.danygames2014.nyalib.fluid.*;
import net.danygames2014.nyalib.screen.FluidScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin implements FluidScreenHandler {
    @Shadow
    public abstract void sendContentUpdates();

    @Shadow
    protected List listeners;
    @Unique
    public ArrayList<FluidStack> trackedFluidStacks = new ArrayList<>();
    @Unique
    public ArrayList<FluidSlot> fluidSlots = new ArrayList<>();

    @Override
    public void addFluidSlot(FluidSlot slot) {
        slot.id = fluidSlots.size();
        fluidSlots.add(slot);
        trackedFluidStacks.add(null);
    }

    @Override
    public ArrayList<FluidSlot> getFluidSlots() {
        return fluidSlots;
    }

    @Override
    public ArrayList<FluidStack> getFluidStacks() {
        ArrayList<FluidStack> fluidStacks = new ArrayList<>();

        for (FluidSlot slot : fluidSlots) {
            fluidStacks.add(slot.getStack());
        }

        return fluidStacks;
    }

    @Inject(method = "sendContentUpdates", at = @At(value = "TAIL"))
    public void sendFluidContentUpdates(CallbackInfo ci) {
        for (int slot = 0; slot < this.fluidSlots.size(); ++slot) {
            FluidStack stack = this.fluidSlots.get(slot).getStack();
            FluidStack trackedStack = this.trackedFluidStacks.get(slot);
            if (!FluidStack.areEqual(trackedStack, stack)) {
                trackedStack = stack == null ? null : stack.copy();
                this.trackedFluidStacks.set(slot, trackedStack);

                for (Object listenerO : this.listeners) {
                    if (listenerO instanceof ScreenHandlerListener listener) {
                        listener.onFluidSlotUpdate((ScreenHandler) (Object) this, slot, trackedStack);
                    }
                }
            }
        }
    }

    @Override
    public FluidSlot getFluidSlot(int index) {
        if (index < 0 || index >= fluidSlots.size()) {
            return null;
        }

        return fluidSlots.get(index);
    }

    // TODO: This should probably return the cursor stack, since why would it return the fluid stack, literally not gonna get compared with client
    @Override
    public FluidStack onFluidSlotClick(int index, int button, boolean shift, PlayerEntity player, ItemStack cursorStack) {
        if (index == -999) {
            return null;
        }

        FluidSlot slot = fluidSlots.get(index);

        if (slot != null) {
            FluidHandler inv = slot.getHandler();

            if (cursorStack == null) {
                return slot.getStack();
            }

            // Item Capability
            FluidHandlerItemCapability item = CapabilityHelper.getCapability(cursorStack, FluidHandlerItemCapability.class);

            if (item != null) {
                FluidStack itemFluidStack = item.getFluid(0);
                FluidStack invFluidStack = inv.getFluid(index, null);

                if (itemFluidStack == null && invFluidStack == null) {
                    // Both are empty. Do Nothing

                } else if (itemFluidStack == null) {
                    // The item is empty. Fill It
                    FluidStack extractedStack = inv.extractFluid(index, item.getRemainingFluidCapacity(0), null);
                    FluidStack remainderStack = item.insertFluid(extractedStack);
                    if (remainderStack != null && remainderStack.amount > 0) {
                        inv.insertFluid(remainderStack, index, null);
                    }

                } else { // if (invFluidStack == null) {
                    // The inventory slot is empty. Fill It
                    FluidStack extractedStack = item.extractFluid(0, inv.getRemainingFluidCapacity(index, null));
                    FluidStack remainderStack = extractedStack;

                    if (slot.canInsert(remainderStack)) {
                        remainderStack = inv.insertFluid(extractedStack, index, null);
                    }

                    if (remainderStack != null && remainderStack.amount > 0) {
                        item.insertFluid(remainderStack, 0);
                    }

                }
            }

            // Bucket
            if (cursorStack.getItem() instanceof FluidBucket bucket) {
                Fluid bucketFluid = bucket.getFluid();
                FluidStack invFluidStack = inv.getFluid(index, null);

                if (bucketFluid == null && invFluidStack == null) {
                    // Both are empty. Do Nothing

                } else if (bucketFluid == null) {
                    // The bucket is empty, try to fill it

                    // If the fluid has a bucket item, try to form it
                    if (bucket.getFullBucketItem(invFluidStack.fluid) != null) {
                        int iterationLimit = 127;
                        while (iterationLimit-- > 0) {
                            if (inv.getFluid(index, null) == null) {
                                break;
                            }

                            // Check if there is enough fluid for another bucket
                            if (inv.getFluid(index, null).amount >= invFluidStack.fluid.getBucketSize()) {
                                FluidStack extractedStack = inv.extractFluid(index, invFluidStack.fluid.getBucketSize(), null);

                                if (extractedStack != null) {
                                    if (extractedStack.amount >= invFluidStack.fluid.getBucketSize()) {
                                        player.inventory.getCursorStack().count--;
                                        if (!pushItemBuffer(bucket.getFullBucketItem(invFluidStack.fluid), player)) {
                                            iterationLimit = 0;
                                        }

                                        if (player.inventory.getCursorStack().count <= 0) {
                                            iterationLimit = 0;
                                        }

                                        // Subtract the bucket size amount from the inventory fluid stack
                                        extractedStack.amount -= invFluidStack.fluid.getBucketSize();
                                    } else {
                                        // We havent managed to retrieve enough fluid for another bucket, so we stop
                                        iterationLimit = 0;
                                    }
                                }

                                // This shouldnt happen, but we will check anyway
                                if (extractedStack != null && extractedStack.amount > 0) {
                                    NyaLib.LOGGER.warn("Excess fluids when extracing fluid for a bucket" + extractedStack);
                                    inv.insertFluid(extractedStack, index, null);
                                }
                            }
                        }
                    }
                } else {
                    // The bucket has fluid, try to deposit it into the slot

                    // Try to form the empty bucket item 
                    if (bucket.getEmptyBucketItem() != null) {
                        PlayerInventory playerI = player.inventory;

                        int iterationLimit = 127;
                        while (iterationLimit-- > 0) {
                            // If the remaining items in the cursor stack are the empty form, break
                            if (playerI.getCursorStack() == null || playerI.getCursorStack().getItem() == bucket.getEmptyBucketItem()) {
                                break;
                            }

                            // If the fluid wont fit, break
                            if (inv.getRemainingFluidCapacity(index, null) < bucketFluid.getBucketSize()) {
                                break;
                            }

                            // If the fluid cannot be inserted, break
                            FluidStack fluidStack = new FluidStack(bucketFluid, bucketFluid.getBucketSize());
                            if (!slot.canInsert(fluidStack)) {
                                break;
                            }

                            // Insert the fluid
                            FluidStack remainderStack = inv.insertFluid(fluidStack, index, null);
                            if (remainderStack != null && remainderStack.amount > 0) {
                                // If it was for some reason not inserted entirely, try to insert it in any slot which will accept it
                                remainderStack = inv.insertFluid(remainderStack, null);
                                if (remainderStack != null && remainderStack.amount > 0) {
                                    NyaLib.LOGGER.warn("Excess fluids when inserting fluid from a bucket" + remainderStack);
                                }
                            }

                            playerI.getCursorStack().count--;
                            if (!pushItemBuffer(bucket.getEmptyBucketItem(), player)) {
                                break;
                            }

                            if (playerI.getCursorStack().count <= 0) {
                                break;
                            }
                        }
                    }
                }

                popItemBuffer(player);
            }
        }

        sendContentUpdates();

        return slot != null ? slot.getStack() : null;
    }

    @Unique
    private Item bufferItem = null;
    @Unique
    private int bufferCount = 0;

    @Unique
    private boolean pushItemBuffer(Item item, PlayerEntity player) {
        if (bufferItem == null) {
            bufferItem = item;
        }

        if (bufferItem == item) {
            bufferCount++;
        }

        if (bufferCount >= item.getMaxCount()) {
            return popItemBuffer(player);
        }

        return true;
    }

    @Unique
    private boolean popItemBuffer(PlayerEntity player) {
        if (bufferItem == null) {
            return false;
        }

        ItemStack bufferStack = new ItemStack(bufferItem, bufferCount);

        if (player.inventory.getCursorStack() == null || player.inventory.getCursorStack().count <= 0) {
            player.inventory.setCursorStack(bufferStack);
            resetItemBuffer();
            return true;
        } else {
            if (player.inventory.addStack(bufferStack)) {
                resetItemBuffer();
                return true;
            } else {
                player.world.spawnEntity(new ItemEntity(player.world, player.x, player.y, player.z, bufferStack));
                resetItemBuffer();
                return false;
            }
        }
    }

    public void resetItemBuffer() {
        bufferItem = null;
        bufferCount = 0;
    }

    @Override
    public void onFluidSlotUpdate(FluidHandler handler) {
        this.sendContentUpdates();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void setFluidStackInSlotClient(int index, FluidStack fluidStack) {
        FluidSlot slot = this.getFluidSlot(index);

        if (slot != null) {
            slot.setStack(fluidStack);
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void updateFluidSlotStacksClient(FluidStack[] fluidStacks) {
        for (int i = 0; i < fluidStacks.length; i++) {
            this.setFluidStackInSlotClient(i, fluidStacks[i]);
        }
    }
}
