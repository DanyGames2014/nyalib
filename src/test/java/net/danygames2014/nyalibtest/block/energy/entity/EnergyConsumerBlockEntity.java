package net.danygames2014.nyalibtest.block.energy.entity;

import net.danygames2014.nyalib.energy.template.block.entity.EnergyConsumerBlockEntityTemplate;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class EnergyConsumerBlockEntity extends EnergyConsumerBlockEntityTemplate {

    boolean powered = false;
    
    @Override
    public void tick() {
        for (Direction side : Direction.values()) {
            if (world.isEmittingRedstonePower(x + side.getOffsetX(), y + side.getOffsetY(), z + side.getOffsetZ())) {
                powered = true;
            }
        }
        
        if (powered) {
            removeEnergy(1);
        }

        super.tick();
    }

    @Override
    public int getMaxInputVoltage(@Nullable Direction direction) {
        return 12;
    }

    @Override
    public int getMaxEnergyInput(@Nullable Direction direction) {
        return 2;
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
    public int getEnergyCapacity() {
        return 24;
    }
}
