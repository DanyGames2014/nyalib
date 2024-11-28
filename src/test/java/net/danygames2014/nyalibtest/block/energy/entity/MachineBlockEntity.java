package net.danygames2014.nyalibtest.block.energy.entity;

import net.danygames2014.nyalib.energy.EnergyHandler;
import net.danygames2014.nyalib.network.NetworkManager;
import net.danygames2014.nyalib.network.energy.EnergyNetwork;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.Vec3i;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class MachineBlockEntity extends BlockEntity implements EnergyHandler {
    public int energy;

    @Override
    public void tick() {
        super.tick();

        if (energy < this.getEnergyCapacity() - 50) {
            for (var net : NetworkManager.getAt(this.world.dimension, this.x, this.y, this.z)) {
                if (net instanceof EnergyNetwork energyNet) {
                    energyNet.requestEnergy(this, new Vec3i(this.x, this.y, this.z), this.getMaxInputAmperage(null));
                }
            }
        }
    }

    @Override
    public int getMaxInputVoltage(@Nullable Direction direction) {
        return 220;
    }

    @Override
    public double getMaxInputAmperage(@Nullable Direction direction) {
        return 1;
    }

    @Override
    public int getOutputVoltage(@Nullable Direction direction) {
        return 0;
    }

    @Override
    public int getMaxOutputVoltage(@Nullable Direction direction) {
        return 0;
    }

    @Override
    public double getMaxOutputAmperage(@Nullable Direction direction) {
        return 0;
    }

    @Override
    public boolean canReceiveEnergy(@Nullable Direction direction) {
        return true;
    }

    @Override
    public boolean canExtractEnergy(@Nullable Direction direction) {
        return false;
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
        return 500;
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
