package net.danygames2014.nyalib.energy.template.block;

import net.danygames2014.nyalib.network.NetworkEdgeComponent;
import net.danygames2014.nyalib.network.NetworkType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;

/**
 * A template for an Energy Consumer Block
 * <p>
 * <p>In the {@link #createBlockEntity()} method, return your Block Entity implementation which extends {@link net.danygames2014.nyalib.energy.template.block.entity.EnergyConsumerBlockEntityTemplate} 
 */
public abstract class EnergyConsumerBlockTemplate extends TemplateBlockWithEntity implements NetworkEdgeComponent {
    public EnergyConsumerBlockTemplate(Identifier identifier, Material material) {
        super(identifier, material);
    }

    @Override
    public NetworkType getNetworkType() {
        return NetworkType.ENERGY;
    }

    @Override
    protected abstract BlockEntity createBlockEntity();
}
