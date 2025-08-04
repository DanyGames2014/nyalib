package net.danygames2014.nyalib.capability.block.energyhandler;

import net.danygames2014.nyalib.capability.block.BlockCapabilityProvider;
import net.danygames2014.nyalib.energy.EnergyStorage;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EnergyStorageInterfaceBlockCapabilityProvider extends BlockCapabilityProvider<EnergyStorageInterfaceBlockCapability> {
    @Override
    public @Nullable EnergyStorageInterfaceBlockCapability getCapability(World world, int x, int y, int z) {
        if (world.getBlockEntity(x,y,z) instanceof EnergyStorage energyStorage) {
            return new EnergyStorageInterfaceBlockCapability(energyStorage);
        }
        
        return null;
    }
}
