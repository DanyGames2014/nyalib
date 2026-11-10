package net.danygames2014.nyalib.entity.maxhealth.mixininterface;

public interface NyaLibMaxHealthLivingEntity {
    default int getMaxHealth() {
        return 20;
    }
}
