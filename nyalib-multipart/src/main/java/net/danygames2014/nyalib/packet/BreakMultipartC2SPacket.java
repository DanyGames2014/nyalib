package net.danygames2014.nyalib.packet;

import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.danygames2014.nyalib.multipart.MultipartState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.ServerWorld;
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
        ServerPlayNetworkHandler serverNetworkHandler = (ServerPlayNetworkHandler) networkHandler;
        MinecraftServer server = serverNetworkHandler.server;
        PlayerEntity player = PlayerHelper.getPlayerFromPacketHandler(networkHandler);
        ServerWorld world = server.getWorld(player.dimensionId);

        boolean canInteract = world.bypassSpawnProtection = world.dimension.id != 0 || server.playerManager.isOperator(player.name);

        // Break the multipart
        Vec3i spawnPos = world.getSpawnPos();
        int xDistanceFromSpawn = (int) MathHelper.abs((float)(x - spawnPos.x));
        int zDistanceFromSpawn = (int) MathHelper.abs((float)(x - spawnPos.z));
        if (xDistanceFromSpawn > zDistanceFromSpawn) {
            zDistanceFromSpawn = xDistanceFromSpawn;
        }

        if (serverNetworkHandler.teleported && player.getSquaredDistance((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F) < (double)64.0F && (zDistanceFromSpawn > 16 || canInteract)) {
            MultipartState state = world.getMultipartState(x,y,z);
            if (state != null) {
                state.removeComponent(componentIndex, true);
                PacketHelper.sendTo(player, new MultipartDataS2CPacket(world, x, y, z));
            }
        }

        world.bypassSpawnProtection = false;
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
