package net.danygames2014.nyalibtest.block.energy.entity;

import net.danygames2014.nyalib.energy.EnergyConsumer;
import net.danygames2014.nyalib.energy.EnergySource;
import net.danygames2014.nyalib.network.Network;
import net.danygames2014.nyalib.network.energy.EnergyNetwork;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class GeneratorBlockEntity extends BlockEntity implements EnergySource {

    public int energy = 0;
    public ArrayList<EnergyNetwork> energyNets = new ArrayList<>(2);

    @Override
    public void tick() {
        if (energy > 0) {
            for (Direction side : Direction.values()) {
                if (world.getBlockEntity(x + side.getOffsetX(), y + side.getOffsetY(), z + side.getOffsetZ()) instanceof EnergyConsumer consumer) {
                    int usedPower = consumer.receiveEnergy(side.getOpposite(), getOutputVoltage(side), energy);
                    removeEnergy(usedPower);
                }
            }

            for (EnergyNetwork energyNet : energyNets) {
                int usedPower = energyNet.provideEnergy(this, new Vec3i(this.x, this.y, this.z), getOutputVoltage(null), energy);
                removeEnergy(usedPower);
            }
        }
    }

    public void addedToNet(World world, int x, int y, int z, Network network) {
        if (network instanceof EnergyNetwork energyNet) {
            if (!energyNets.contains(energyNet)) {
                energyNets.add(energyNet);
            }
        }
    }

    public void removedFromNet(World world, int x, int y, int z, Network network) {
        if (network instanceof EnergyNetwork energyNet) {
            energyNets.remove(energyNet);
        }
    }

    public void update(World world, int x, int y, int z, Network network) {
        if (network instanceof EnergyNetwork energyNet) {
            if (!energyNets.contains(energyNet)) {
                energyNets.add(energyNet);
            }
        }
    }

    @Override
    public int getOutputVoltage(@Nullable Direction direction) {
        return 12;
    }

    @Override
    public int getMaxOutputVoltage(@Nullable Direction direction) {
        return 12;
    }

    @Override
    public int getMaxEnergyOutput(@Nullable Direction direction) {
        return 12;
    }

    @Override
    public boolean canExtractEnergy(@Nullable Direction direction) {
        return true;
    }

    @Override
    public void onOvervoltage(@Nullable Direction direction, double voltage) {

    }

    @Override
    public boolean canConnectEnergy(Direction direction) {
        return true;
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public int getEnergyCapacity() {
        return 1000;
    }

    @Override
    public int setEnergy(int value) {
        this.energy = value;

        if (energy > getEnergyCapacity()) {
            energy = getEnergyCapacity();
        }

        return energy;
    }
}
