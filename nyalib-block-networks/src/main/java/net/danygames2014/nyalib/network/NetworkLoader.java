package net.danygames2014.nyalib.network;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.danygames2014.nyalib.NyaLib;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.ClientWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.api.event.world.WorldEvent;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.SideUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Optional;

@SuppressWarnings({"unused", "OptionalIsPresent"})
public class NetworkLoader {
    public static ObjectOpenHashSet<Dimension> readOnly = new ObjectOpenHashSet<>();
    public static boolean isRemote = false;

    @EventListener
    public void worldSaveListener(WorldEvent.Save event) {
        // If the world or dimension are null, do not proceed
        if (event.world == null || event.world.dimension == null) {
            return;
        }
        
        saveNetworks(event.world, event.world.dimension);
    }
    
    public void saveNetworks(World world, Dimension dimension) {
        // Get the dimension Identifier
        Optional<Identifier> dimIdentifierO = DimensionRegistry.INSTANCE.getIdByLegacyId(dimension.id);
        String dimensionId = dimIdentifierO.isPresent() ? dimension.id + " (" + dimIdentifierO.get() + ")" : String.valueOf(dimension.id);
        
        // Do not save networks on a remote world (e.g client playing on server)
        if (isRemote) {
            NyaLib.LOGGER.info("Skipping saving NyaLib networks in dimension " + dimensionId + " because the world is remote");
            return;
        }

        // Do not save networks if the dimension networks have been marked as read-only
        if (readOnly.contains(dimension)) {
            NyaLib.LOGGER.warn("Saving NyaLib networks in dimension " + dimensionId + " prevented as they are read-only due to error when loading.");
            return;
        }

        NyaLib.LOGGER.debug("Saving NyaLib networks in dimension " + dimensionId);
        
        // Save the networks
        try {
            File file = world.storage.getWorldPropertiesFile("nyalib_networks");

            // If the file is null, do not attempt to save
            if (file == null) {
                return;
            }
            
            NbtCompound tag = new NbtCompound();
            if (file.exists()) {
                tag = NbtIo.readCompressed(new FileInputStream(file));
            }

            tag.putInt("next_id", NetworkManager.NEXT_ID.get());
            NetworkManager.writeNbt(world, tag);

            NbtIo.writeCompressed(tag, new FileOutputStream(file));
            NyaLib.LOGGER.info("Saved NyaLib networks in dimension " + dimensionId);
        } catch (Exception e) {
            NyaLib.LOGGER.error("Error occured while saving NyaLib Networks in dimension " + dimensionId, e);
        }
    }

    @EventListener
    public void worldInitListener(WorldEvent.Init event) {
        // If the world or dimension are null, do not proceed
        if (event.world == null || event.world.dimension == null) {
            return;
        }
        
        loadNetworks(event.world, event.world.dimension);
    }
    
    public void loadNetworks(World world, Dimension dimension) {
        // Detect if the world is remote
        isRemote = SideUtil.get(() -> world instanceof ClientWorld, () -> false);

        // Get the dimension Identifier
        Optional<Identifier> dimIdentifierO = DimensionRegistry.INSTANCE.getIdByLegacyId(dimension.id);
        String dimensionId = dimIdentifierO.isPresent() ? dimension.id + " (" + dimIdentifierO.get() + ")" : String.valueOf(dimension.id);

        // Do not save networks if the dimension networks have been marked as read-only
        if (isRemote) {
            NyaLib.LOGGER.info("Skipping loading NyaLib networks in dimension " + dimensionId + " because the world is remote");
            return;
        }
        
        // If the world has no chunk storage (For example AMI Inventory World), do not attempt to load
        if (world.storage.getChunkStorage(dimension) == null) {
            NyaLib.LOGGER.info("Skipping loading NyaLib networks in dimension " + dimensionId + " because there is no chunk storage");
            return;
        }

        // When a dimension is being loaded again from a save-file, remove its read-only status if it had one
        readOnly.remove(dimension);

        NyaLib.LOGGER.debug("Loading NyaLib networks in dimension " + dimensionId);
        
        // Load the networks
        try {
            File file = world.storage.getWorldPropertiesFile("nyalib_networks");
            if (file.exists()) {
                NbtCompound tag = NbtIo.readCompressed(new FileInputStream(file));

                NetworkManager.NETWORKS.put(dimension, new Object2ObjectOpenHashMap<>());
                NetworkManager.removeQueue.computeIfAbsent(dimension, dim -> new ObjectOpenHashSet<>());
                NetworkManager.NEXT_ID.set(tag.getInt("next_id"));
                NetworkManager.readNbt(world, tag);

                int networkCount = 0;
                for (var typeNetworks : NetworkManager.NETWORKS.get(dimension).values()) {
                    networkCount += typeNetworks.size();
                }

                NyaLib.LOGGER.info("Loaded {} NyaLib networks in dimension {}", networkCount, dimensionId);
            }
        } catch (Exception e) {
            NyaLib.LOGGER.error("Error occured while loading NyaLib Networks in dimension " + dimensionId + ", networks are now read only to prevent saving corrupted data", e);
            readOnly.add(dimension);
        }
    }
}
