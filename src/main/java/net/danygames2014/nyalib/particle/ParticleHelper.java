package net.danygames2014.nyalib.particle;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;

public class ParticleHelper {
    // Normal particles

    /**
     * Spawn a particle for all players in the given range
     */
    public static void addParticle(World world, String particle, double x, double y, double z, double velocityX, double velocityY, double velocityZ, int range) {
        for (Object playerO : world.players) {
            if (playerO instanceof PlayerEntity player) {
                if (player.getDistance(x, y, z) <= range) {
                    addParticle(player, particle, x, y, z, velocityX, velocityY, velocityZ);
                }
            }
        }
    }

    /**
     * Spawn a particle for all players in a range of 32 blocks
     */
    public static void addParticle(World world, String particle, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        addParticle(world, particle, x, y, z, velocityX, velocityY, velocityZ, 32);
    }


    /**
     * Spawn a particle for the given player at the given coordinates
     */
    public static void addParticle(PlayerEntity player, String particle, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        PacketHelper.sendTo(player, new ParticlePacket(particle, x, y, z, velocityX, velocityY, velocityZ));
    }

    // Item Particles

    /**
     * Spawn a particle for all players in the given range
     */
    public static void addItemParticle(World world, Item item, double x, double y, double z, double velocityX, double velocityY, double velocityZ, int range) {
        for (Object playerO : world.players) {
            if (playerO instanceof PlayerEntity player) {
                if (player.getDistance(x, y, z) <= range) {
                    addItemParticle(player, item, x, y, z, velocityX, velocityY, velocityZ);
                }
            }
        }
    }

    /**
     * Spawn a particle for all players in a range of 32 blocks
     */
    public static void addItemParticle(World world, Item item, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        addItemParticle(world, item, x, y, z, velocityX, velocityY, velocityZ, 32);
    }

    /**
     * Spawn an item particle for the given player at the given coordinates
     */
    public static void addItemParticle(PlayerEntity player, Item item, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        PacketHelper.sendTo(player, new ParticlePacket(item, x, y, z, velocityX, velocityY, velocityZ));
    }
}
