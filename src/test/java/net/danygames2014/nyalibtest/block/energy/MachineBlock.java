package net.danygames2014.nyalibtest.block.energy;

import net.danygames2014.nyalib.network.Network;
import net.danygames2014.nyalib.network.NetworkEdgeComponent;
import net.danygames2014.nyalib.network.NetworkType;
import net.danygames2014.nyalibtest.block.energy.entity.MachineBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;

public class MachineBlock extends TemplateBlockWithEntity implements NetworkEdgeComponent {
    public MachineBlock(Identifier identifier) {
        super(identifier, Material.METAL);
    }

    @Override
    public NetworkType getNetworkType() {
        return NetworkType.ENERGY;
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new MachineBlockEntity();
    }

    @Override
    public void onAddedToNet(World world, int x, int y, int z, Network network) {
        if (world.getBlockEntity(x, y, z) instanceof MachineBlockEntity machine) {
            machine.addedToNet(world, x, y, z, network);
        }
    }

    @Override
    public void onRemovedFromNet(World world, int x, int y, int z, Network network) {
        if (world.getBlockEntity(x, y, z) instanceof MachineBlockEntity machine) {
            machine.removedFromNet(world, x, y, z, network);
        }
    }

    @Override
    public void update(World world, int x, int y, int z, Network network) {
        if (world.getBlockEntity(x, y, z) instanceof MachineBlockEntity machine) {
            machine.update(world, x, y, z, network);
        }
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        if (world.getBlockEntity(x, y, z) instanceof MachineBlockEntity machine) {
            if (player.isSneaking()) {
                machine.removeEnergy(10);
            }

            player.method_490(machine.getEnergyStored() + "/" + machine.getEnergyCapacity() + "[" + machine.hashCode() + "]");
            return true;
        }
        return false;
    }
}
