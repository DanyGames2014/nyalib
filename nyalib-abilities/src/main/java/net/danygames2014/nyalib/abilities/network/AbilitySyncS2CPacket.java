package net.danygames2014.nyalib.abilities.network;

import net.danygames2014.nyalib.abilities.ability.Ability;
import net.danygames2014.nyalib.abilities.ability.AbilityManager;
import net.danygames2014.nyalib.abilities.ability.AbilityRegistry;
import net.danygames2014.nyalib.abilities.ability.value.AbilityValue;
import net.danygames2014.nyalib.abilities.ability.value.AbilityValueFactory;
import net.danygames2014.nyalib.abilities.ability.value.AbilityValueTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.SideUtil;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class AbilitySyncS2CPacket extends Packet implements ManagedPacket<AbilitySyncS2CPacket> {
    public static final PacketType<AbilitySyncS2CPacket> TYPE = PacketType.builder(true, false, AbilitySyncS2CPacket::new).build();

    public int entityId;
    public Identifier abilityIdentifier;
    public String abilityValueType;
    public NbtCompound abilityValueNbt;

    int size = 0;

    public AbilitySyncS2CPacket() {

    }

    public AbilitySyncS2CPacket(int entityId, Ability<?, ?> ability, AbilityValue<?> value) {
        this.entityId = entityId;
        this.abilityIdentifier = ability.identifier;
        this.abilityValueType = AbilityValueTypeRegistry.getId(value.getClass());
        if (this.abilityValueType != null) {
            this.abilityValueNbt = new NbtCompound();
            value.writeNbt(this.abilityValueNbt);
        }
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            if (stream.readBoolean()) {
                entityId = stream.readInt();
                abilityIdentifier = Identifier.tryParse(stream.readUTF());
                abilityValueType = stream.readUTF();
                abilityValueNbt = NbtIo.read(stream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            int initialSize = stream.size();

            if (abilityValueNbt == null) {
                stream.writeBoolean(false);
                return;
            }

            stream.writeBoolean(true);
            stream.writeInt(entityId);
            stream.writeUTF(abilityIdentifier.toString());
            stream.writeUTF(abilityValueType);
            NbtIo.write(abilityValueNbt, stream);

            size = stream.size() - initialSize;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        SideUtil.run(() -> handleClient(networkHandler), () -> {
        });
    }

    @Environment(EnvType.CLIENT)
    public void handleClient(NetworkHandler networkHandler) {
        PlayerEntity player = PlayerHelper.getPlayerFromPacketHandler(networkHandler);

        Entity targetEntity = null;

        if (entityId == -1) {
            targetEntity = player;
        } else {
            @SuppressWarnings("unchecked")
            List<Entity> entities = player.world.getEntities();
            for (Entity entity : entities) {
                if (entity.id == entityId) {
                    targetEntity = entity;
                }
            }
        }

        if (targetEntity == null) {
            return;
        }

        Ability<?, ?> ability = AbilityRegistry.getAbility(abilityIdentifier);
        if (ability == null) {
            return;
        }

        AbilityValueFactory<?> abilityValueFactory = AbilityValueTypeRegistry.getFactory(abilityValueType);
        AbilityValue<?> abilityValue = abilityValueFactory.create();
        abilityValue.readNbt(abilityValueNbt);

        AbilityManager.getInstance().set(targetEntity, ability, abilityValue.get());
        System.err.println("Received AbilitySyncS2CPacket: " + abilityIdentifier + " -> " + abilityValue.get());
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public @NotNull PacketType<AbilitySyncS2CPacket> getType() {
        return TYPE;
    }
}
