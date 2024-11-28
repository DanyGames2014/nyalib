package net.danygames2014.nyalibtest.block.energy;

import net.danygames2014.nyalib.network.NetworkNodeComponent;
import net.danygames2014.nyalib.network.NetworkType;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class WireBlock extends TemplateBlock implements NetworkNodeComponent {
    public WireBlock(Identifier identifier) {
        super(identifier, Material.METAL);
    }

    @Override
    public NetworkType getNetworkType() {
        return NetworkType.ENERGY;
    }
}
