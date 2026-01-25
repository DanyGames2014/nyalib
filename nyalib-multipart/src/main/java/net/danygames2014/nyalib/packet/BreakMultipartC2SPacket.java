package net.danygames2014.nyalib.packet;

import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.danygames2014.nyalib.multipart.MultipartState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.SideUtil;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BreakMultipartC2SPacket extends Packet implements ManagedPacket<BreakMultipartC2SPacket> {
    public static final PacketType<BreakMultipartC2SPacket> TYPE = PacketType.builder(false, true, BreakMultipartC2SPacket::new).build();
    
    private int x;
    private int y;
    private int z;
    private int componentIndex;

    public BreakMultipartC2SPacket(int x, int y, int z, MultipartComponent component) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.componentIndex = component.state.components.indexOf(component);
    }

    public BreakMultipartC2SPacket() {
        
    }
    
    @Override
    public void read(DataInputStream stream) {
        try {
            this.x = stream.readInt();
            this.y = stream.readInt();
            this.z = stream.readInt();
            this.componentIndex = stream.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(x);
            stream.writeInt(y);
            stream.writeInt(z);
            stream.writeInt(componentIndex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        SideUtil.run(() -> {}, () -> handleServer(networkHandler));
    }

    @Environment(EnvType.SERVER)
    public void handleServer(NetworkHandler networkHandler) {
        PlayerEntity player = PlayerHelper.getPlayerFromPacketHandler(networkHandler);
        World world = player.world;
        
        MultipartState state = world.getMultipartState(x,y,z);
        if (state != null) {
            state.removeComponent(componentIndex, true);
            // TODO: actually do logic on the server to see if the player can mine the component
            PacketHelper.sendTo(player, new MultipartDataS2CPacket(world, x, y, z));
        }
    }

    @Override
    public int size() {
        return 16;
    }

    @Override
    public @NotNull PacketType<BreakMultipartC2SPacket> getType() {
        return TYPE;
    }
}
