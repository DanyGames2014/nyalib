package net.danygames2014.nyalib.energy.template.block.entity;

import net.danygames2014.nyalib.energy.EnergyConsumer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

/**
 * A template for an Energy Consumer Block Entity
 */
public abstract class EnergyConsumerBlockEntityTemplate extends BlockEntity implements EnergyConsumer {
    public int energy;

    @Override
    public void tick() {
        consumed = 0;
    }

    @Override
    public abstract int getMaxInputVoltage(@Nullable Direction direction);

    @Override
    public abstract int getMaxEnergyInput(@Nullable Direction direction);

    @Override
    public abstract boolean canReceiveEnergy(@Nullable Direction direction);

    @Override
    public abstract void onOvervoltage(@Nullable Direction direction, double voltage);

    @Override
    public abstract boolean canConnectEnergy(Direction direction);

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public abstract int getEnergyCapacity();

    @Override
    public int setEnergy(int value) {
        this.energy = value;

        if (energy > getEnergyCapacity()) {
            energy = getEnergyCapacity();
        }

        return energy;
    }

    public int consumed = 0;
    @Override
    public int receiveEnergy(@Nullable Direction direction, int voltage, int energy) {
        // If we cannot receive energy in this direction, dont care, return zero
        if (!canReceiveEnergy(direction)) {
            return 0;
        }

        // Cap the energy on the maximum this machine will be able to receive
        energy = Math.min(energy, getMaxEnergyInput(direction) - consumed);
        
        // If the received power is zero or negative, return zero
        if (energy <= 0) {
            return 0;
        }

        // If we wouldn't be able to store any power anyway, dont bother calculating and return zero
        if (getRemainingCapacity() <= 0) {
            return 0;
        }

        // Check if the voltage is higher than the maximum input voltage
        if (voltage > getMaxInputVoltage(direction)) {
            // If the voltage is higher, trigger an overvoltage event and return zero
            this.onOvervoltage(direction, voltage);
            return 0;
        }

        // Return the used power
        int consumedPower = addEnergy(Math.min(energy, getMaxEnergyInput(direction)));
        
        // Increase the consumed last tick value
        consumed += consumedPower;
        
        // Return the maount of power used
        return consumedPower;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("energy", energy);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        energy = nbt.getInt("energy");
    }
}
