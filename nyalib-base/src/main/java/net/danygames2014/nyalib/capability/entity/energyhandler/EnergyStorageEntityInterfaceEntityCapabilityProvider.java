package net.danygames2014.nyalib.capability.entity.energyhandler;

import net.danygames2014.nyalib.capability.entity.EntityCapabilityProvider;
import net.danygames2014.nyalib.energy.EnergyStorageEntity;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class EnergyStorageEntityInterfaceEntityCapabilityProvider extends EntityCapabilityProvider<EnergyStorageEntityCapability> {
    @Override
    public @Nullable EnergyStorageEntityCapability getCapability(Entity entity) {
        if (entity instanceof EnergyStorageEntity energyStorageEntity) {
            return new EnergyStorageEntityInterfaceEntityCapability(energyStorageEntity);
        }
        
        return null;
    }
}
