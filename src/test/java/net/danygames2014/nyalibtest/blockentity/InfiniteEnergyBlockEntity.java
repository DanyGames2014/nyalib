package net.danygames2014.nyalibtest.blockentity;

import net.danygames2014.nyalib.energy.EnergyDevice;
import net.minecraft.block.entity.BlockEntity;
import net.modificationstation.stationapi.api.util.math.Direction;

public class InfiniteEnergyBlockEntity extends BlockEntity implements EnergyDevice {
    @Override
    public void tick() {
        for (Direction dir : Direction.values()) {
            BlockEntity blockEntity = world.getBlockEntity(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ());
            if (blockEntity instanceof EnergyDevice energyDevice) {
                if (energyDevice.canInsertEnergy(dir.getOpposite())) {
                    energyDevice.insertEnergy(Integer.MAX_VALUE, dir.getOpposite());
                }
            }
        }
    }

    @Override
    public boolean canConnect(Direction direction) {
        return true;
    }

    @Override
    public boolean canExtractEnergy(Direction direction) {
        return true;
    }

    @Override
    public int extractEnergy(int amount, Direction direction) {
        return amount;
    }

    @Override
    public boolean canInsertEnergy(Direction direction) {
        return false;
    }

    @Override
    public int insertEnergy(int amount, Direction direction) {
        return 0;
    }
}
