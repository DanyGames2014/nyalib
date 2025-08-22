package net.danygames2014.nyalib.fluid.item;

import net.danygames2014.nyalib.fluid.Fluid;
import net.modificationstation.stationapi.api.template.item.TemplateBucketItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class FluidBucket extends TemplateBucketItem {
    public FluidBucket(Identifier identifier, Fluid fluid) {
        super(identifier, 0);
    }
}
