package net.danygames2014.nyalib.multipart;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class MultipartDynamicRenderDispatcher {
    public static MultipartDynamicRenderDispatcher INSTANCE = new MultipartDynamicRenderDispatcher();

    public ObjectOpenHashSet<DynamicRenderComponent> renderers = new ObjectOpenHashSet<>();

    public World world;

    public LivingEntity camera;
    public float cameraYaw;
    public float cameraPitch;
    public double cameraX;
    public double cameraY;
    public double cameraZ;

    public boolean addDynamicRenderer(DynamicRenderComponent component) {
        return renderers.add(component);
    }

    public boolean removeDynamicRenderer(DynamicRenderComponent component) {
        return renderers.remove(component);
    }

    public void renderComponents(float tickDelta) {
        for(DynamicRenderComponent renderer : renderers) {
            MultipartComponent component = (MultipartComponent) renderer;
            if(component.world == null || component.world.dimension != world.dimension) {
                continue;
            }
            renderer.renderDynamic(component.x - cameraX, component.y - cameraY, component.z - cameraZ, tickDelta);
        }
    }

    public void prepare(World world, LivingEntity camera, float tickDelta) {
        this.world = world;
        this.camera = camera;
        this.cameraYaw = camera.prevYaw + (camera.yaw - camera.prevYaw) * tickDelta;
        this.cameraPitch = camera.prevPitch + (camera.pitch - camera.prevPitch) * tickDelta;
        this.cameraX = camera.lastTickX + (camera.x - camera.lastTickX) * (double)tickDelta;
        this.cameraY = camera.lastTickY + (camera.y - camera.lastTickY) * (double)tickDelta;
        this.cameraZ = camera.lastTickZ + (camera.z - camera.lastTickZ) * (double)tickDelta;
    }
}
