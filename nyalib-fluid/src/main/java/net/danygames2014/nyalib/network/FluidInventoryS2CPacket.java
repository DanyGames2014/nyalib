package net.danygames2014.nyalib.network;

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
import java.util.ArrayList;

public class FluidInventoryS2CPacket extends Packet implements ManagedPacket<FluidInventoryS2CPacket> {
    public static final PacketType<FluidInventoryS2CPacket> TYPE = PacketType.builder(true, false, FluidInventoryS2CPacket::new).build();

    public int syncId;
    public FluidStack[] contents;

    // The size of the packet in bytes
    private int size = 4;
    
    public FluidInventoryS2CPacket() {
    }

    public FluidInventoryS2CPacket(int syncId, ArrayList<FluidStack> stacks) {
        this.syncId = syncId;
        this.contents = new FluidStack[stacks.size()];

        for (int i = 0; i < stacks.size(); i++) {
            FluidStack stack = stacks.get(i);
            this.contents[i] = stack == null ? null : stack.copy();
        }
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            syncId = stream.readInt();
            contents = new FluidStack[stream.readShort()];

            for (int i = 0; i < contents.length; i++) {
                if (stream.readBoolean()) {
                    this.contents[i] = new FluidStack(FluidRegistry.get(Identifier.of(stream.readUTF())), stream.readInt());
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            int initialStreamSize = stream.size();
            
            stream.writeInt(syncId);
            stream.writeShort(contents.length);

            //noinspection ForLoopReplaceableByForEach
            for (int i = 0; i < contents.length; i++) {
                if (contents[i] == null) {
                    stream.writeBoolean(false);
                } else {
                    stream.writeBoolean(true);
                    stream.writeUTF(contents[i].fluid.getIdentifier().toString());
                    stream.writeInt(contents[i].amount);
                }
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
    private void handleClient(NetworkHandler networkHandler) {
        PlayerEntity player = PlayerHelper.getPlayerFromPacketHandler(networkHandler);

        if (syncId == 0) {
            player.playerScreenHandler.updateFluidSlotStacksClient(contents);
        } else if (syncId == player.currentScreenHandler.syncId) {
            player.currentScreenHandler.updateFluidSlotStacksClient(contents);
        }
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public @NotNull PacketType<FluidInventoryS2CPacket> getType() {
        return TYPE;
    }
}
