package net.danygames2014.nyalib.world.structure;

public enum CollisionType {
    // This will prevent the entire structure from generating at all
    DONT_GENERATE,

    // This will prevent placing the conflicting block
    DONT_PLACE,

    // This will replace the existing block with the structure one
    REPLACE_BLOCK
}
