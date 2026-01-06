package net.danygames2014.nyalibtest.sound;

import net.danygames2014.nyalib.particle.ParticleHelper;
import net.danygames2014.nyalib.sound.SoundHelper;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class ServerSoundBlock extends TemplateBlock {
    public ServerSoundBlock(Identifier identifier, Material material) {
        super(identifier, material);
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        if (world.isRemote) {
            return false;
        }

        if (player.isSneaking()) {
            SoundHelper.playMusic(world, x, y, z, "cat");
        } else {
            SoundHelper.playMusic(world, x, y, z, null);
            SoundHelper.playSound(world, x, y, z, "random.explode", 1.0F, (world.random.nextFloat() * 0.4F) + 0.6F);

            for (int i = 0; i < 8; i++) {
                ParticleHelper.addParticle(world, "lava", x + 0.5, y + 1, z + 0.5, world.random.nextDouble() * 4, world.random.nextDouble() * 4, world.random.nextDouble() * 4);
            }

            ParticleHelper.addItemParticle(world, Item.DIAMOND, x + 1, y + 1.5, z, world.random.nextDouble() * 4, world.random.nextDouble() * 4, world.random.nextDouble() * 4);
            ParticleHelper.addItemParticle(world, Item.DIAMOND, x - 1, y + 1.5, z, world.random.nextDouble() * 4, world.random.nextDouble() * 4, world.random.nextDouble() * 4);
            ParticleHelper.addItemParticle(world, Item.DIAMOND, x, y + 1.5, z + 1, world.random.nextDouble() * 4, world.random.nextDouble() * 4, world.random.nextDouble() * 4);
            ParticleHelper.addItemParticle(world, Item.DIAMOND, x, y + 1.5, z - 1, world.random.nextDouble() * 4, world.random.nextDouble() * 4, world.random.nextDouble() * 4);
        }

        return true;
    }
}
