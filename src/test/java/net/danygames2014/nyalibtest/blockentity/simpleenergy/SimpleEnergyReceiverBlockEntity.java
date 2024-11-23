package net.danygames2014.nyalibtest.blockentity.simpleenergy;

import net.danygames2014.nyalib.energy.EnergyStorage;
import net.danygames2014.nyalib.energy.simple.SimpleEnergyHandler;
import net.minecraft.block.entity.BlockEntity;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public class SimpleEnergyReceiverBlockEntity extends BlockEntity implements SimpleEnergyHandler {
    public int storedEnergy;

    public SimpleEnergyReceiverBlockEntity() {
        this.storedEnergy = 0;
    }

    @Override
    public boolean canExtractEnergy(@Nullable Direction direction) {
        return false;
    }

    @Override
    public int extractEnergy(int amount, @Nullable Direction direction) {
        return 0;
    }

    @Override
    public boolean canInsertEnergy(@Nullable Direction direction) {
        return true;
    }

    @Override
    public int insertEnergy(int amount, @Nullable Direction direction) {
        int prevStoredEnergy = storedEnergy;
        storedEnergy += Math.min(amount, 150);
        storedEnergy = MathHelper.clamp(storedEnergy, 0, getEnergyCapacity());
        return Math.max(storedEnergy - prevStoredEnergy, 0);
    }

    @Override
    public boolean canConnectEnergy(Direction direction) {
        return true;
    }

    @Override
    public int getEnergyStored() {
        return this.storedEnergy;
    }

    @Override
    public int setEnergy(int value) {
        this.storedEnergy = value;
        return storedEnergy;
    }

    @Override
    public int getEnergyCapacity() {
        return 150000;
    }
}
