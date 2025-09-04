package net.danygames2014.nyalib.network;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.fluid.FluidRegistry;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.SideUtil;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ScreenHandlerFluidSlotUpdateS2CPacket extends Packet implements ManagedPacket<ScreenHandlerFluidSlotUpdateS2CPacket> {
    public static final PacketType<ScreenHandlerFluidSlotUpdateS2CPacket> TYPE = PacketType.builder(true, false, ScreenHandlerFluidSlotUpdateS2CPacket::new).build();

    public int syncId;
    public int slot;
    public FluidStack stack;

    // The size of the packet in bytes
    private int size = 10;
    
    public ScreenHandlerFluidSlotUpdateS2CPacket() {
    }

    public ScreenHandlerFluidSlotUpdateS2CPacket(int syncId, int slot, FluidStack stack) {
        this.syncId = syncId;
        this.slot = slot;
        this.stack = stack == null ? stack : stack.copy();
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            this.syncId = stream.readInt();
            this.slot = stream.readInt();
            
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

            stream.writeInt(this.syncId);
            stream.writeInt(this.slot);
            
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
        SideUtil.run(() -> handleClient(networkHandler), () -> {});
    }
    
    @Environment(EnvType.CLIENT)
    public void handleClient(NetworkHandler networkHandler){
        PlayerEntity player = PlayerHelper.getPlayerFromPacketHandler(networkHandler);
        
        switch (this.syncId) {
            case -1 -> {
                NyaLib.LOGGER.error("Encountered an unknown syncId in ScreenHandlerFluidSlotUpdateS2CPacket: {}", this.syncId);
            }
            
            case 0 -> {
                player.playerScreenHandler.setFluidStackInSlotClient(slot, stack);
            }

            default -> {
                if (syncId == player.currentScreenHandler.syncId) {
                    player.currentScreenHandler.setFluidStackInSlotClient(slot, stack);
                }
            }
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public @NotNull PacketType<ScreenHandlerFluidSlotUpdateS2CPacket> getType() {
        return TYPE;
    }
}
