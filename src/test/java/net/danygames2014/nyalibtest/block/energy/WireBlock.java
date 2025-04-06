package net.danygames2014.nyalibtest.block.energy;

import net.danygames2014.nyalib.energy.template.block.EnergyWireBlockTemplate;
import net.danygames2014.nyalib.network.Network;
import net.danygames2014.nyalib.network.NetworkManager;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;

public class WireBlock extends EnergyWireBlockTemplate {
    public WireBlock(Identifier identifier) {
        super(identifier, Material.WOOL);
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        if (!player.isSneaking()) {
            ArrayList<Network> networks = NetworkManager.getAt(world.dimension, x, y, z, this.getNetworkTypes());
            StringBuilder sb = new StringBuilder();
            sb.append("This block (x:" + x + " y:" + y + " z:" + z + ") is in networks:");
            for (var net : networks) {
                sb.append(" " + net.getId());
            }
            player.sendMessage(sb.toString());
            return true;
        }

        return super.onUse(world, x, y, z, player);
    }
}
