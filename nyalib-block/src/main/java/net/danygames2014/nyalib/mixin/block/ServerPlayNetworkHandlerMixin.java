package net.danygames2014.nyalib.mixin.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.danygames2014.nyalib.block.voxelshape.HasCollisionVoxelShape;
import net.danygames2014.nyalib.block.voxelshape.HasVoxelShape;
import net.danygames2014.nyalib.block.voxelshape.VoxelShape;
import net.minecraft.block.Block;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow
    private ServerPlayerEntity player;
    @Shadow
    private MinecraftServer server;

    @WrapOperation(method = "onPlayerMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;teleport(DDDFF)V", ordinal = 0))
    private void verifyBlockIntersectionForBlockVoxelShapes(ServerPlayNetworkHandler instance, double x, double y, double z, float yaw, float pitch, Operation<Void> original) {
        ServerWorld world = this.server.getWorld(this.player.dimensionId);
        Box originalPlayerBox = player.boundingBox;
        Box playerBox = Box.create(originalPlayerBox.minX, originalPlayerBox.minY, originalPlayerBox.minZ, originalPlayerBox.maxX, originalPlayerBox.maxY, originalPlayerBox.maxZ);

        Vec3i min = new Vec3i((int) Math.floor(playerBox.minX), (int) Math.floor(playerBox.minY), (int) Math.floor(playerBox.minZ));
        Vec3i max = new Vec3i((int) Math.ceil(playerBox.maxX), (int) Math.ceil(playerBox.maxY), (int) Math.ceil(playerBox.maxZ));

        boolean collisionVerified = false;

        for (int blockX = min.x; blockX <= max.x; blockX++) {
            for (int blockY = min.y; blockY <= max.y; blockY++) {
                for (int blockZ = min.z; blockZ <= max.z; blockZ++) {
                    Block block = world.getBlockState(blockX, blockY, blockZ).getBlock();
                    List<Box> boxes = null;

                    if (block instanceof HasCollisionVoxelShape hasCollisionVoxelShape) {
                        VoxelShape voxelShape = hasCollisionVoxelShape.getCollisionVoxelShape(world, blockX, blockY, blockZ);
                        if (voxelShape != null) {
                            boxes = voxelShape.getOffsetBoxes();
                        }
                    } else if (block instanceof HasVoxelShape hasVoxelShape) {
                        VoxelShape voxelShape = hasVoxelShape.getVoxelShape(world, blockX, blockY, blockZ);
                        if (voxelShape != null) {
                            boxes = voxelShape.getOffsetBoxes();
                        }
                    } else {
                        Box collisionShape = block.getCollisionShape(world, blockX, blockY, blockZ);
                        if (collisionShape != null) {
                            boxes = List.of(collisionShape);
                        }
                    }

                    if (boxes != null) {
                        for (Box blockBoxPart : boxes) {
                            if (blockBoxPart == null) continue;
                            if (playerBox.intersects(blockBoxPart)) {
                                collisionVerified = true;
                            }
                        }
                    }
                }
            }
        }

        if (collisionVerified) {
            original.call(instance, x, y, z, yaw, pitch);
        }
    }
}