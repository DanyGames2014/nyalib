package net.danygames2014.nyalib.entity.datatracker.network;

import net.danygames2014.nyalib.entity.datatracker.ExtendedDataTracker;
import net.danygames2014.nyalib.entity.datatracker.ExtendedDataTrackerEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.SideUtil;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class ExtendedEntityTrackerSpawnS2CPacket extends Packet implements ManagedPacket<ExtendedEntityTrackerSpawnS2CPacket> {
    public static final PacketType<ExtendedEntityTrackerSpawnS2CPacket> TYPE = PacketType.builder(true, false, ExtendedEntityTrackerSpawnS2CPacket::new).build();

    private int entityId;   
    private ExtendedDataTracker dataTracker;
    private List<ExtendedDataTrackerEntry> entries;
    
    public ExtendedEntityTrackerSpawnS2CPacket() {
        
    }
    
    public ExtendedEntityTrackerSpawnS2CPacket(Entity entity) {
        this.entityId = entity.id;
        this.dataTracker = entity.getExtendedDataTracker();
    }    

    @Override
    public void read(DataInputStream stream) {
        try {
            this.entityId = stream.readInt();
            this.entries = ExtendedDataTracker.deserializeEntries(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(this.entityId);
            this.dataTracker.writeEntries(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        SideUtil.runClient(() -> handleClient(networkHandler));
    }
    
    @Environment(EnvType.CLIENT)   
    public void handleClient(NetworkHandler networkHandler) {
        if (networkHandler instanceof ClientNetworkHandler clientNetworkHandler) {
            Entity entity = clientNetworkHandler.getEntity(this.entityId);
            if (entity != null) {
                entity.getExtendedDataTracker().updateEntries(this.entries);
                System.err.println("ExtendedDataTracker initialized for spawned entity " + entity.id);
            }
        }
    }   

    @Override
    public int size() {
        return 5;
    }

    @Override
    public @NotNull PacketType<ExtendedEntityTrackerSpawnS2CPacket> getType() {
        return TYPE;
    }
}
