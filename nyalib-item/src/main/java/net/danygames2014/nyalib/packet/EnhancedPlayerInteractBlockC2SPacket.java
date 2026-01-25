package net.danygames2014.nyalib.packet;

import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.math.Vec3d;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EnhancedPlayerInteractBlockC2SPacket extends PlayerInteractBlockC2SPacket implements ManagedPacket<EnhancedPlayerInteractBlockC2SPacket> {
    public static final PacketType<EnhancedPlayerInteractBlockC2SPacket> TYPE = PacketType.builder(false, true, EnhancedPlayerInteractBlockC2SPacket::new).build();
    
    public Vec3d hitVec;
    
    public EnhancedPlayerInteractBlockC2SPacket() {
        super();
    }

    public EnhancedPlayerInteractBlockC2SPacket(int x, int y, int z, int side, ItemStack stack, Vec3d hitVec) {
        super(x, y, z, side, stack);
        this.hitVec = hitVec;
    }

    @Override
    public void read(DataInputStream stream) {
        super.read(stream);
        try {
            double vecX = stream.readDouble();
            double vecY = stream.readDouble();
            double vecZ = stream.readDouble();
            this.hitVec = Vec3d.create(vecX, vecY, vecZ);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        super.write(stream);
        try {
            stream.writeDouble(hitVec.x);
            stream.writeDouble(hitVec.y);
            stream.writeDouble(hitVec.z);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        networkHandler.onPlayerInteractBlock(this);
    }

    @Override
    public int size() {
        return super.size() + 24;
    }

    @Override
    public @NotNull PacketType<EnhancedPlayerInteractBlockC2SPacket> getType() {
        return TYPE;
    }
}
