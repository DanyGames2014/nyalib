package net.danygames2014.nyalib.network;

import net.danygames2014.nyalib.fluid.FluidRegistry;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.play.ScreenHandlerAcknowledgementPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.SideUtil;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ClickFluidSlotC2SPacket extends Packet implements ManagedPacket<ClickFluidSlotC2SPacket> {
    public static final PacketType<ClickFluidSlotC2SPacket> TYPE = PacketType.builder(false, true, ClickFluidSlotC2SPacket::new).build();

    public int syncId;
    public int slot;
    public int button;
    public short actionType;
    public FluidStack stack;
    public boolean holdingShift;
    
    // The size of the packet in bytes
    private int size = 9;

    public ClickFluidSlotC2SPacket() {
    }

    public ClickFluidSlotC2SPacket(int syncId, int slot, int button, boolean holdingShift, FluidStack stack, short actionType) {
        this.syncId = syncId;
        this.slot = slot;
        this.button = button;
        this.holdingShift = holdingShift;
        this.stack = stack;
        this.actionType = actionType;
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            this.syncId = stream.readByte();
            this.slot = stream.readShort();
            this.button = stream.readByte();
            this.actionType = stream.readShort();
            this.holdingShift = stream.readBoolean();
            String fluidIdentifier = stream.readUTF();
            if (!fluidIdentifier.isEmpty()) {
                int amount = stream.readInt();
                this.stack = new FluidStack(FluidRegistry.get(Identifier.of(fluidIdentifier)), amount);
            } else {
                this.stack = null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            int initialStreamSize = stream.size();
            
            stream.writeByte(this.syncId);
            stream.writeShort(this.slot);
            stream.writeByte(this.button);
            stream.writeShort(this.actionType);
            stream.writeBoolean(this.holdingShift);
            if (this.stack == null) {
                stream.writeUTF("");
            } else {
                stream.writeUTF(this.stack.fluid.getIdentifier().toString());
                stream.writeInt(this.stack.amount);
            }

            this.size = stream.size() - initialStreamSize;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        SideUtil.run(() -> {}, () -> handleServer(networkHandler));
    }

    @Environment(EnvType.SERVER)
    public void handleServer(NetworkHandler handler) {
        if (PlayerHelper.getPlayerFromPacketHandler(handler) instanceof ServerPlayerEntity player && handler instanceof ServerPlayNetworkHandler networkHandler) {
            if (player.currentScreenHandler.syncId == syncId && player.currentScreenHandler.canOpen(player)) {
                FluidStack clickResultStack = player.currentScreenHandler.onFluidSlotClick(slot, button, holdingShift, player, player.inventory.getCursorStack());
                
                // TODO: This is shit, do something about it, probably not needed at all
                if (FluidStack.areEqual(stack, clickResultStack)) {
                    player.networkHandler.sendPacket(new ScreenHandlerAcknowledgementPacket(syncId, actionType, true));
                    player.skipPacketSlotUpdates = true;
                    player.currentScreenHandler.sendContentUpdates();
                    player.updateCursorStack();
                    player.skipPacketSlotUpdates = false;
                } else {
                    //noinspection unchecked
                    networkHandler.transactions.put(player.currentScreenHandler.syncId, actionType);
                    player.networkHandler.sendPacket(new ScreenHandlerAcknowledgementPacket(syncId, actionType, false));
                    player.currentScreenHandler.updatePlayerList(player, false);
                    
                    ArrayList<FluidStack> var3 = new ArrayList<>();

                    for (int i = 0; i < player.currentScreenHandler.getFluidSlots().size(); i++) {
                        var3.add(player.currentScreenHandler.getFluidSlot(i).getStack());
                    }
                    
                    player.onFluidContentsUpdate(player.currentScreenHandler, var3);
                }
            }
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public @NotNull PacketType<ClickFluidSlotC2SPacket> getType() {
        return TYPE;
    }
}
