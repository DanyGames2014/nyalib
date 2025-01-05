package net.danygames2014.nyalib.util;

import net.danygames2014.nyalib.packet.MusicPacket;
import net.danygames2014.nyalib.packet.SoundPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;

public class SoundHelper {
    /**
     * Play a sound for all players at their position
     */
    public static void playSound(World world, String sound, float volume, float pitch) {
        for (Object playerO : world.players) {
            if (playerO instanceof PlayerEntity player) {
                playSound(player, player.x, player.y, player.z, sound, volume, pitch);
            }
        }
    }

    /**
     * Play a sound on the given position for all players in range
     */
    public static void playSound(World world, double x, double y, double z, String sound, float volume, float pitch, int range) {
        for (Object playerO : world.players) {
            if (playerO instanceof PlayerEntity player) {
                if (player.getDistance(x, y, z) <= range) {
                    playSound(player, x, y, z, sound, volume, pitch);
                }
            }
        }
    }

    /**
     * Play a sound on the given position for all players in range of 64 blocks
     */
    public static void playSound(World world, double x, double y, double z, String sound, float volume, float pitch) {
        playSound(world, x, y, z, sound, volume, pitch, 64);
    }

    /**
     * Play a sound to a given player at a given position
     */
    public static void playSound(PlayerEntity player, double x, double y, double z, String sound, float volume, float pitch) {
        PacketHelper.sendTo(player, new SoundPacket(x, y, z, volume, pitch, sound));
    }

    /**
     * Play a sound to a given player at their position
     */
    public static void playSound(PlayerEntity player, String sound, float volume, float pitch) {
        PacketHelper.sendTo(player, new SoundPacket(player.x, player.y, player.z, volume, pitch, sound));
    }

    // Music

    /**
     * Play a sound for all players at their position
     */
    public static void playMusic(World world, String name) {
        for (Object playerO : world.players) {
            if (playerO instanceof PlayerEntity player) {
                playMusic(player, player.x, player.y, player.z, name);
            }
        }
    }

    /**
     * Play a sound on the given position for all players in range
     */
    public static void playMusic(World world, double x, double y, double z, String name, int range) {
        for (Object playerO : world.players) {
            if (playerO instanceof PlayerEntity player) {
                if (player.getDistance(x, y, z) <= range) {
                    playMusic(player, x, y, z, name);
                }
            }
        }
    }

    /**
     * Play a sound on the given position for all players in range of 64 blocks
     */
    public static void playMusic(World world, double x, double y, double z, String name) {
        playMusic(world, x, y, z, name, 64);
    }

    /**
     * Play a sound to a given player at a given position
     */
    public static void playMusic(PlayerEntity player, double x, double y, double z, String name) {
        if (name == null) {
            name = "";
        }
        
        PacketHelper.sendTo(player, new MusicPacket((int) x, (int) y, (int) z, name));
    }

    /**
     * Play a sound to a given player at their position
     */
    public static void playMusic(PlayerEntity player, String name) {
        if (name == null) {
            name = "";
        }

        PacketHelper.sendTo(player, new MusicPacket((int) player.x, (int) player.y, (int) player.z, name));
    }
}
