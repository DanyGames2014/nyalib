package net.danygames2014.nyalib.multipart;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class MultipartTickScheduler {
    public ObjectOpenHashSet<MultipartEvent> scheduledTicks = new ObjectOpenHashSet<>();
    private final ObjectOpenHashSet<MultipartEvent> pendingTicks = new ObjectOpenHashSet<>();
    private final ObjectOpenHashSet<MultipartEvent> toInitialize = new ObjectOpenHashSet<>();
    private boolean isTicking = false;

    public void scheduleTick(MultipartComponent component, long time) {
        MultipartEvent event = new MultipartEvent(component, time);
        if (isTicking) {
            pendingTicks.add(event);
        } else {
            scheduledTicks.add(event);
        }
    }

    public void writeNbt(NbtCompound nbt) {
        NbtList events = new NbtList();
        for(MultipartEvent event : scheduledTicks) {
            events.add(event.writeNbt());
        }
        if(events.size() > 0) {
            nbt.put("scheduledMultipartTicks", events);
        }
    }

    public void readNbt(NbtCompound nbt, World world) {
        if(nbt.contains("scheduledMultipartTicks")) {
            NbtList events = nbt.getList("scheduledMultipartTicks");
            for(int i = 0; i < events.size(); i++) {
                MultipartEvent event = MultipartEvent.fromNbt((NbtCompound) events.get(i));
                scheduledTicks.add(event);
                toInitialize.add(event);
            }
        }
    }

    public void initializeEvents(World world) {
        if(!toInitialize.isEmpty()) {
            for(MultipartEvent event : toInitialize) {
                event.initialize(world);
            }
            toInitialize.clear();
        }
    }

    public void tick(long currentTime) {
        isTicking = true;

        ObjectArrayList<MultipartEvent> toRemove = new ObjectArrayList<>();
        for(MultipartEvent event : scheduledTicks) {
            if(event.time <= currentTime) {
                if(event.initialized && event.component.state.components.contains(event.component)) {
                    event.component.onScheduledTick();
                }
                toRemove.add(event);
            }
        }

        if(!toRemove.isEmpty()) {
            scheduledTicks.removeAll(toRemove);
        }

        isTicking = false;

        if(!pendingTicks.isEmpty()) {
            scheduledTicks.addAll(pendingTicks);
            pendingTicks.clear();
        }
    }
}
