package com.br3adjam.midi2noteblocks.world;

import com.br3adjam.midi2noteblocks.command.TestCommands;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.RepeaterBlock;

public class BlockControl {
    static Level level;
    public BlockControl(Level level) {
        this.level = level;
    }

    public void placeNoteblock(BlockPos pos, int pitch) {
        level.setBlock(pos, Blocks.NOTE_BLOCK.defaultBlockState()
                .setValue(NoteBlock.NOTE, Math.max(Math.min(pitch, 24), 0) ),
            3); // TODO)) make sure this works + make pitch changeable
//        level.getBlockState(pos).getBlock().triggerEvent(level.getBlockState(pos), level, pos, 0, 0);
        level.blockEvent(pos, Blocks.NOTE_BLOCK, 0, 0);

    }

    public static void placeNext(RedstoneGeneratorParams redstoneGeneratorParams) {
        BlockPos repeaterPos = redstoneGeneratorParams.startPos.relative(redstoneGeneratorParams.direction.getAxis(), 2*redstoneGeneratorParams.step + 1);
        BlockPos redstonePos = repeaterPos.relative(redstoneGeneratorParams.direction.getAxis(), 1);

        // todo)) make the redstone actually go in the direction it's supposed to

        // wool
        level.setBlock(repeaterPos.below(), Blocks.WHITE_WOOL.defaultBlockState(), 3);
        // repeater
        level.setBlock(repeaterPos, Blocks.REPEATER.defaultBlockState().
                setValue(RepeaterBlock.FACING, redstoneGeneratorParams.direction), 3);

        // wool
        level.setBlock(redstonePos.below(), Blocks.WHITE_WOOL.defaultBlockState(), 3);
        // redstone
        level.setBlock(redstonePos, Blocks.REDSTONE_WIRE.defaultBlockState(), 3);


        redstoneGeneratorParams.step++;
        System.out.println("next step");
    }
}
