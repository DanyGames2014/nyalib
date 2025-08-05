package net.danygames2014.nyalib.capability.block.energyhandler;

import net.danygames2014.nyalib.energy.EnergyConsumer;
import net.danygames2014.nyalib.energy.EnergyHandler;
import net.danygames2014.nyalib.energy.EnergySource;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

class EnergyHandlerInterfaceBlockCapability extends EnergyHandlerBlockCapability{
    private final EnergyHandler energyHandler;
    private final EnergyConsumer energyConsumer;
    private final EnergySource energySource;

    public EnergyHandlerInterfaceBlockCapability(EnergyHandler energyHandler, EnergyConsumer energyConsumer, EnergySource energySource) {
        this.energyHandler = energyHandler;
        this.energyConsumer = energyConsumer;
        this.energySource = energySource;
    }

    // Energy Capable
    @Override
    public boolean canConnectEnergy(Direction direction) {
        return energyHandler.canConnectEnergy(direction);
    }

    // Energy Storage
    @Override
    public int getEnergyStored() {
        return energyHandler.getEnergyStored();
    }

    @Override
    public int getEnergyCapacity() {
        return energyHandler.getEnergyCapacity();
    }

    @Override
    public int getRemainingCapacity() {
        return energyHandler.getRemainingCapacity();
    }

    @Override
    public int setEnergy(int value) {
        return energyHandler.setEnergy(value);
    }

    @Override
    public int changeEnergy(int difference) {
        return energyHandler.changeEnergy(difference);
    }

    @Override
    public int addEnergy(int amount) {
        return energyHandler.addEnergy(amount);
    }

    @Override
    public int removeEnergy(int amount) {
        return energyHandler.removeEnergy(amount);
    }

    // Energy Consumer
    @Override
    public int getMaxInputVoltage(@Nullable Direction direction) {
        if (energyConsumer != null) {
            return energyConsumer.getMaxInputVoltage(direction);
        }
        return 0;
    }

    @Override
    public int getMaxEnergyInput(@Nullable Direction direction) {
        if (energyConsumer != null) {
            return energyConsumer.getMaxEnergyInput(direction);
        }
        return 0;
    }

    @Override
    public boolean canReceiveEnergy(@Nullable Direction direction) {
        if (energyConsumer != null) {
            return energyConsumer.canReceiveEnergy(direction);
        }
        return false;
    }

    @Override
    public int receiveEnergy(@Nullable Direction direction, int voltage, int energy) {
        if (energyConsumer != null) {
            return energyConsumer.receiveEnergy(direction, voltage, energy);
        }
        return 0;
    }

    @Override
    public void onOvervoltage(@Nullable Direction direction, double voltage) {
        if (energyConsumer != null) {
            energyConsumer.onOvervoltage(direction, voltage);
        }
    }

    // Energy Source
    @Override
    public int getMaxOutputVoltage(@Nullable Direction direction) {
        if (energySource != null) {
            return energySource.getMaxOutputVoltage(direction);
        }
        return 0;
    }

    @Override
    public int getOutputVoltage(@Nullable Direction direction) {
        if (energySource != null) {
            return energySource.getOutputVoltage(direction);
        }
        return 0;
    }

    @Override
    public int getMaxEnergyOutput(@Nullable Direction direction) {
        if (energySource != null) {
            return energySource.getMaxEnergyOutput(direction);
        }
        return 0;
    }

    @Override
    public boolean canExtractEnergy(@Nullable Direction direction) {
        if (energySource != null) {
            return energySource.canExtractEnergy(direction);
        }
        return false;
    }

    @Override
    public int extractEnergy(@Nullable Direction direction, int requestedEnergy) {
        if (energySource != null) {
            return energySource.extractEnergy(direction, requestedEnergy);
        }
        return 0;
    }
}
