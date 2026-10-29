package net.danygames2014.nyalib.energy.template.block;

import net.danygames2014.nyalib.energy.EnergyCapable;
import net.danygames2014.nyalib.energy.EnergyConductor;
import net.danygames2014.nyalib.network.Network;
import net.danygames2014.nyalib.network.NetworkNodeComponent;
import net.danygames2014.nyalib.network.NetworkType;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public abstract class EnergyWireBlockTemplate extends TemplateBlock implements NetworkNodeComponent, EnergyConductor {
    public EnergyWireBlockTemplate(Identifier identifier, Material material) {
        super(identifier, material);
    }

    @Override
    public NetworkType getNetworkType() {
        return NetworkType.ENERGY;
    }

    @Override
    public boolean canConnectTo(World world, int x, int y, int z, @Nullable Network network, Direction direction) {
        if (world.getBlockEntity(x + direction.getOffsetX(), y + direction.getOffsetY(), z + direction.getOffsetZ()) instanceof EnergyCapable energyCapable) {
            return energyCapable.canConnectEnergy(direction.getOpposite());
        }
        
        return NetworkNodeComponent.super.canConnectTo(world, x, y, z, network, direction);
    }
}
