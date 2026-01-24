package net.danygames2014.nyalib.network;

import net.danygames2014.nyalib.NyaLib;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.ClientWorld;
import net.modificationstation.stationapi.api.event.world.WorldEvent;
import net.modificationstation.stationapi.api.util.SideUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("unused")
public class NetworkLoader {

    public static boolean readOnly = false;
    public static boolean isRemote = false;

    @EventListener
    public void saveNetworks(WorldEvent.Save event) {
        NyaLib.LOGGER.debug("Saving NyaLib networks");

        if (isRemote) {
            return;
        }

        if (readOnly) {
            NyaLib.LOGGER.warn("Saving NyaLib networks prevented as they are read-only due to error when loading.");
            return;
        }

        try {
            File file = event.world.storage.getWorldPropertiesFile("nyalib_networks");

            NbtCompound tag = new NbtCompound();
            if (file.exists()) {
                tag = NbtIo.readCompressed(new FileInputStream(file));
            }

            tag.putInt("next_id", NetworkManager.NEXT_ID.get());
            NetworkManager.writeNbt(event.world, tag);

            NbtIo.writeCompressed(tag, new FileOutputStream(file));
            NyaLib.LOGGER.debug("Saved NyaLib networks");
        } catch (Exception e) {
            NyaLib.LOGGER.error("Error occured while saving NyaLib Networks", e);
        }
    }

    @EventListener
    public void loadNetworks(WorldEvent.Init event) {
        NyaLib.LOGGER.debug("Loading NyaLib networks");

        if (event.world == null) {
            return;
        }

        isRemote = SideUtil.get(() -> event.world instanceof ClientWorld, () -> false);

        if (isRemote) {
            NyaLib.LOGGER.info("Skipping loading NyaLib networks because of the world being remote");
            return;
        }
        
        readOnly = false;

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

                NyaLib.LOGGER.info("Loaded {} NyaLib networks", networkCount);
            }
        } catch (Exception e) {
            NyaLib.LOGGER.error("Error occured while loading NyaLib Networks, networks are now read only to prevent saving corrupted data", e);
            readOnly = true;
        }
    }
}
