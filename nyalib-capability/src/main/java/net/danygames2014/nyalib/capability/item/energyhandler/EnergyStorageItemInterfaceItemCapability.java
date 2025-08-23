package net.danygames2014.nyalib.capability.item.energyhandler;

import net.danygames2014.nyalib.energy.EnergyStorageItem;
import net.minecraft.item.ItemStack;

class EnergyStorageItemInterfaceItemCapability extends EnergyStorageItemCapability {
    private final EnergyStorageItem energyStorageItem;
    private final ItemStack stack;

    public EnergyStorageItemInterfaceItemCapability(ItemStack stack, EnergyStorageItem energyStorageItem) {
        this.stack = stack;
        this.energyStorageItem = energyStorageItem;
    }

    // Storage
    @Override
    public int getEnergyStored() {
        return energyStorageItem.getEnergyStored(stack);
    }

    @Override
    public int getEnergyCapacity() {
        return energyStorageItem.getEnergyCapacity(stack);
    }

    @Override
    public int getRemainingCapacity() {
        return energyStorageItem.getRemainingCapacity(stack);
    }

    @Override
    public int setEnergy(int value) {
        return energyStorageItem.setEnergy(stack, value);
    }

    @Override
    public int changeEnergy(int difference) {
        return energyStorageItem.changeEnergy(stack, difference);
    }

    @Override
    public int addEnergy(int amount) {
        return energyStorageItem.addEnergy(stack, amount);
    }

    @Override
    public int removeEnergy(int amount) {
        return energyStorageItem.removeEnergy(stack, amount);
    }

    // Input
    @Override
    public boolean canReceiveEnergy() {
        return energyStorageItem.canReceiveEnergy(stack);
    }

    @Override
    public int getMaxEnergyInput() {
        return energyStorageItem.getMaxEnergyInput(stack);
    }

    @Override
    public int receiveEnergy(int energy) {
        return energyStorageItem.receiveEnergy(stack, energy);
    }

    // Output
    @Override
    public boolean canExtractEnergy() {
        return energyStorageItem.canExtractEnergy(stack);
    }

    @Override
    public int getMaxEnergyOutput() {
        return energyStorageItem.getMaxEnergyOutput(stack);
    }

    @Override
    public int extractEnergy(int requestedEnergy) {
        return energyStorageItem.extractEnergy(stack, requestedEnergy);
    }
}
