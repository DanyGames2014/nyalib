package net.danygames2014.nyalibtest.block;

import com.oath.cyclops.types.futurestream.Continuation;
import net.danygames2014.nyalib.network.*;
import net.danygames2014.nyalibtest.NyaLibTest;
import net.danygames2014.nyalibtest.blockentity.EmptyBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;

public class CableBlock extends TemplateBlock implements NetworkComponent {
    public static World theWorld;

    public CableBlock(Identifier identifier) {
        super(identifier, Material.STONE);
    }

    @Override
    public NetworkType getNetworkType() {
        return NyaLibTest.energyNetworkType;
    }

    @Override
    public void onPlaced(World world, int x, int y, int z) {
        super.onPlaced(world, x, y, z);
//        NetworkManager.addBlock(world, x, y, z, this);
        theWorld = world;
    }

//    @Override
//    protected BlockEntity createBlockEntity() {
//        return new EmptyBlockEntity();
//    }

//    @Override
//    public void onBreak(World world, int x, int y, int z) {
//        NetworkManager.removeBlock(world, x, y, z, this);
//        super.onBreak(world, x, y, z);
//    }

    @Override
    public void update(World world, int x, int y, int z, Network network) {
        world.method_244(x,y,z, 0);
    }

    @Override
    public int getColorMultiplier(BlockView blockView, int x, int y, int z) {
        if (theWorld != null) {
            Network net = NetworkManager.getAt(theWorld.dimension, x, y, z, this.getNetworkTypes().get(0).getIdentifier());

            if (net != null) {
                return net.hashCode();
            }
        }

        return 0;
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        Network net = NetworkManager.getAt(world.dimension, x, y, z, this.getNetworkTypes().get(0).getIdentifier());
        if (net != null) {
            player.method_490("NET " + net.getId() + " HASHCODE: " + net.hashCode());
        }
        return true;
    }
}
