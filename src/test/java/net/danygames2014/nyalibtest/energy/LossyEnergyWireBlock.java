package net.danygames2014.nyalibtest.energy;

import net.modificationstation.stationapi.api.util.Identifier;

public class LossyEnergyWireBlock extends WireBlock {
    public LossyEnergyWireBlock(Identifier identifier) {
        super(identifier);
    }

    @Override
    public double getEnergyLossPerBlock() {
        return 0.5D;
    }
}
