package net.danygames2014.nyalib.network;

import net.danygames2014.nyalib.NyaLib;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.modificationstation.stationapi.api.event.world.WorldEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

@SuppressWarnings("unused")
public class NetworkLoader {

    public static boolean readOnly = false;

    @EventListener
    public void saveNetworks(WorldEvent.Save event) {
        NyaLib.LOGGER.debug("Saving NyaLib networks");

        if (readOnly) {
            NyaLib.LOGGER.warn("Saving NyaLib networks prevented as they are read-only due to error when loading.");
        }

        try {
            File file = event.world.dimensionData.method_1736("nyalib_networks");

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
        try {
            File file = event.world.dimensionData.method_1736("nyalib_networks");
            if (file.exists()) {
                NbtCompound tag = NbtIo.readCompressed(new FileInputStream(file));

                NetworkManager.NEXT_ID.set(tag.getInt("next_id"));
                NetworkManager.readNbt(event.world, tag);

                NyaLib.LOGGER.info("Loaded NyaLib networks");
            }
        } catch (Exception e) {
            NyaLib.LOGGER.error("Error occured while loading NyaLib Networks, networks are now read only to prevent saving corrupted data", e);
            readOnly = true;
        }
    }
}
