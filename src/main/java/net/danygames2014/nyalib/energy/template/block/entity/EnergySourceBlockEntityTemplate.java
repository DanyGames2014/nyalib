package net.danygames2014.nyalib.energy.template.block.entity;

import net.danygames2014.nyalib.energy.EnergyConsumer;
import net.danygames2014.nyalib.energy.EnergySource;
import net.danygames2014.nyalib.network.Network;
import net.danygames2014.nyalib.network.energy.EnergyNetwork;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * A template for an Energy Source Block Entity
 */
public abstract class EnergySourceBlockEntityTemplate extends BlockEntity implements EnergySource {
    public int energy;
    public HashMap<EnergyNetwork, Direction> energyNets = new HashMap<>(2);

    @Override
    public void tick() {
        // We are using a push system, so the machine is responsible for sending the energy
        
        // Reset the extracted counter
        extracted = 0;
        
        // First check if we have anything to actually send
        if (energy > 0) {
            // If we do, first try to send to adjacent energy consumers
            for (Direction side : Direction.values()) {
                if (world.getBlockEntity(x + side.getOffsetX(), y + side.getOffsetY(), z + side.getOffsetZ()) instanceof EnergyConsumer consumer) {
                    int usedPower = consumer.receiveEnergy(side.getOpposite(), getOutputVoltage(side), Math.min(energy, getMaxEnergyOutput(side) - extracted));
                    removeEnergy(usedPower);
                    extracted += usedPower;
                }
            }

            // After that, try to send energy to the energy networks this source is part of
            for (Map.Entry<EnergyNetwork, Direction> energyNet : energyNets.entrySet()) {
                int usedPower = energyNet.getKey().provideEnergy(this, new Vec3i(this.x, this.y, this.z), getOutputVoltage(energyNet.getValue()), Math.min(energy, getMaxEnergyOutput(null) - extracted));
                removeEnergy(usedPower);
                extracted += usedPower;
            }
        }
    }

    public void addedToNet(World world, int x, int y, int z, Network network) {
        if (network instanceof EnergyNetwork energyNet) {
            if (!energyNets.containsKey(energyNet)) {
                energyNets.put(energyNet, getNetSide(x,y,z,network));
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
            if (!energyNets.containsKey(energyNet)) {
                energyNets.put(energyNet, getNetSide(x,y,z,network));
            }
        }
    }
    
    public @Nullable Direction getNetSide(int x, int y, int z, Network network) {
        for (Direction side : Direction.values()) {
            if(network.isAt(x + side.getOffsetX(), y + side.getOffsetY(), z + side.getOffsetZ())) {
                return side;
            }
        }
        return null;
    }

    @Override
    public abstract int getMaxOutputVoltage(@Nullable Direction direction);

    @Override
    public abstract int getOutputVoltage(@Nullable Direction direction);

    @Override
    public abstract int getMaxEnergyOutput(@Nullable Direction direction);

    @Override
    public abstract boolean canExtractEnergy(@Nullable Direction direction);

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

    public int extracted = 0;
    @Override
    public int extractEnergy(@Nullable Direction direction, int requestedEnergy) {
        // If energy cannot be extracted on this side, return zero
        if(!canExtractEnergy(direction)) {
            return 0;
        }
        
        // If there is no energy, skip the calculations
        if(getEnergyStored() <= 0){
            return 0;
        }
        
        // Cap the requested energy at the max this machine will be able to provide
        requestedEnergy = Math.min(requestedEnergy, getMaxEnergyOutput(direction) - extracted);
        
        // If no or negative energy is requested, return zero
        if(requestedEnergy <= 0){
            return 0;
        }

        // Return the extracted energy
        int extractedEnergy = removeEnergy(requestedEnergy);
        
        // Increment the extracted value
        extracted += extractedEnergy;
        
        // Return the extracted energy
        return extractedEnergy;
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
