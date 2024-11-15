package net.danygames2014.nyalib.energy;

@SuppressWarnings("unused")
public interface EnergyStorage extends EnergyHandler {
    int getStoredEnergy();
    
    int getEnergyCapacity();
    
    default int getRemainingCapacity(){
        return getEnergyCapacity() - getStoredEnergy();
    }
    
    int setEnergy(int energy);
    
    int changeEnergy(int amount);
    
    default int addEnergy(int amount){
        return changeEnergy(amount);
    }
    
    default int removeEnergy(int amount){
        return changeEnergy(-amount);
    }
}
