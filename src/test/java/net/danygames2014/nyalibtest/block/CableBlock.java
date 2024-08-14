package net.danygames2014.nyalibtest.block;

import net.danygames2014.nyalib.network.Network;
import net.danygames2014.nyalib.network.NetworkComponent;
import net.danygames2014.nyalib.network.NetworkManager;
import net.danygames2014.nyalib.network.NetworkType;
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
    public Identifier getNetworkTypeIdentifier() {
        return NetworkType.ENERGY.getType();
    }

    @Override
    public void onPlaced(World world, int x, int y, int z) {
        NetworkManager.addBlock(x, y, z, world, this, this);
        theWorld = world;
    }

    @Override
    public int getColorMultiplier(BlockView blockView, int x, int y, int z) {
        if (theWorld != null) {
            Network net = NetworkManager.getAt(x, y, z, theWorld.dimension, this.getNetworkTypeIdentifier());

            if (net != null) {
                return net.hashCode();
            }
        }

        return 8989898;
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        Network net = NetworkManager.getAt(x, y, z, world.dimension, this.getNetworkTypeIdentifier());
        if (net != null) {
            player.method_490("NET " + net.getId() + " HASHCODE: " + net.hashCode());
        }
        return true;
    }
}
