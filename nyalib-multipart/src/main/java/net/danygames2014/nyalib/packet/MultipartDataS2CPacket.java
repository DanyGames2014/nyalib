package net.danygames2014.nyalib.packet;

import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class MultipartDataS2CPacket extends Packet implements ManagedPacket<MultipartDataS2CPacket> {
    public static final PacketType<MultipartDataS2CPacket> TYPE = PacketType.builder(true, false, MultipartDataS2CPacket::new).build();

    private World world;
    private int chunkX;
    private int chunkZ;
    
    public MultipartDataS2CPacket(World world, int chunkX, int chunkZ) {
        this.world = world;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    public MultipartDataS2CPacket() {
    }

    @Override
    public void read(DataInputStream stream) {
        
    }

    @Override
    public void write(DataOutputStream stream) {

    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        System.err.println("Received MultipartDataS2CPacket");
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public @NotNull PacketType<MultipartDataS2CPacket> getType() {
        return TYPE;
    }
}
