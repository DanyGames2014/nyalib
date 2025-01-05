package net.danygames2014.nyalib.packet;

import net.danygames2014.nyalib.NyaLib;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ItemParticle;
import net.minecraft.item.Item;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParticlePacket extends Packet implements ManagedPacket<ParticlePacket> {
    public static final PacketType<ParticlePacket> TYPE = PacketType.builder(true, false, ParticlePacket::new).build();

    double x;
    double y;
    double z;
    double velocityX;
    double velocityY;
    double velocityZ;
    String particle;
    Identifier item;

    public ParticlePacket() {
    }

    public ParticlePacket(Item item, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.particle = "item";
        this.item = ItemRegistry.INSTANCE.getId(item);
    }

    public ParticlePacket(String particle, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.particle = particle;
        this.item = Identifier.of("");
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            x = stream.readDouble();
            y = stream.readDouble();
            z = stream.readDouble();
            velocityX = stream.readDouble();
            velocityY = stream.readDouble();
            velocityZ = stream.readDouble();
            particle = stream.readUTF();
            item = Identifier.of(stream.readUTF());
        } catch (IOException e) {
            NyaLib.LOGGER.warn("Error reading particle packet", e);
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeDouble(x);
            stream.writeDouble(y);
            stream.writeDouble(z);
            stream.writeDouble(velocityX);
            stream.writeDouble(velocityY);
            stream.writeDouble(velocityZ);
            stream.writeUTF(particle);
            stream.writeUTF(item.toString());
        } catch (IOException e) {
            NyaLib.LOGGER.warn("Error writing particle packet", e);
        }
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.SERVER)) {
            return;
        }

        handleClient();
    }

    @Environment(EnvType.CLIENT)
    public void handleClient() {
        if (particle.equals("item")) {
            Item particleItem = ItemRegistry.INSTANCE.get(item);

            if (Minecraft.INSTANCE != null && Minecraft.INSTANCE.particleManager != null && particleItem != null) {
                Minecraft.INSTANCE.particleManager.addParticle(new ItemParticle(Minecraft.INSTANCE.world, x, y, z, particleItem));
            }
        } else {
            Minecraft.INSTANCE.world.addParticle(particle, x, y, z, velocityX, velocityY, velocityZ);
        }
    }

    @Override
    public int size() {
        return 48 + particle.length() + item.toString().length();
    }

    @Override
    public @NotNull PacketType<ParticlePacket> getType() {
        return TYPE;
    }
}
