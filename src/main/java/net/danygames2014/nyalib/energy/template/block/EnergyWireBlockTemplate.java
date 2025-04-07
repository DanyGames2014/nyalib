package net.danygames2014.nyalib.energy.template.block;

import net.danygames2014.nyalib.energy.EnergyConductor;
import net.danygames2014.nyalib.network.NetworkNodeComponent;
import net.danygames2014.nyalib.network.NetworkType;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public abstract class EnergyWireBlockTemplate extends TemplateBlock implements NetworkNodeComponent, EnergyConductor {
    public EnergyWireBlockTemplate(Identifier identifier, Material material) {
        super(identifier, material);
    }

    @Override
    public NetworkType getNetworkType() {
        return NetworkType.ENERGY;
    }
}
