package com.br3adjam.midi2noteblocks.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NoteBlock;

public class NoteblockControl {
    Level level;
    public NoteblockControl(Level level) {
        this.level = level;
    }

    public void placeNoteblock(BlockPos pos, int pitch) {
        level.setBlock(pos, Blocks.NOTE_BLOCK.defaultBlockState()
                .setValue(NoteBlock.NOTE, Math.max(Math.min(pitch, 24), 0) ),
            3); // TODO)) make sure this works + make pitch changeable
//        level.getBlockState(pos).getBlock().triggerEvent(level.getBlockState(pos), level, pos, 0, 0);
        level.blockEvent(pos, Blocks.NOTE_BLOCK, 0, 0);

    }
}
