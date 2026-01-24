package net.danygames2014.nyalib.init.multipart;

import net.danygames2014.nyalib.NyaLibMultipart;
import net.danygames2014.nyalib.block.MultipartBlock;
import net.danygames2014.nyalib.block.material.MultipartMaterial;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;

public class BlockListener {
    public static Block multipartBlock;
    
    public static Material multipartMaterial;
    
    @EventListener
    public void registerBlocks(BlockRegistryEvent event) {
        multipartMaterial = new MultipartMaterial();

        multipartBlock = new MultipartBlock(NyaLibMultipart.NAMESPACE.id("multipart")).setTranslationKey(NyaLibMultipart.NAMESPACE, "multipart");
    }
}
