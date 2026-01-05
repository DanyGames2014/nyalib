package net.danygames2014.nyalibtest.block.fluid.item;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.danygames2014.nyalib.fluid.Fluid;
import net.danygames2014.nyalib.fluid.FluidBucket;
import net.danygames2014.nyalib.fluid.FluidRegistry;
import net.danygames2014.nyalib.fluid.Fluids;
import net.danygames2014.nyalibtest.NyaLibTest;
import net.danygames2014.nyalibtest.init.ClientListener;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;

public class FluidCellItem extends TemplateItem implements FluidBucket {
    public static final Object2ObjectOpenHashMap<Fluid, FluidCellItem> FLUID_CELL_ITEMS = new Object2ObjectOpenHashMap<>();
    public final Fluid fluid;
    
    public FluidCellItem(Identifier identifier, Fluid fluid) {
        super(identifier);
        this.fluid = fluid;
        this.maxCount = 16;
        FLUID_CELL_ITEMS.put(fluid, this);
        ClientListener.registerCellTexture(identifier);
    }


    @Override
    public Fluid getFluid() {
        return fluid;
    }

    @Override
    public Item getEmptyBucketItem() {
        return NyaLibTest.emptyCellItem;
    }

    @Override
    public Item getFullBucketItem(Fluid fluid) {
        return FLUID_CELL_ITEMS.get(fluid);
    }

    public ItemStack use(ItemStack stack, World world, PlayerEntity player) {
        float var5 = player.prevPitch + (player.pitch - player.prevPitch);
        float var6 = player.prevYaw + (player.yaw - player.prevYaw);
        double x = player.prevX + (player.x - player.prevX);
        double y = player.prevY + (player.y - player.prevY) + 1.62 - (double)player.standingEyeHeight;
        double z = player.prevZ + (player.z - player.prevZ);
        Vec3d headPos = Vec3d.createCached(x, y, z);
        float var14 = MathHelper.cos(-var6 * ((float)Math.PI / 180F) - (float)Math.PI);
        float var15 = MathHelper.sin(-var6 * ((float)Math.PI / 180F) - (float)Math.PI);
        float var16 = -MathHelper.cos(-var5 * ((float)Math.PI / 180F));
        float var17 = MathHelper.sin(-var5 * ((float)Math.PI / 180F));
        float var18 = var15 * var16;
        float var20 = var14 * var16;
        double var21 = 5.0F;
        Vec3d var23 = headPos.add((double)var18 * var21, (double)var17 * var21, (double)var20 * var21);
        HitResult hitResult = world.raycast(headPos, var23, this.fluid == null);
        
        if (hitResult == null) {
            return stack;
        }
        
        switch (hitResult.type) {
            case BLOCK -> {
                Vec3i pos = new Vec3i(hitResult.blockX, hitResult.blockY, hitResult.blockZ);

                if (!world.canInteract(player, pos.x, pos.y, pos.z)) {
                    return stack;
                }

                if (this.fluid == null) {
                    Fluid targetFluid = FluidRegistry.get(world.getBlockId(pos.x, pos.y, pos.z));

                    if (targetFluid != null && world.getBlockMeta(pos.x, pos.y, pos.z) == 0 && FLUID_CELL_ITEMS.containsKey(targetFluid)) {
                        world.setBlock(pos.x, pos.y, pos.z, 0);
                        return new ItemStack(FLUID_CELL_ITEMS.get(targetFluid));
                    }

                } else {
                    if (!this.fluid.isPlaceableInWorld()) {
                        return stack;
                    }

                    Direction side = Direction.byId(hitResult.side);
                    pos.x += side.getOffsetX();
                    pos.y += side.getOffsetY();
                    pos.z += side.getOffsetZ();
                    
                    if (world.isAir(pos.x, pos.y, pos.z) || !world.getMaterial(pos.x, pos.y, pos.z).isSolid()) {
                        if (world.dimension.evaporatesWater && this.fluid == Fluids.WATER) {
                            world.playSound(x + (double) 0.5F, y + (double) 0.5F, z + (double) 0.5F, "random.fizz", 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);

                            for (int particle = 0; particle < 8; ++particle) {
                                world.addParticle("largesmoke", (double) pos.x + Math.random(), (double) pos.y + Math.random(), (double) pos.z + Math.random(), 0.0F, 0.0F, 0.0F);
                            }
                        } else {
                            world.setBlock(pos.x, pos.y, pos.z, this.fluid.getFlowingBlock().id, 0);
                        }

                        return new ItemStack(this.getEmptyBucketItem());
                    }
                }
            }
            
            case ENTITY -> {
                if (this.fluid == null && hitResult.entity instanceof CowEntity) {
                    return new ItemStack(NyaLibTest.milkCellItem);
                }
            }
        }
        
        return stack;
    }
}
