package net.danygames2014.nyalibtest.block.simpleenergy.entity;

import net.danygames2014.nyalib.energy.simple.SimpleEnergyHandler;
import net.minecraft.block.entity.BlockEntity;
import net.modificationstation.stationapi.api.util.math.Direction;

public class InfiniteSimpleEnergyBlockEntity extends BlockEntity implements SimpleEnergyHandler {
    @Override
    public void tick() {
        for (Direction dir : Direction.values()) {
            BlockEntity blockEntity = world.getBlockEntity(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ());
            if (blockEntity instanceof SimpleEnergyHandler simpleEnergyHandler) {
                if (simpleEnergyHandler.canInsertEnergy(dir.getOpposite())) {
                    simpleEnergyHandler.insertEnergy(Integer.MAX_VALUE, dir.getOpposite());
                }
            }
        }
    }

    @Override
    public boolean canConnectEnergy(Direction direction) {
        return true;
    }

    @Override
    public boolean canInsertEnergy(Direction direction) {
        return false;
    }

    @Override
    public int insertEnergy(int amount, Direction direction) {
        return 0;
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
    public int getEnergyStored() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getEnergyCapacity() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int setEnergy(int value) {
        return Integer.MAX_VALUE;
    }
}
