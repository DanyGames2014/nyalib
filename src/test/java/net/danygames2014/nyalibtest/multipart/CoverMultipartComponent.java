package net.danygames2014.nyalibtest.multipart;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Box;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.util.Identifier;

public class CoverMultipartComponent extends MultipartComponent {
    public Block block;

    public CoverMultipartComponent(Block block) {
        this.block = block;
    }

    public CoverMultipartComponent() {
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Identifier blockId = BlockRegistry.INSTANCE.getId(block);
        if (blockId != null) {
            nbt.putString("blockId", blockId.toString());
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("blockId")) {
            this.block = BlockRegistry.INSTANCE.get(Identifier.of(nbt.getString("blockId")));
        }
    }

    @Override
    public ObjectArrayList<Box> getBoundingBoxes() {
        ObjectArrayList<Box> boxes = new ObjectArrayList<>();
        boxes.add(Box.createCached(this.x + 0.5D, this.y, this.z, this.x + 1.0D, this.y + 1.0D, this.z + 1.0D));
        return boxes;
    }

    @Override
    public void getCollisionBoxes(ObjectArrayList<Box> boxes) {
        boxes.add(Box.createCached(this.x + 0.5D, this.y, this.z, this.x + 1.0D, this.y + 1.0D, this.z + 1.0D));
    }

    @Override
    public void render(Tessellator tessellator, BlockRenderManager blockRenderManager, int renderLayer) {
        if (renderLayer != 0) {
            return;
        }
        blockRenderManager.renderBlock(block, x, y + renderLayer, z);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " { x=" + x + ", y=" + y + ", z=" + z + ", world=" + world + ", block= " + block + "}";
    }
}
