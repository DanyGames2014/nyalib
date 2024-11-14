package net.danygames2014.nyalib.fluid;

import net.minecraft.block.Block;
import net.minecraft.client.resource.language.I18n;
import net.modificationstation.stationapi.api.util.Identifier;

public record Fluid(Identifier identifier, Block still, Block flowing) {
    public Identifier getIdentifier() {
        return identifier;
    }

    public String getTranslationKey() {
        return "fluid." + identifier.namespace + "." + identifier.path + ".name";
    }

    public String getTranslatedName() {
        return I18n.getTranslation(getTranslationKey());
    }
}
