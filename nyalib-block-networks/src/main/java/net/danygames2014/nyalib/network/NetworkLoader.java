package net.danygames2014.nyalib.network;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.danygames2014.nyalib.NyaLib;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.ClientWorld;
import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.api.event.world.WorldEvent;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.SideUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@SuppressWarnings({"unused", "OptionalIsPresent"})
public class NetworkLoader {
    public static ObjectOpenHashSet<Dimension> readOnly = new ObjectOpenHashSet<>();
    public static boolean isRemote = false;

    @EventListener
    public void saveNetworks(WorldEvent.Save event) {
        // If the world or dimension are null, do not proceed
        if (event.world == null || event.world.dimension == null) {
            return;
        }

        // Get the dimension Identifier
        Optional<Identifier> dimIdentifierO = DimensionRegistry.INSTANCE.getIdByLegacyId(event.world.dimension.id);
        String dimensionId = dimIdentifierO.isPresent() ? event.world.dimension.id + " (" + dimIdentifierO.get() + ")" : String.valueOf(event.world.dimension.id);
        
        NyaLib.LOGGER.debug("Saving NyaLib networks in dimension " + dimensionId);

        // Do not save networks on a remote world (e.g client playing on server)
        if (isRemote) {
            return;
        }

        // Do not save networks if the dimension networks have been marked as read-only
        if (readOnly.contains(event.world.dimension)) {
            NyaLib.LOGGER.warn("Saving NyaLib networks in dimension " + dimensionId + " prevented as they are read-only due to error when loading.");
            return;
        }

        // Save the networks
        try {
            File file = event.world.storage.getWorldPropertiesFile("nyalib_networks");

            NbtCompound tag = new NbtCompound();
            if (file.exists()) {
                tag = NbtIo.readCompressed(new FileInputStream(file));
            }

            tag.putInt("next_id", NetworkManager.NEXT_ID.get());
            NetworkManager.writeNbt(event.world, tag);

            NbtIo.writeCompressed(tag, new FileOutputStream(file));
            NyaLib.LOGGER.debug("Saved NyaLib networks in dimension " + dimensionId);
        } catch (Exception e) {
            NyaLib.LOGGER.error("Error occured while saving NyaLib Networks in dimension " + dimensionId, e);
        }
    }

    @EventListener
    public void loadNetworks(WorldEvent.Init event) {
        // If the world or dimension are null, do not proceed
        if (event.world == null || event.world.dimension == null) {
            return;
        }

        // Get the dimension Identifier
        Optional<Identifier> dimIdentifierO = DimensionRegistry.INSTANCE.getIdByLegacyId(event.world.dimension.id);
        String dimensionId = dimIdentifierO.isPresent() ? event.world.dimension.id + " (" + dimIdentifierO.get() + ")" : String.valueOf(event.world.dimension.id);
        
        // If the world has no chunk storage (For example AMI Inventory World), do not attempt to save
        if (event.world.storage.getChunkStorage(event.world.dimension) == null) {
            NyaLib.LOGGER.info("Skipping loading NyaLib networks in dimension " + dimensionId + " because there is no chunk storage");
            return;
        }

        NyaLib.LOGGER.debug("Loading NyaLib networks in dimension " + dimensionId);

        // Detect if the world is remote
        isRemote = SideUtil.get(() -> event.world instanceof ClientWorld, () -> false);

        // Do not save networks if the dimension networks have been marked as read-only
        if (isRemote) {
            NyaLib.LOGGER.info("Skipping loading NyaLib networks in dimension " + dimensionId + " because of the world being remote");
            return;
        }
        
        // When a dimension is being loaded again from a save-file, remove its read-only status if it had one
        readOnly.remove(event.world.dimension);

        // Load the networks
        try {
            File file = event.world.storage.getWorldPropertiesFile("nyalib_networks");
            if (file.exists()) {
                NbtCompound tag = NbtIo.readCompressed(new FileInputStream(file));

                NetworkManager.NETWORKS = new HashMap<>();
                NetworkManager.removeQueue = new ArrayList<>();
                NetworkManager.NEXT_ID.set(tag.getInt("next_id"));
                NetworkManager.readNbt(event.world, tag);

                int networkCount = 0;
                for (var dimEntries : NetworkManager.NETWORKS.values()) {
                    for (var networks : dimEntries.values()) {
                        networkCount += networks.size();
                    }
                }

                NyaLib.LOGGER.info("Loaded {} NyaLib networks in dimension {}", networkCount, dimensionId);
            }
        } catch (Exception e) {
            NyaLib.LOGGER.error("Error occured while loading NyaLib Networks in dimension " + dimensionId + ", networks are now read only to prevent saving corrupted data", e);
            readOnly.add(event.world.dimension);
        }
    }
}
