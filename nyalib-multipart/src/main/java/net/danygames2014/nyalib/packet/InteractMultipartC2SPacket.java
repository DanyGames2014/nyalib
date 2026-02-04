package net.danygames2014.nyalib.packet;

import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.danygames2014.nyalib.multipart.MultipartState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.SideUtil;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class InteractMultipartC2SPacket extends Packet implements ManagedPacket<InteractMultipartC2SPacket> {
    public static final PacketType<InteractMultipartC2SPacket> TYPE = PacketType.builder(false, true, InteractMultipartC2SPacket::new).build();

    public int x;
    public int y;
    public int z;
    public int side;
    public ItemStack stack;
    public Vec3d hitVec;
    public int componentIndex;

    public InteractMultipartC2SPacket() {
    }

    public InteractMultipartC2SPacket(int x, int y, int z, int side, Vec3d pos, ItemStack stack, MultipartComponent component) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.side = side;
        this.hitVec = pos;
        this.stack = stack;
        this.componentIndex = component.state.components.indexOf(component);
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            this.x = stream.readInt();
            this.y = stream.readInt();
            this.z = stream.readInt();
            this.side = stream.read();
            
            int itemId = stream.readInt();
            if (itemId >= 0) {
                byte count = stream.readByte();
                short damage = stream.readShort();
                this.stack = new ItemStack(itemId, count, damage);
            } else {
                this.stack = null;
            }

            double vecX = stream.readDouble();
            double vecY = stream.readDouble();
            double vecZ = stream.readDouble();
            this.hitVec = Vec3d.create(vecX, vecY, vecZ);
            this.componentIndex = stream.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(this.x);
            stream.writeInt(this.y);
            stream.writeInt(this.z);
            stream.write(this.side);
            
            if (this.stack == null) {
                stream.writeInt(-1);
            } else {
                stream.writeInt(this.stack.itemId);
                stream.writeByte(this.stack.count);
                stream.writeShort(this.stack.getDamage());
            }

            stream.writeDouble(hitVec.x);
            stream.writeDouble(hitVec.y);
            stream.writeDouble(hitVec.z);
            
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
        ServerPlayerEntity player = (ServerPlayerEntity) PlayerHelper.getPlayerFromPacketHandler(networkHandler);
        ServerWorld serverWorld = server.getWorld(player.dimensionId);
        
        ItemStack selectedItem = player.inventory.getSelectedItem();
        boolean var4 = serverWorld.bypassSpawnProtection = serverWorld.dimension.id != 0 || server.playerManager.isOperator(player.name);
        if (side == 255) {
            if (selectedItem == null) {
                return;
            }

            player.interactionManager.interactItem(player, serverWorld, selectedItem);
        } else {
            Vec3i spawnPos = serverWorld.getSpawnPos();
            int xDistanceFromSpawn = (int) MathHelper.abs((float)(x - spawnPos.x));
            int zDistanceFromSpawn = (int) MathHelper.abs((float)(x - spawnPos.z));
            if (xDistanceFromSpawn > zDistanceFromSpawn) {
                zDistanceFromSpawn = xDistanceFromSpawn;
            }

            if (serverNetworkHandler.teleported && player.getSquaredDistance((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F) < (double)64.0F && (zDistanceFromSpawn > 16 || var4)) {
                interactMultipart(player, serverWorld);
            }
        }

        selectedItem = player.inventory.getSelectedItem();
        if (selectedItem != null && selectedItem.count == 0) {
            player.inventory.main[player.inventory.selectedSlot] = null;
        }

        player.skipPacketSlotUpdates = true;
        player.inventory.main[player.inventory.selectedSlot] = ItemStack.clone(player.inventory.main[player.inventory.selectedSlot]);
        Slot selectedSlot = player.currentScreenHandler.getSlot(player.inventory, player.inventory.selectedSlot);
        player.currentScreenHandler.sendContentUpdates();
        player.skipPacketSlotUpdates = false;
        if (!ItemStack.areEqual(player.inventory.getSelectedItem(), stack)) {
            serverNetworkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(player.currentScreenHandler.syncId, selectedSlot.id, player.inventory.getSelectedItem()));
        }

        serverWorld.bypassSpawnProtection = false;
    }

    public void interactMultipart(PlayerEntity player, World world) {
        MultipartState state = world.getMultipartState(x,y,z);

        if (state != null && stack != null) {
            stack.useOnMultipart(player, world, x, y, z, Direction.byId(side), hitVec, state.components.get(componentIndex));
        }
    }

    @Override
    public int size() {
        return 45;
    }

    @Override
    public @NotNull PacketType<InteractMultipartC2SPacket> getType() {
        return TYPE;
    }
}
