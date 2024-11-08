package net.danygames2014.nyalib.fluid;

import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.util.Identifier;

public record Fluid(Identifier identifier, Block still, Block flowing) {
    public Identifier getIdentifier() {
        return identifier;
    }
}
