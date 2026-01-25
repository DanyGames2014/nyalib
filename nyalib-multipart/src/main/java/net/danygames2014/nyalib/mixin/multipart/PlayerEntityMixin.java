package net.danygames2014.nyalib.mixin.multipart;

import net.danygames2014.nyalib.mixininterface.MultipartPlayer;
import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(PlayerEntity.class)
public class PlayerEntityMixin extends LivingEntity implements MultipartPlayer {
    @Shadow
    public PlayerInventory inventory;

    public PlayerEntityMixin(World world) {
        super(world);
    }

    @Override
    public float getMultipartBreakingSpeed(int x, int y, int z, MultipartComponent component) {
        float breakingSpeed = this.inventory.getStrengthOnMultipart(x, y, z, component);

        if (this.isInFluid(Material.WATER)) {
            breakingSpeed /= 5.0F;
        }

        if (!this.onGround) {
            breakingSpeed /= 5.0F;
        }

        return breakingSpeed;
    }

    @Override
    public boolean canHarvestMultipart(int x, int y, int z, MultipartComponent component) {
        return this.inventory.isUsingEffectiveToolOnMultipart(x, y, z, component);
    }
}
