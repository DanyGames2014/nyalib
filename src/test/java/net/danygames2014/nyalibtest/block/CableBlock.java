package net.danygames2014.nyalibtest.block;

import net.danygames2014.nyalib.network.*;
import net.danygames2014.nyalibtest.NyaLibTest;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class CableBlock extends TemplateBlock implements NetworkComponent {
    public static World theWorld;

    public CableBlock(Identifier identifier) {
        super(identifier, Material.STONE);
    }

    @Override
    public NetworkType getNetworkType() {
        return NyaLibTest.energyNetworkType;
    }

    @Override
    public void onPlaced(World world, int x, int y, int z) {
        NetworkManager.addBlock(x, y, z, world, this, this);
        theWorld = world;
    }

    @Override
    public void onBreak(World world, int x, int y, int z) {
        NetworkManager.removeBlock(x, y, z, world, this, this);
    }

    @Override
    public int getColorMultiplier(BlockView blockView, int x, int y, int z) {
        if (theWorld != null) {
            Network net = NetworkManager.getAt(x, y, z, theWorld.dimension, this.getNetworkType().getIdentifier());

            if (net != null) {
                return net.hashCode();
            }
        }

        return 8989898;
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        Network net = NetworkManager.getAt(x, y, z, world.dimension, this.getNetworkType().getIdentifier());
        if (net != null) {
            player.method_490("NET " + net.getId() + " HASHCODE: " + net.hashCode());
        }
        return true;
    }
}
