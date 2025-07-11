package net.danygames2014.nyalibtest.block.network;

import net.danygames2014.nyalib.network.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;

public class CableBlock extends TemplateBlock implements NetworkNodeComponent {
    public static World theWorld;

    public CableBlock(Identifier identifier) {
        super(identifier, Material.STONE);
    }

    @Override
    public NetworkType getNetworkType() {
        return NetworkType.ENERGY;
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
        player.sendMessage("This block is in networks:");
        for (var net : networks){
            player.sendMessage("NET " + net.getId() + " HASHCODE: " + net.hashCode());
        }
        return true;
    }
}
