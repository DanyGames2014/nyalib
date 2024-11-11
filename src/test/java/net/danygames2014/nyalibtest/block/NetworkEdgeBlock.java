package net.danygames2014.nyalibtest.block;

import net.danygames2014.nyalib.network.Network;
import net.danygames2014.nyalib.network.NetworkEdgeComponent;
import net.danygames2014.nyalib.network.NetworkManager;
import net.danygames2014.nyalib.network.NetworkType;
import net.danygames2014.nyalibtest.NyaLibTest;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;

public class NetworkEdgeBlock extends TemplateBlock implements NetworkEdgeComponent {
    public static World theWorld;

    public NetworkEdgeBlock(Identifier identifier) {
        super(identifier, Material.STONE);
    }

    @Override
    public NetworkType getNetworkType() {
        return NyaLibTest.energyNetworkType;
    }

    @Override
    public void onPlaced(World world, int x, int y, int z) {
        super.onPlaced(world, x, y, z);
        theWorld = world;
    }

    @Override
    public void update(World world, int x, int y, int z, Network network) {
        world.notifyNeighbors(x,y,z, 0);
    }

    @Override
    public int getColorMultiplier(BlockView blockView, int x, int y, int z) {
        if (theWorld != null) {
            Network net = NetworkManager.getAt(theWorld.dimension, x, y, z, this.getNetworkTypes().get(0).getIdentifier());

            if (net != null) {
                return net.hashCode();
            }
        }

        return 0;
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        ArrayList<Network> networks = NetworkManager.getAt(world.dimension, x, y, z, this.getNetworkTypes());
        player.method_490("This block is in networks:");
        for (var net : networks){
            player.method_490("NET " + net.getId() + " HASHCODE: " + net.hashCode());
        }
        return true;
    }
}
