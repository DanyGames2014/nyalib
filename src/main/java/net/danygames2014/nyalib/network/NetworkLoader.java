package net.danygames2014.nyalib.network;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.modificationstation.stationapi.api.event.world.WorldEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

@SuppressWarnings("CallToPrintStackTrace")
public class NetworkLoader {

    @EventListener
    public void saveNetworks(WorldEvent.Save event) {
        try {
            File file = event.world.method_261().method_1736("nyalib_networks");
            if (file.exists()) {
                NbtCompound tag = NbtIo.readCompressed(new FileInputStream(file));

                // Tag
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventListener
    public void loadNetworks(WorldEvent.Init event) {
        try {
            File file = event.world.method_261().method_1736("nyalib_networks");
            if (file.exists()) {
                NbtCompound tag = NbtIo.readCompressed(new FileInputStream(file));

                // Modify Tag Here

                NbtIo.writeCompressed(tag, new FileOutputStream(file));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
