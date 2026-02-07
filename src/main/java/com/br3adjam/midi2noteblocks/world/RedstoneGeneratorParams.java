package com.br3adjam.midi2noteblocks.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class RedstoneGeneratorParams {
    // if null, redstone generator is not running

    public final BlockPos startPos; // starting position of the redstone / repeater chain
    public final Direction direction; // direction the redstone / repeater chain is going in

    public RedstoneGeneratorParams(BlockPos startPos, Direction direction) {
        this.startPos = startPos;
        this.direction = direction;
    }

    // current step the redstone / repeater chain is on, used find the correct position to place the next repeater + redstone
    public int step = 0;

    // todo)) find a better name
}
