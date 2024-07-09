package net.danygames2014.nyalibtest.blockentity;

import net.danygames2014.nyalib.energy.EnergyDevice;
import net.danygames2014.nyalib.energy.EnergyStorage;
import net.minecraft.block.entity.BlockEntity;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;

public class EnergyReceiverBlockEntity extends BlockEntity implements EnergyDevice, EnergyStorage {
    public int storedEnergy;

    public EnergyReceiverBlockEntity() {
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
        storedEnergy = MathHelper.clamp(storedEnergy, 0, getMaxEnergyStored());
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
    public int getMaxEnergyStored() {
        return 150000;
    }
}
