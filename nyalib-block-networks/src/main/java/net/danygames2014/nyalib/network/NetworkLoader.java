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
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings({"unused", "OptionalIsPresent"})
public class NetworkLoader {
    // TODO: Flush the executor when quitting world, maybe during loading?
    private static final ExecutorService SAVE_EXECUTOR = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r);
        thread.setName("NyaLib Block Networks Save Thread");
        thread.setDaemon(true);
        return thread;
    });
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

        File mainFile = world.storage.getWorldPropertiesFile("nyalib_networks");

        // If the file is null, do not attempt to save
        if (mainFile == null || mainFile.getName().trim().isEmpty() || mainFile.isDirectory()) {
            return;
        }

        NbtCompound tag = new NbtCompound();

        if (mainFile.exists()) {
            try (FileInputStream fis = new FileInputStream(mainFile)) {
                tag = NbtIo.readCompressed(fis);
            } catch (Exception e) {
                NyaLib.LOGGER.error("Failed to read NyaLib networks in dimension " + dimensionId + ". Aborting save", e);
                return;
            }
        }

        tag.putInt("next_id", NetworkManager.NEXT_ID.get());
        
        try {
            NetworkManager.writeNbt(world, tag);
        } catch (Exception e) {
            NyaLib.LOGGER.error("Failed to write NyaLib networks in dimension " + dimensionId + ". Aborting save", e);
            return;
        }

        // Save the networks off-thread
        NbtCompound writtenTag = tag;
        SAVE_EXECUTOR.submit(() -> {
            NyaLib.LOGGER.debug("Saving NyaLib networks in dimension " + dimensionId);

            try {
                File newFile = new File(mainFile.getAbsolutePath() + ".new");
                File oldFile = new File(mainFile.getAbsolutePath() + ".old");

                // First save into the new file
                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    NbtIo.writeCompressed(writtenTag, fos);
                }

                // Demote the main file to the backup file
                if (mainFile.exists()) {
                    renameFile(mainFile, oldFile);
                }

                // Promote the temporary file to the main file
                boolean promoted = renameFile(newFile, mainFile);

                if (!promoted && !mainFile.exists() && oldFile.exists()) {
                    renameFile(oldFile, mainFile);
                    NyaLib.LOGGER.error("Failed to promote NyaLib networks new file in dimension " + dimensionId + " to the main file.");
                    return;
                }

                NyaLib.LOGGER.info("Saved NyaLib networks in dimension " + dimensionId);
            } catch (Exception e) {
                NyaLib.LOGGER.error("Error occured while saving NyaLib Networks in dimension " + dimensionId, e);
            }
        });
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

        // Do not load networks if the dimension networks have been marked as read-only
        if (isRemote) {
            NyaLib.LOGGER.info("Skipping loading NyaLib networks in dimension " + dimensionId + " because the world is remote");
            return;
        }

        // If the world has no chunk storage (For example AMI Inventory World), do not attempt to load
        if (world.storage.getChunkStorage(dimension) == null) {
            NyaLib.LOGGER.info("Skipping loading NyaLib networks in dimension " + dimensionId + " because there is no chunk storage");
            return;
        }

        // Construct the file paths
        File mainFile = world.storage.getWorldPropertiesFile("nyalib_networks");
        if (mainFile == null) {
            NyaLib.LOGGER.info("Skipping loading NyaLib networks in dimension " + dimensionId + " as it cannot provide a data file");
            return;
        }

        File newFile = new File(mainFile.getAbsolutePath() + ".new");
        File oldFile = new File(mainFile.getAbsolutePath() + ".old");

        // When a dimension is being loaded again from a save-file, remove its read-only status if it had one
        readOnly.remove(dimension);

        // Load the networks
        NyaLib.LOGGER.info("Loading NyaLib networks in dimension " + dimensionId);

        try {
            // Try to load the main file
            if (mainFile.exists()) {
                if (loadNetworksFromFile(world, dimension, dimensionId, mainFile, false)) {
                    //noinspection ResultOfMethodCallIgnored I don't care
                    newFile.delete();
                    return;
                } else {
                    NyaLib.LOGGER.warn("Failed to load NyaLib networks in dimension " + dimensionId + " from the main file.");
                }
            }

            // If loading from the main file fails, try to load from the temporary file
            if (newFile.exists()) {
                if (loadNetworksFromFile(world, dimension, dimensionId, newFile, false)) {
                    renameFile(newFile, mainFile);
                    return;
                } else {
                    NyaLib.LOGGER.warn("Failed to load NyaLib networks in dimension " + dimensionId + " from the temporary file.");
                }
            }

            // As a last resort try to load from the .old backup file
            if (oldFile.exists()) {
                if (loadNetworksFromFile(world, dimension, dimensionId, oldFile, false)) {
                    renameFile(oldFile, mainFile);
                    return;
                } else {
                    NyaLib.LOGGER.warn("Failed to load NyaLib networks in dimension " + dimensionId + " from the backup file. Networks will be reinitialized.");
                }
            }

            loadNetworksFromFile(world, dimension, dimensionId, null, true);
        } catch (Exception e) {
            NyaLib.LOGGER.error("Error occured while loading NyaLib Networks in dimension " + dimensionId, e);
        }
    }

    private boolean loadNetworksFromFile(World world, Dimension dimension, String dimensionId, File file, boolean init) {
        try {
            NbtCompound tag = new NbtCompound();
            if (init) {
                NyaLib.LOGGER.info("Initializing NyaLib networks in dimension " + dimensionId);
            } else {
                try (FileInputStream fis = new FileInputStream(file)) {
                    tag = NbtIo.readCompressed(fis);
                }
            }

            NetworkManager.NETWORKS.put(dimension, new Object2ObjectOpenHashMap<>());
            NetworkManager.removeQueue.computeIfAbsent(dimension, dim -> new ObjectOpenHashSet<>());
            NetworkManager.NEXT_ID.set(tag.getInt("next_id"));
            if (!init) {
                NetworkManager.readNbt(world, tag);
            }

            int networkCount = 0;
            for (var typeNetworks : NetworkManager.NETWORKS.get(dimension).values()) {
                networkCount += typeNetworks.size();
            }

            NyaLib.LOGGER.info("Loaded {} NyaLib networks in dimension {}", networkCount, dimensionId);
        } catch (Exception e) {
            NyaLib.LOGGER.error("Error occured while loading NyaLib Networks in dimension " + dimensionId, e);
            readOnly.add(dimension);

            // If the files exists and has failed to load, rename it to .corrupt
            if (file != null && file.exists()) {
                File corruptFile = new File(file.getAbsolutePath() + ".corrupt");
                renameFile(file, corruptFile);
            }

            return false;
        }

        // On succesfull load, remove the read-only flag
        readOnly.remove(dimension);
        return true;
    }

    @SuppressWarnings("UnusedReturnValue")
    private boolean renameFile(File source, File target) {
        if (source == null || target == null || !source.exists()) {
            return false;
        }

        try {
            Files.move(
                    source.toPath(),
                    target.toPath(),
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE
            );
            return true;
        } catch (Exception e) {
            NyaLib.LOGGER.error("Failed to rename file from " + source.getName() + " to " + target.getName(), e);
            return false;
        }
    }
}
