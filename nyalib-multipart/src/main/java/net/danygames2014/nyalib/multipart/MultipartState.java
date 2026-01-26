package net.danygames2014.nyalib.multipart;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.danygames2014.nyalib.NyaLibMultipart;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.SideUtil;

public class MultipartState {
    public int x;
    public int y;
    public int z;
    public World world;

    public ObjectArrayList<MultipartComponent> components;

    public MultipartState() {
        this.components = new ObjectArrayList<>();
    }

    public boolean addComponent(MultipartComponent component, boolean notify) {
        component.world = world;
        component.x = x;
        component.y = y;
        component.z = z;
        component.state = this;
        if (components.add(component)) {
            component.onPlaced();
            if (notify) {
                this.markDirty();
                world.notifyNeighbors(x, y, z, world.getBlockId(x, y, z));
            }
            return true;
        }

        return false;
    }

    public boolean removeComponent(int index, boolean notify) {
        return removeComponent(components.get(index), notify);
    }
    
    public boolean removeComponent(MultipartComponent component, boolean notify) {
        if (components.remove(component)) {
            if (components.isEmpty()) {
                world.setMultipartState(x, y, z, null);
            }
            
            if (notify) {
                this.markDirty();
                world.notifyNeighbors(x, y, z, world.getBlockId(x, y, z));
            }
            return true;
        }

        return false;
    }

    public void markDirty() {
        if (world != null) {
            if (!world.isRemote) {
                world.blockUpdateEvent(x, y, z);
                //noinspection Convert2MethodRef
                SideUtil.run(() -> {}, () -> sendUpdateToClient());
            }
            
            world.setBlockDirty(x,y,z);
        }
    }

    @Environment(EnvType.SERVER)
    public void sendUpdateToClient() {
        //noinspection deprecation
        if (FabricLoader.getInstance().getGameInstance() instanceof MinecraftServer server) {
            server.playerManager.markMultipartDirty(x, y, z, world.dimension.id);
        }
    }

    // Collision and Bounds checking
    public void getCollisionBoxes(ObjectArrayList<Box> boxes) {
        for (var component : components) {
            component.getCollisionBoxes(boxes);
        }
    }

    // Rendering
    public void render(Tessellator tessellator, BlockRenderManager blockRenderManager, int renderLayer) {
        for (MultipartComponent component : components) {
            component.render(tessellator, blockRenderManager, renderLayer);
        }
    }

    // NBT
    public void writeNbt(NbtCompound nbt) {
        NbtList componentNbtList = new NbtList();

        for (MultipartComponent component : components) {
            NbtCompound componentTag = new NbtCompound();
            componentTag.putString("id", MultipartComponentRegistry.getIdentifier(component.getClass()).toString());
            component.writeNbt(componentTag);
            componentNbtList.add(componentTag);
        }

        nbt.put("components", componentNbtList);
    }

    public void readNbt(NbtCompound nbt) {
        components.clear();

        NbtList componentNbtList = nbt.getList("components");
        for (int i = 0; i < componentNbtList.size(); i++) {
            NbtCompound componentNbt = (NbtCompound) componentNbtList.get(i);

            MultipartComponentFactory factory = MultipartComponentRegistry.get(Identifier.of(componentNbt.getString("id"))).factory;

            if (factory == null) {
                NyaLibMultipart.LOGGER.warn("Could not find MultipartComponent with id " + componentNbt.getString("id"));
                continue;
            }

            MultipartComponent component = factory.create();
            addComponent(component, false);
            component.readNbt(componentNbt);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        //noinspection StringConcatenationInsideStringBufferAppend
        sb.append("MultipartState { x= " + x + ", y= " + y + ", z= " + z + ", world= " + world + " }");
        for (var component : components) {
            sb.append("\n").append(component);
        }

        return sb.toString();
    }
}
