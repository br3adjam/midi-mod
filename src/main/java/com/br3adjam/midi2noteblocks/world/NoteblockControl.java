package com.br3adjam.midi2noteblocks.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NoteBlock;

public class NoteblockControl {
    public void placeNoteblock(BlockPos pos, Level level) {
        level.setBlock(pos, Blocks.NOTE_BLOCK.defaultBlockState(), 3); // TODO)) make sure this works + make pitch changeable
    }
}
