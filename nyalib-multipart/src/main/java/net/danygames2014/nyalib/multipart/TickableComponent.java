package net.danygames2014.nyalib.multipart;

import org.jetbrains.annotations.NotNull;

public interface TickableComponent {
    void tick();

    @NotNull MultipartState getState();
}
