package net.danygames2014.nyalibtest.multipart;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.danygames2014.nyalib.multipart.MultipartComponent;
import net.danygames2014.nyalib.util.BoxUtil;
import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.Box;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;

public class CoverMultipartComponent extends MultipartComponent {
    public Block block;
    public Direction direction;
    public Box bounds = Box.create(0f, 0f, 0f, 0.5f, 1f, 1f);

    public CoverMultipartComponent(Block block, Direction direction) {
        this.block = block;
        this.direction = direction;
    }

    public CoverMultipartComponent() {
    }

    @Override
    public BlockSoundGroup getSoundGroup() {
        return block.soundGroup;
    }

    @Override
    public ObjectArrayList<ItemStack> getDropList() {
        ObjectArrayList<ItemStack> dropList = new ObjectArrayList<>();
        dropList.add(new ItemStack(block));
        return dropList;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Identifier blockId = BlockRegistry.INSTANCE.getId(block);
        if (blockId != null) {
            nbt.putString("blockId", blockId.toString());
        }
        if(direction != null){
            nbt.putInt("direction", direction.getId());
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("blockId")) {
            this.block = BlockRegistry.INSTANCE.get(Identifier.of(nbt.getString("blockId")));
        }
        if (nbt.contains("direction")){
            direction = Direction.byId(nbt.getInt("direction"));
        } else {
            direction = Direction.NORTH;
        }
    }

    @Override
    public ObjectArrayList<Box> getBoundingBoxes() {
        ObjectArrayList<Box> boxes = new ObjectArrayList<>();
        boxes.add(BoxUtil.rotate(bounds, direction).offset(x, y, z));
        return boxes;
    }

    @Override
    public void getCollisionBoxes(ObjectArrayList<Box> boxes) {
        boxes.add(BoxUtil.rotate(bounds, direction).offset(x, y, z));
    }

    @Override
    public void render(Tessellator tessellator, BlockRenderManager blockRenderManager, int renderLayer) {
        if (renderLayer != 0) {
            return;
        }
//        blockRenderManager.renderBlock(block, x, y + renderLayer, z);
    }

    @Override
    public void onBreakStart() {
        int dir = direction.getId();
        dir++;
        if(dir >= Direction.values().length){
            dir = 0;
        }
        direction = Direction.byId(dir);
        System.out.println(direction);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " { x=" + x + ", y=" + y + ", z=" + z + ", world=" + world + ", block= " + block + "}";
    }
}
