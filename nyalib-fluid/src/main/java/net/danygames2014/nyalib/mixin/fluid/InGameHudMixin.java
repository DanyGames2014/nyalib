package net.danygames2014.nyalib.mixin.fluid;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.danygames2014.nyalib.block.FlowingFluidBlock;
import net.danygames2014.nyalib.block.StillFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.modificationstation.stationapi.api.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ClientPlayerEntity;isInFluid(Lnet/minecraft/block/material/Material;)Z"))
    public boolean displayTheAirBar(ClientPlayerEntity player, Material material, Operation<Boolean> original){
        double eyeY = player.y + (double)player.getEyeHeight();

        int blockX = MathHelper.floor(player.x);
        int blockY = MathHelper.floor((float)MathHelper.floor(eyeY));
        int blockZ = MathHelper.floor(player.z);

        BlockState state = player.world.getBlockState(blockX, blockY, blockZ);

        if (state.getBlock() instanceof StillFluidBlock stillFluidBlock) {
            if (stillFluidBlock.fluid.willDrown(player)) {
                return player.isInFluid(stillFluidBlock.fluid.getMaterial());
            }
        } else if (state.getBlock() instanceof FlowingFluidBlock flowingFluidBlock) {
            if (flowingFluidBlock.fluid.willDrown(player)) {
                return player.isInFluid(flowingFluidBlock.fluid.getMaterial());
            }
        }

        return original.call(player, material);
    }
}
