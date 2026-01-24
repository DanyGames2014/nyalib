package net.danygames2014.nyalib.block;

import net.danygames2014.nyalib.init.multipart.BlockListener;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class MultipartBlock extends TemplateBlock {
    public MultipartBlock(Identifier identifier) {
        super(identifier, BlockListener.multipartMaterial);
        this.setBoundingBox(0.4F, 0.4F, 0.4F, 0.6F, 0.6F, 0.6F);
    }
}