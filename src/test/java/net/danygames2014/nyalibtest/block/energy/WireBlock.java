package net.danygames2014.nyalibtest.block.energy;

import net.danygames2014.nyalib.network.Network;
import net.danygames2014.nyalib.network.NetworkManager;
import net.danygames2014.nyalib.network.NetworkNodeComponent;
import net.danygames2014.nyalib.network.NetworkType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;

public class WireBlock extends TemplateBlock implements NetworkNodeComponent {
    public WireBlock(Identifier identifier) {
        super(identifier, Material.METAL);
    }

    @Override
    public NetworkType getNetworkType() {
        return NetworkType.ENERGY;
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
