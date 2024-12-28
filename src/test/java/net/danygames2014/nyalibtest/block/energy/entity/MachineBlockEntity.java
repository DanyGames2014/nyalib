package net.danygames2014.nyalibtest.block.energy.entity;

import net.danygames2014.nyalib.energy.EnergyConsumer;
import net.danygames2014.nyalib.network.Network;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class MachineBlockEntity extends BlockEntity implements EnergyConsumer {
    public int energy;

    @Override
    public void tick() {
        
    }

    @Override
    public int getMaxInputVoltage(@Nullable Direction direction) {
        return 12;
    }

    @Override
    public int getMaxEnergyInput(@Nullable Direction direction) {
        return 12;
    }

    @Override
    public boolean canReceiveEnergy(@Nullable Direction direction) {
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
