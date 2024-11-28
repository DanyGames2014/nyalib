package net.danygames2014.nyalibtest.block.energy;

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
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        if (world.getBlockEntity(x, y, z) instanceof MachineBlockEntity machine) {
            if (player.isSneaking()) {
                machine.removeEnergy(10);
            }

            player.method_490(machine.getEnergyStored() + "/" + machine.getEnergyCapacity());
            return true;
        }
        return false;
    }
}
