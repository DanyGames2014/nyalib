package net.danygames2014.nyalib.mixin.multipart;

import it.unimi.dsi.fastutil.objects.ObjectCollection;
import net.danygames2014.nyalib.multipart.MultipartState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.modificationstation.stationapi.impl.world.FlattenedWorldManager;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlattenedWorldManager.class)
public class FlattenedWorldManagerMixin {
    @Inject(method = "saveChunk", at = @At(value = "TAIL"))
    private static void saveMultipart(FlattenedChunk chunk, World world, NbtCompound chunkTag, CallbackInfo ci) {
        ObjectCollection<MultipartState> multipartStates = chunk.getMultipartStates();
        
        // If there are no multiparts, do not bother
        if (multipartStates == null || multipartStates.isEmpty()) {
            return;
        }

        multipartStates.removeIf(multipartState -> multipartState.components.isEmpty());
        
        // Save the multipart data
        NbtCompound multipartNbt = new NbtCompound();

        // Write all the multipart states in the chunk
        NbtList multipartNbtList = new NbtList();
        for (MultipartState multipartState : multipartStates) {
            NbtCompound multipartNbtCompound = new NbtCompound();
            
            multipartNbtCompound.putInt("x", multipartState.x & 15);
            multipartNbtCompound.putInt("y", multipartState.y);
            multipartNbtCompound.putInt("z", multipartState.z & 15);
            multipartState.writeNbt(multipartNbtCompound);
            
            multipartNbtList.add(multipartNbtCompound);
        }
        multipartNbt.put("states", multipartNbtList);
        
        // Add the multipart tag to the chunk
        chunkTag.put("NyaLibMultipart", multipartNbt);
    }
    
    @Inject(method = "loadChunk", at = @At("RETURN"))
    private static void loadMultipart(World world, NbtCompound chunkTag, CallbackInfoReturnable<Chunk> cir){
        // If there is no multipart data, do not bother
        if (!chunkTag.contains("NyaLibMultipart")) {
            return;
        }
        
        Chunk chunk = cir.getReturnValue();
        
        // Get the multipart tag from the chunk
        NbtCompound multipartNbt = chunkTag.getCompound("NyaLibMultipart");
        
        // Read all the multipart states
        NbtList multipartNbtList = multipartNbt.getList("states");
        for (int i = 0; i < multipartNbtList.size(); i++) {
            NbtCompound multipartNbtCompound = (NbtCompound) multipartNbtList.get(i);
            
            int x = multipartNbtCompound.getInt("x");
            int y = multipartNbtCompound.getInt("y");
            int z = multipartNbtCompound.getInt("z");
            
            MultipartState state = new MultipartState();
            chunk.setMultipartState(x, y, z, state);
            if (multipartNbtCompound.contains("components")) {
                state.readNbt(multipartNbtCompound);
            }
        }
    }
}
