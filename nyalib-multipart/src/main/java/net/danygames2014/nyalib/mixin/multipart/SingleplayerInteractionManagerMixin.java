package net.danygames2014.nyalib.mixin.multipart;

import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.minecraft.block.Block;
import net.minecraft.client.SingleplayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(SingleplayerInteractionManager.class)
public class SingleplayerInteractionManagerMixin extends InteractionManagerMixin {
    @Override
    public void attackMultipart(ItemStack selectedStack, int x, int y, int z, Vec3d pos, Direction face, MultipartComponent component) {
        
    }

    @Override
    public boolean interactMultipart(ItemStack stack, int x, int y, int z, Vec3d pos, Direction face, MultipartComponent component) {
        return false;
    }

    @Override
    public void processMultipartBreakingAction(int x, int y, int z, Vec3d pos, Direction face, MultipartComponent component) {
        if (this.multipartBreakingDelayTicks > 0) {
            --this.multipartBreakingDelayTicks;
            return;
        }
        
        if (x == this.multipartBreakingPosX && y == this.multipartBreakingPosY && z == this.multipartBreakingPosZ) {
            int blockId = this.minecraft.world.getBlockId(x, y, z);
            if (blockId == 0) {
                return;
            }

            Block var6 = Block.BLOCKS[blockId];
            this.multipartBreakingProgress += var6.getHardness(this.minecraft.player);
            if (this.multipartBreakingSoundDelayTicks % 4.0F == 0.0F) {
                this.minecraft.soundManager.playSound(var6.soundGroup.getSound(), (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, (var6.soundGroup.getVolume() + 1.0F) / 8.0F, var6.soundGroup.getPitch() * 0.5F);
            }

            this.multipartBreakingSoundDelayTicks++;
            if (this.multipartBreakingProgress >= 1.0F) {
                //this.breakBlock(x, y, z, side);
                this.multipartBreakingProgress = 0.0F;
                this.lastMultipartBreakingProgress = 0.0F;
                this.multipartBreakingSoundDelayTicks = 0.0F;
                this.multipartBreakingDelayTicks = 5;
            }
        } else {
            this.multipartBreakingProgress = 0.0F;
            this.lastMultipartBreakingProgress = 0.0F;
            this.multipartBreakingSoundDelayTicks = 0.0F;
            this.multipartBreakingPosX = x;
            this.multipartBreakingPosY = y;
            this.multipartBreakingPosZ = z;
        }

    }

    @Override
    public void cancelMultipartBreaking() {
        multipartBreakingProgress = 0.0F;
        multipartBreakingDelayTicks = 0;
    }
}
