package net.danygames2014.nyalibtest.capability.meow;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class DefaultMeowBlockCapability extends MeowBlockCapability {
    private final World world;
    private final int x;
    private final int y;
    private final int z;

    public DefaultMeowBlockCapability(World world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void meow() {
        PlayerEntity player = world.getClosestPlayer(x, y, z, 10);
        
        if (player != null) {
            player.sendMessage("Meow!");
        }
    }
}
