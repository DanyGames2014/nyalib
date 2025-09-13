package net.danygames2014.nyalib.compat.whatsthis.providers;

import net.danygames2014.nyalib.NyaLibCompat;
import net.danygames2014.nyalib.capability.CapabilityHelper;
import net.danygames2014.nyalib.capability.entity.itemhandler.ItemHandlerEntityCapability;
import net.danygames2014.whatsthis.Util;
import net.danygames2014.whatsthis.api.IProbeHitEntityData;
import net.danygames2014.whatsthis.api.IProbeInfo;
import net.danygames2014.whatsthis.api.IProbeInfoEntityProvider;
import net.danygames2014.whatsthis.api.ProbeMode;
import net.danygames2014.whatsthis.config.Config;
import net.danygames2014.whatsthis.config.ConfigSetup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ItemEntityProbeInfoProvider implements IProbeInfoEntityProvider {
    @Override
    public String getID() {
        return NyaLibCompat.NAMESPACE.id("nyalib_entity_inventory").toString();
    }

    @Override
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, Entity entity, IProbeHitEntityData data) {
        if (entity instanceof PlayerEntity) {
            return;
        }

        ItemHandlerEntityCapability itemHandler = CapabilityHelper.getCapability(entity, ItemHandlerEntityCapability.class);
        if (itemHandler != null) {
            if (Util.show(mode, Config.PROVIDER_CONFIG.showChestContents)) {
                EntityInventoryInfo.showInventoryInfo(mode, probeInfo, world, entity, ConfigSetup.getProbeConfig());
            }
        }
    }
}
