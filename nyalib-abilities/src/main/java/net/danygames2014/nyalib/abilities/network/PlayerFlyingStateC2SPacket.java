package net.danygames2014.nyalib.abilities.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.SideUtil;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PlayerFlyingStateC2SPacket extends Packet implements ManagedPacket<PlayerFlyingStateC2SPacket> {
    public static final PacketType<PlayerFlyingStateC2SPacket> TYPE = PacketType.builder(false, true, PlayerFlyingStateC2SPacket::new).build();

    public boolean flying;

    public PlayerFlyingStateC2SPacket() {

    }

    public PlayerFlyingStateC2SPacket(boolean flying) {
        this.flying = flying;
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            flying = stream.readBoolean();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeBoolean(flying);
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

        if (player.nyalib$canFly()) {
            player.nyalib$setFlying(flying);
        } else {
            player.nyalib$setFlying(false);
        }
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public @NotNull PacketType<PlayerFlyingStateC2SPacket> getType() {
        return TYPE;
    }
}
