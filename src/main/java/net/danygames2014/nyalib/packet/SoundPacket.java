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

public class SoundPacket extends Packet implements ManagedPacket<SoundPacket> {
    public static final PacketType<SoundPacket> TYPE = PacketType.builder(true, false, SoundPacket::new).build();
    
    double x;
    double y;
    double z;
    float volume;
    float pitch;
    String name;
    
    public SoundPacket() {
    }

    public SoundPacket(double x, double y, double z, float volume, float pitch, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.volume = volume;
        this.pitch = pitch;
        this.name = name;
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            x = stream.readDouble();
            y = stream.readDouble();
            z = stream.readDouble();
            volume = stream.readFloat();
            pitch = stream.readFloat();
            name = stream.readUTF();
        } catch (IOException e) {
            NyaLib.LOGGER.warn("Error reading sound packet", e);
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeDouble(x);
            stream.writeDouble(y);
            stream.writeDouble(z);
            stream.writeFloat(volume);
            stream.writeFloat(pitch);
            stream.writeUTF(name);
        } catch (IOException e) {
            NyaLib.LOGGER.warn("Error writing sound packet", e);
        }
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.SERVER)) {
            return;
        }

        Minecraft.INSTANCE.world.playSound(x,y,z,name,volume,pitch);
    }

    @Override
    public int size() {
        return 32 + name.length();
    }

    @Override
    public @NotNull PacketType<SoundPacket> getType() {
        return TYPE;
    }
}
