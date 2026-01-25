package net.danygames2014.nyalib.packet;

import net.danygames2014.nyalib.multipart.MultipartState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.SideUtil;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class MultipartDataS2CPacket extends Packet implements ManagedPacket<MultipartDataS2CPacket> {
    public static final PacketType<MultipartDataS2CPacket> TYPE = PacketType.builder(true, false, MultipartDataS2CPacket::new).build();

    private World world;
    private int x;
    private int y;
    private int z;
    private NbtCompound stateNbt;

    public MultipartDataS2CPacket(World world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public MultipartDataS2CPacket() {
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            this.x = stream.readInt();
            this.y = stream.readInt();
            this.z = stream.readInt();
            
            int length = stream.readInt();
            if (length > 0) {
                byte[] bytes = new byte[length];
                stream.readFully(bytes);
                stateNbt = NbtIo.readCompressed(new ByteArrayInputStream(bytes));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(x);
            stream.writeInt(y);
            stream.writeInt(z);

            MultipartState state = world.getMultipartState(x,y,z);

            NbtCompound stateNbt = new NbtCompound();
            if (state != null) {
                state.writeNbt(stateNbt);
            }
            
            if (stateNbt.values().isEmpty()) {
                stream.writeInt(0); // length   
            } else {
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                NbtIo.writeCompressed(stateNbt, byteStream);
                byte[] bytes = byteStream.toByteArray();
                stream.writeInt(bytes.length); // length
                stream.write(bytes); // nbt data
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        SideUtil.run(() -> handleClient(networkHandler), () -> {
        });
    }

    // This will run on client
    @Environment(EnvType.CLIENT)
    public void handleClient(NetworkHandler networkHandler) {
        PlayerEntity player = PlayerHelper.getPlayerFromPacketHandler(networkHandler);
        World world = player.world;

        MultipartState state = new MultipartState();
        world.setMultipartState(x,y,z,state);
        state.readNbt(stateNbt);
        state.markDirty();
        
        for (int i = 0; i < 50; i++) {
            Minecraft.INSTANCE.worldRenderer.addParticle("lava", x + 0.5D,y,z + 0.5D, 0.0D, 0.2D, 0.0D);
        }
    }

    @Override
    public int size() {
        return 12;
    }

    @Override
    public @NotNull PacketType<MultipartDataS2CPacket> getType() {
        return TYPE;
    }
}
