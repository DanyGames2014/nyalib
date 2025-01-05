package net.danygames2014.nyalib.packet;

import net.danygames2014.nyalib.NyaLib;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MusicPacket extends Packet implements ManagedPacket<MusicPacket> {
    public static final PacketType<MusicPacket> TYPE = PacketType.builder(true, false, MusicPacket::new).build();
    
    int x;
    int y;
    int z;
    String name;
    
    public MusicPacket() {
    }

    public MusicPacket(int x, int y, int z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            x = stream.readInt();
            y = stream.readInt();
            z = stream.readInt();
            name = stream.readUTF();
        } catch (IOException e) {
            NyaLib.LOGGER.warn("Error reading music packet", e);
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(x);
            stream.writeInt(y);
            stream.writeInt(z);
            stream.writeUTF(name);
        } catch (IOException e) {
            NyaLib.LOGGER.warn("Error writing music packet", e);
        }
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.SERVER)) {
            return;
        }

        if(name.isEmpty()) {
            name = null;
        }
        
        Minecraft.INSTANCE.world.playStreaming(name, x,y,z);
    }

    @Override
    public int size() {
        return 12 + name.length();
    }

    @Override
    public @NotNull PacketType<MusicPacket> getType() {
        return TYPE;
    }
}
